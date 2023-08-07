package com.pay.wxpayback.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.pay.wxpayback.constant.SystemConstant;
import com.pay.wxpayback.constant.wxpay.WXOrderConstant;
import com.pay.wxpayback.constant.wxpay.WechatPayHttpHeaders;
import com.pay.wxpayback.exception.ApiException;
import com.pay.wxpayback.pojo.ToolWxConfig;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * @Description
 * @Author 小乌龟
 * @Date 2022/4/10 20:14
 */
public class WxPayUtil {
    private static final Logger log = LoggerFactory.getLogger(WxPayUtil.class);

    /**
      *签名生成 计算签名
      * @param message  签名体
      * @param privateKey 商户私钥
      * @return String
      * @author zhangjunrong
      * @date 2022/4/10 20:15
      */
    public static String sign(byte[] message, PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }

   /**
     *构造HttpClient 实现 微信申请接口 调用功能
     * @param wxConfig 微信支付数据库参数
     * @param verifier 微信验签器
     * @return CloseableHttpClient
     * @author zhangjunrong
     * @date 2022/5/16 8:54
     */
    public static CloseableHttpClient getHttpClient(ToolWxConfig wxConfig, Verifier verifier) {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(wxConfig.getPrivateKey().getBytes(StandardCharsets.UTF_8)));
        //通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(wxConfig.getMchId(), wxConfig.getMchSerialNo(), merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier));
        return builder.build();
    }
    /**
      *构造HttpClient 实现 微信申请接口 调用功能  不用微信签名认证 账单申请中可用到
      * @param wxConfig 微信支付数据库参数
      * @param verifier 微信验签器
      * @return CloseableHttpClient
      * @author zhangjunrong
      * @date 2022/6/29 20:49
      */
    public static CloseableHttpClient getHttpClientNoSign(ToolWxConfig wxConfig, Verifier verifier) {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(wxConfig.getPrivateKey().getBytes(StandardCharsets.UTF_8)));
        //通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(wxConfig.getMchId(), wxConfig.getMchSerialNo(), merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .withValidator(response -> true);
        return builder.build();
    }

    /**
      *获取验证功能
      * @param wxConfig 微信支付数据库参数
      * @return Verifier 微信验签器
      * @author zhangjunrong
      * @date 2022/5/16 8:53
      */
    public static Verifier getVerifier(ToolWxConfig wxConfig)  {
        try {
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(wxConfig.getPrivateKey().getBytes(StandardCharsets.UTF_8)));
            //定时更新平台证书功能
            // 获取证书管理器实例
            CertificatesManager certificatesManager = CertificatesManager.getInstance();
            // 向证书管理器增加需要自动更新平台证书的商户信息
            certificatesManager.putMerchant(wxConfig.getMchId(), new WechatPay2Credentials(wxConfig.getMchId(),
                    new PrivateKeySigner(wxConfig.getMchSerialNo(), merchantPrivateKey)), wxConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
            // 从证书管理器中获取verifier=>验签器
            return certificatesManager.getVerifier(wxConfig.getMchId());
        } catch (Exception e) {
            log.warn("验证出错");
            throw new ApiException("微信证书出错 联系客服");
        }
    }

    /**
     *回调验证
     * @param request 微信回调请求
     * @param wxConfig 微信基本配置信息
     * @return String
     * @author zhangjunrong
     * @date 2022/4/21 15:02
     */
    public static Notification verifyBack(HttpServletRequest request, ToolWxConfig wxConfig) throws IOException, ValidationException, ParseException {
        //应答报文主体
        BufferedReader br = request.getReader();
        String str;
        StringBuilder builder = new StringBuilder();
        while ((str = br.readLine()) != null) {
            builder.append(str);
        }
        // 构建request，传入必要参数
        //参数 1.微信序列号 2.应答随机串 3.应答时间戳 4.应答签名 5.应答报文主体
        NotificationRequest notificationRequest = new NotificationRequest.Builder()
                .withSerialNumber(request.getHeader(WechatPayHttpHeaders.WECHATPAY_SERIAL))
                .withNonce(request.getHeader(WechatPayHttpHeaders.WECHATPAY_NONCE))
                .withTimestamp(request.getHeader(WechatPayHttpHeaders.WECHATPAY_TIMESTAMP))
                .withSignature(request.getHeader(WechatPayHttpHeaders.WECHATPAY_SIGNATURE))
                .withBody(builder.toString())
                .build();
        NotificationHandler handler = new NotificationHandler(WxPayUtil.getVerifier(wxConfig), wxConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        // 验签和解析请求体
        log.info("验签和解析请求体==============================开始验证==============================");
        Notification notification = handler.parse(notificationRequest);
        return notification;
    }
    /**
     *验证用户支付金额 场景 微信回调验证
     * todo 待具体需求测试
     * @param node 微信回调json 返回参数
     * @param total 数据库记录金额
     * @return Boolean
     * @author zhangjunrong
     * @date 2022/5/16 8:39
     */
    public static Boolean verifyMoney(JsonNode node,Integer total){
        //总金额计数值 用户支付计算
        int userPayTotal = SystemConstant.NUM_ZERO;
        //1.验证订单金额
        //用户支付金额
        int payerTotal = node.get(WXOrderConstant.AMOUNT).get(WXOrderConstant.AMOUNT_PAYER_TOTAL).asInt();
        userPayTotal = userPayTotal + payerTotal;
        //CASH充值型代金券 要加上优惠金额 银行优惠 获取
        //排空 如果没有优惠则跳过
        if (!ObjectUtil.isEmpty(node.get(WXOrderConstant.PROMOTION_DETAIL))) {
            for (JsonNode objNode : node.get(WXOrderConstant.PROMOTION_DETAIL)) {
                //如果优惠类型为CASH 则要和 用户支付金额 累加
                if (WXOrderConstant.WX_DISCOUNT_TYPE.equals(objNode.get(WXOrderConstant.PROMOTION_DETAIL_TYPE).textValue())) {
                    userPayTotal = userPayTotal + objNode.get(WXOrderConstant.AMOUNT).asInt();
                }
            }
        }
        //2.总金额 预支付时的 金额 与 total 用户支付金额
        //微信端返回的支付总金额
        int wxTotal = node.get(WXOrderConstant.AMOUNT).get(WXOrderConstant.AMOUNT_TOTAL).asInt();
        //校验通知的信息是否与商户侧的信息一致，防止数据泄露导致出现“假通知”，造成资金损失。
        log.info("微信回调金额===比较=== "+"微信端返回的支付总金额"+node.get(WXOrderConstant.AMOUNT).get(WXOrderConstant.AMOUNT_TOTAL).asInt()+"================用户支付总金额计算"+userPayTotal);
        return wxTotal == userPayTotal && total == wxTotal;
    }
}
