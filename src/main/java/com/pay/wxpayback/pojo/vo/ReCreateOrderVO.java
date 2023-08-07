package com.pay.wxpayback.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 微信创建订单
 * @author 小乌龟
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReCreateOrderVO {
    //微信小程序需要参数
//    "appId": "wx499********7c70e",  // 微信开放平台 - 应用 - AppId，注意和微信小程序、公众号 AppId 可能不一致
//    "timeStamp": 1597935292,        // 时间戳（单位：秒）
//    "nonceStr": "c5sEwbaNPiXAF3iv", // 随机字符串
//    "package": "wx202254********************fbe90000", // 小程序下单接口返回的prepay_id参数值，提交格式如：prepay_id=***;示例值：prepay_id=wx201410272009395522657a690389285100
//    "signType": "RSA"    签名类型，默认为RSA，仅支持RSA。
//    "paySign": "A842B45937F6EFF60DEC7A2EAA52D5A0" // 签名，这里用的 MD5/RSA 签名


    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 订单详情扩展字符串 package
     */
    private String wxPackage;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;

    /**
     * 订单号
     */
    private String outTradeNo;

}
