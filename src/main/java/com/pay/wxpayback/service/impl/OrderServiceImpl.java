package com.pay.wxpayback.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pay.wxpayback.Enum.WxApiType;
import com.pay.wxpayback.Enum.WxPayStatusEnum;
import com.pay.wxpayback.constant.SystemConstant;
import com.pay.wxpayback.constant.wxpay.WXOrderConstant;
import com.pay.wxpayback.constant.wxpay.WechatPayHttpHeaders;
import com.pay.wxpayback.exception.ApiException;
import com.pay.wxpayback.pojo.Order;
import com.pay.wxpayback.mapper.OrderMapper;
import com.pay.wxpayback.pojo.ToolWxConfig;
import com.pay.wxpayback.pojo.vo.ReCreateOrderVO;
import com.pay.wxpayback.pojo.vo.ToCreateOrderVO;
import com.pay.wxpayback.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.wxpayback.utils.WxPayUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 小王八
 * @since 2023-07-28
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    private final ReentrantLock reentrantLock1 = new ReentrantLock();

    private final ReentrantLock reentrantLock2 = new ReentrantLock();

    /**
     * 调用微信支付 get请求 统一配置
     *要微信签名认证
     * @param url
     * @return String
     * @author zhangjunrong
     * @date 2022/5/9 20:39
     */
    private String getHttpGet(ToolWxConfig wxConfig, String url) throws URISyntaxException, IOException {
        //1.构造httpGet请求
        URIBuilder uriBuilder = null;
        uriBuilder = new URIBuilder(url);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader(WechatPayHttpHeaders.ACCEPT, WechatPayHttpHeaders.APPLICATION_JSON);
        //2.调起微信查询订单接口
        CloseableHttpResponse response = WxPayUtil.getHttpClient(wxConfig, WxPayUtil.getVerifier(wxConfig)).execute(httpGet);
        //3.返回结果信息
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public ReCreateOrderVO createOrder(ToolWxConfig wxConfig, ToCreateOrderVO toCreatOrderVO) {
        try {
            //1.请求配置参数
            HttpPost httpPost = new HttpPost(WxApiType.CREATE_ORDER.getValue());
            //格式配置
            httpPost.addHeader(WechatPayHttpHeaders.ACCEPT, WechatPayHttpHeaders.APPLICATION_JSON);
            httpPost.addHeader(WechatPayHttpHeaders.CONTENT_TYPE, WechatPayHttpHeaders.APPLICATION_JSON_UTF);
            //2.读取privateKey私钥
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(wxConfig.getPrivateKey().getBytes(StandardCharsets.UTF_8)));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            //3.配置下单参数
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put(WXOrderConstant.MCHID, wxConfig.getMchId())
                    .put(WXOrderConstant.APPID, wxConfig.getAppId())
                    .put(WXOrderConstant.DESCRIPTION, toCreatOrderVO.getDescription())
                    .put(WXOrderConstant.NOTIFY_URL, SystemConstant.PAY_NOTIFY_URL)
                    .put(WXOrderConstant.OUT_TRADE_NO, toCreatOrderVO.getOutTradeNo());
            rootNode.putObject(WXOrderConstant.AMOUNT)
                    //total 微信需要int类型 为了不丢失精度 单位为分
                    .put(WXOrderConstant.AMOUNT_TOTAL, toCreatOrderVO.getTotal());
            // 用户在直连商户appid下的唯一标识。 下单前需获取到用户的Openid
            rootNode.putObject(WXOrderConstant.PAYER)
                    .put(WXOrderConstant.OPENID, toCreatOrderVO.getOpenId());

            objectMapper.writeValue(bos, rootNode);
            //4.调用微信下单接口
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            //接口返回值 申请获取prepay_id
            CloseableHttpResponse response = WxPayUtil.getHttpClient(wxConfig, WxPayUtil.getVerifier(wxConfig)).execute(httpPost);
            //预支付交易会话标识 prepay_id
            String bodyAsString = EntityUtils.toString(response.getEntity());
            //5.APP调起支付API 构造签名串
            String timestamp = DateUtil.currentSeconds() + "";
            String nonce = RandomUtil.randomString(SystemConstant.NUM_32);
            StringBuilder builder = new StringBuilder();
            //应用id
            builder.append(wxConfig.getAppId()).append("\n");
            //时间戳
            builder.append(timestamp).append("\n");
            //随机字符串
            builder.append(nonce).append("\n");
            //预支付交易会话标识 prepay_id  通过bodyAsString获取 prepay_id
            JsonNode node = objectMapper.readTree(bodyAsString);
            // prepay_id=wx201410272009395522657a690389285100  必须以此格式
            String wxPackage = SystemConstant.WX_PACKAGE + node.get(WXOrderConstant.PREPAY_ID).textValue();
            builder.append(wxPackage).append("\n");

            log.info("微信签名请求体: {}", builder.toString());

            //6.计算签名
            String sign = WxPayUtil.sign(builder.toString().getBytes(StandardCharsets.UTF_8), merchantPrivateKey);

            //7.返回参数 让前端调微信支付
            return new ReCreateOrderVO(timestamp, nonce, wxPackage, "RSA", sign,toCreatOrderVO.getOutTradeNo());
        } catch (Exception e) {
            log.error(toCreatOrderVO.getOutTradeNo() + "订单下单失败");
            log.error(e.toString());
            //下单失败抛异常
            throw new ApiException("下单失败 重新尝试付款");
        }
    }

    Integer queryAmountByOrderNo(String orderNo) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.select("amount").eq("pk_id",orderNo);
        return getOne(wrapper).getAmount();
    }

    void updateStatus(Integer status,String pkId) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.set("pay_status",status).eq("pk_id",pkId);
        update(wrapper);
    }

    Integer getStatus(String pkId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.select("pay_status").eq("pk_id",pkId);
        return getOne(wrapper).getPayStatus();
    }

    @Override
    public Boolean verifyCreateOrder(String decryptOrder) {
        //在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
        //实现: 加入一把可重入锁
        if (reentrantLock1.tryLock()) {
            try {
                log.info("===================================进入微信支付回调核对订单中========================================");
                ObjectMapper objectMapper = new ObjectMapper();
                //微信回调 解密后 信息
                JsonNode node = objectMapper.readTree(decryptOrder);
                //获取订单商户号
                String orderNo = node.get(WXOrderConstant.OUT_TRADE_NO).textValue();

                log.info(node.get(WXOrderConstant.OUT_TRADE_NO) + "订单回调信息记录:订单状态:" + orderNo);
                //2.如果回调 支付类型为成功 核对金额 入数据库
                //获取支付状态
                String tradeState = node.get(WXOrderConstant.TRADE_STATE).textValue();
                if (StrUtil.equals(WXOrderConstant.WX_BACK_OK, tradeState)) {
                    //1.查询数据库 对比支付金额
                    if(WxPayUtil.verifyMoney(node, queryAmountByOrderNo(orderNo))) {
                        //2.对比成功 账单状态 已支付
                        updateStatus(SystemConstant.NUM_ONE,orderNo);
                    }
                    //支付成功 就返回true
                    return true;
                }

            } catch (Exception e) {
                log.error("订单支付异常===>订单回调信息记录:订单状态:" + decryptOrder);
            }finally {
                //释放锁
                reentrantLock1.unlock();
            }
        }
        return false;
    }

    @Override
    public void closeOrder(ToolWxConfig wxConfig, String outTradeNo) {
        try {
            //1.微信接口编辑
            String url = StrFormatter.format(WxApiType.CLOSE_ORDER.getValue(), outTradeNo);
            HttpPost httpPost = new HttpPost(url);
            //格式配置
            httpPost.addHeader(WechatPayHttpHeaders.ACCEPT, WechatPayHttpHeaders.APPLICATION_JSON);
            httpPost.addHeader(WechatPayHttpHeaders.CONTENT_TYPE, WechatPayHttpHeaders.APPLICATION_JSON_UTF);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //2.添加商户id
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put(WXOrderConstant.MCHID, wxConfig.getMchId());
            objectMapper.writeValue(bos, rootNode);
            //3.调起微信关单接口
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = WxPayUtil.getHttpClient(wxConfig, WxPayUtil.getVerifier(wxConfig)).execute(httpPost);
            log.info("=====关单结果======={}====",response);
        } catch (Exception e) {
            log.error("关单失败" + outTradeNo + e);
        }
    }

    @Override
    public String refundOrder(ToolWxConfig wxConfig,Order order) {
        try {
            //1.请求配置参数
                HttpPost httpPost = new HttpPost(WxApiType.REFUND_ORDER.getValue());
                //格式配置
                httpPost.addHeader(WechatPayHttpHeaders.ACCEPT, WechatPayHttpHeaders.APPLICATION_JSON);
                httpPost.addHeader(WechatPayHttpHeaders.CONTENT_TYPE, WechatPayHttpHeaders.APPLICATION_JSON_UTF);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode rootNode = objectMapper.createObjectNode();
                //2.配置参数 订单商户号 退款订单号 微信订单号(微信生成) 退款回调地址(与下单回调地址不一样) 金额信息 amount: 原订单金额 total 退款金额 refund (单位都是分) 退款币种 CNY 人民币
                //商户订单号 下单时生成
                rootNode.put(WXOrderConstant.OUT_TRADE_NO, order.getPkId().toString())
                        //微信支付系统生成的订单号 下单时生成(系统生成)
                        .put(WXOrderConstant.OUT_REFUND_NO, order.getPkId().toString())
                        //退款回调地址
                        .put(WXOrderConstant.NOTIFY_URL, SystemConstant.REFUND_NOTIFY_URL);
                //金额信息 amount: 原订单金额 total 退款金额 refund (单位都是分)
                rootNode.putObject(WXOrderConstant.AMOUNT)
                        //现阶段 total==refund 不支持部分退款
                        //原订单金额 total
                        .put(WXOrderConstant.AMOUNT_TOTAL, order.getAmount())
                        //退款金额 refund
                        .put(WXOrderConstant.AMOUNT_REFUND, order.getAmount())
                        //退款币种 CNY 人民币
                        .put(WXOrderConstant.AMOUNT_CURRENCY, SystemConstant.CURRENCY_RMB);
                objectMapper.writeValue(bos, rootNode);
                //3.调用微信退款接口
                httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
                //接口返回值
                CloseableHttpResponse response = WxPayUtil.getHttpClient(wxConfig, WxPayUtil.getVerifier(wxConfig)).execute(httpPost);
                String bodyAsString = EntityUtils.toString(response.getEntity());
                log.info("微信申请退款返回结果" + "response:" + bodyAsString);
                JsonNode refundNode = objectMapper.readTree(bodyAsString);
                // 修改订单退款状态 微信退款入数据库
              String status = refundNode.get(WXOrderConstant.STATUS).textValue();
              log.info("微信方=====退款状态====={}======",status);
              Integer statusCode = WxPayStatusEnum.getCode(status);
              updateStatus(statusCode,order.getPkId().toString());
                return "退款申请成功 注意退款查收";
        } catch (Exception e) {
            log.info("微信退款" +order.getPkId() + "失败");
            throw new ApiException("退款失败,请联系客服解决");
        }
    }


    @Override
    public Boolean updateRefundOrder(String decryptOrder) {
        //在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
        //实现: 加入一把可重入锁
        if (reentrantLock2.tryLock()) {
            try {
                if (ObjectUtil.isNotEmpty(decryptOrder)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    //string=>json 方便获取数据
                    JsonNode jsonNode = objectMapper.readTree(decryptOrder);
                    log.info("微信退款回调参数===================={}====================", decryptOrder);
                    //获取退款单编号(系统生成)
                    String outRefundNo = jsonNode.get(WXOrderConstant.OUT_REFUND_NO).textValue();
                    String refundStatus = jsonNode.get(WXOrderConstant.REFUND_STATUS).textValue();
                    //数据库核对 如果退款状态已退款说明已处理 直接返回即可
                    if (getStatus(outRefundNo) == SystemConstant.NUM_THREE) {
                        return true;
                    }
                    Integer statusCode = WxPayStatusEnum.getCode(refundStatus);
                    log.info("微信退款回调 退款订单编号(系统生成)============{}===========订单状态======{}", outRefundNo, refundStatus);
                    //1.退款表状态修改 微信退款id(微信生成)  退款 调起退款则给用户退款 不根据本系统回调反馈 所以修改状态不成功 依旧返回true
                      updateStatus(statusCode,outRefundNo);
                    return true;
                }
            } catch (Exception e) {
                log.warn("微信退款回调接口====修改订单状态失败" + e);
            } finally {
                //释放锁
                reentrantLock2.unlock();
            }
        }
        return false;
    }

    @Override
    public String queryCreateOrder(ToolWxConfig wxConfig, String outTradeNo) {
        try {
            //1.查单 微信接口 编辑 微信订单号 + 商户号
            String url = StrFormatter.format(WxApiType.QUERY_CREATE_ORDER.getValue(), outTradeNo, wxConfig.getMchId());
            //2.调用微信接口
            String bodyAsString = getHttpGet(wxConfig, url);
            log.info("支付查单信息" + bodyAsString);
            //返回查单结果信息
            if (ObjectUtil.isNotEmpty(bodyAsString)){
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(bodyAsString);
                return jsonNode.get(WXOrderConstant.TRADE_STATE).textValue();
            }
        } catch (Exception e) {
            log.error("支付查单失败" + outTradeNo);
        }
        return null;
    }

    @Override
    public String queryRefundOrder(ToolWxConfig wxConfig, String refundNo) {
        try {
            //1.查单 微信接口 拼接url
            String url = StrFormatter.format(WxApiType.QUERY_REFUND_ORDER.getValue(), refundNo);
            //2.调用微信接口
            String bodyAsString = getHttpGet(wxConfig, url);
            log.info("退单查询信息============{}========" + bodyAsString);
            //返回查单结果信息
            if (ObjectUtil.isNotEmpty(bodyAsString)){
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(bodyAsString);
                return jsonNode.get(WXOrderConstant.STATUS).textValue();
            }
        } catch (Exception e) {
            log.error("退单查单失败" + refundNo);
        }
        return null;
    }
}
