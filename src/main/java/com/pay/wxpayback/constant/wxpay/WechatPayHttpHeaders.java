package com.pay.wxpayback.constant.wxpay;

/**
 *微信支付HTTP请求头相关常量
 * @author zhangjunrong
 * @return
 */
public final class WechatPayHttpHeaders {
    //请求头配置
    public static final String ACCEPT = "Accept";

    public static final String CONTENT_TYPE = "Content-type";

    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_JSON_UTF = "application/json; charset=utf-8";
    /**
      *微信回调参数==>微信序列号
      */
    public static final String WECHATPAY_SERIAL = "Wechatpay-Serial";
    /**
     *微信回调参数==>应答随机串
     */
    public static final String WECHATPAY_NONCE="Wechatpay-Nonce";
    /**
     *微信回调参数==>应答时间戳
     */
    public static final String WECHATPAY_TIMESTAMP="Wechatpay-Timestamp";
    /**
     *微信回调参数==>应答签名
     */
    public static final String WECHATPAY_SIGNATURE="Wechatpay-Signature";

    private WechatPayHttpHeaders() {
        // Don't allow instantiation
    }

}
