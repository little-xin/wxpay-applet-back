package com.pay.wxpayback.constant;

/**
 * @Description 系统常量
 * @Author 小乌龟
 * @Date 2022/11/10 19:53
 */
public class SystemConstant {

    /**
     * 微信支付
     */
    /**
     * 支付回调url
     */
    public final static String PAY_NOTIFY_URL="https://域名配置/pay/order/wechatPayCallback";

    public final static String REFUND_NOTIFY_URL="https://域名配置/pay/order/wechatRefundBack";
    /**
     * 固定支付参数 package
     */
    public final static String WX_PACKAGE="prepay_id=";

    public final static String PAY_SUCCESS="支付成功";

    /**
     * 币种 RMB
     */
    public final static String CURRENCY_RMB="CNY";
    /**
     * 数字 0~11
     */
    public final static int NUM_NEGATIVE_ONE = -1;
    public final static int NUM_ZERO = 0;
    public final static int NUM_ONE = 1;
    public final static int NUM_TWO = 2;
    public final static int NUM_THREE = 3;
    public final static int NUM_FOUR = 4;
    public final static int NUM_FIVE = 5;
    public final static int NUM_SIX = 6;
    public final static int NUM_SEVEN = 7;
    public final static int NUM_EIGHT = 8;
    public final static int NUM_NINE = 9;
    public final static int NUM_TEN = 10;
    public final static int NUM_ELEVEN = 11;
    public final static int NUM_15 = 15;
    public final static int NUM_16 = 16;
    public final static int NUM_19 = 19;
    public final static int NUM_TWENTY = 20;
    public final static int NUM_30 = 30;
    public final static int NUM_32 = 32;
    public final static int NUM_50 = 50;
    public final static int NUM_60 = 60;
    public final static int NUM_64 = 64;
    public final static int NUM_90 = 90;
    public final static int NUM_100 = 100;
    public final static int NUM_200 = 200;
    public final static int NUM_1024 = 1024;
    public final static int NUM_5000 = 5000;
    public final static int NUM_24_time =86400; //60*60*60*24 一天
}
