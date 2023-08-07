package com.pay.wxpayback.Enum;

/**
  *微信支付 api调用接口
  * @author zhangjunrong
  * @date 2022/5/9 19:57
  */
public enum WxApiType {
    /** 小程序 下单API */
    CREATE_ORDER("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi"),

    /** 申请退款API 通过 outTradeNo 商户订单号 退单*/
    REFUND_ORDER("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds"),

    /** 查询支付订单API ONE{}=>outTradeNo – 商户订单号 系统生 TOW{}=>mchId商户的商户号，由微信支付生成并下发。 */
    QUERY_CREATE_ORDER("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{}?mchid={}"),

    /** 查询退款订单API {}=>outRefundNo 商户退款单号*/
    QUERY_REFUND_ORDER("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/{}"),

    /** 关闭订单API {}=>outTradeNo 商户订单号*/
    CLOSE_ORDER("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{}/close");

    private final String value;

    WxApiType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
