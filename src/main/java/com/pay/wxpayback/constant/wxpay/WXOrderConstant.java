package com.pay.wxpayback.constant.wxpay;

/**
 * @Description 微信支付参数
 * @Author 小乌龟
 * @Date 2022/5/4 8:44
 */
public class WXOrderConstant {

    /**
     *应用ID
     */
    public final static String APPID ="appid";

    /**
     *直连商户号
     */
    public final static String MCHID ="mchid";

    /**
     *用户支付状态
     */
    public final static String TRADE_STATE ="trade_state";

    /**
     *用户退款状态
     */
    public final static String STATUS ="status";


    /**
     *商品描述
     */
    public final static String DESCRIPTION ="description";

    /**
     *通知地址
     */
    public final static String NOTIFY_URL ="notify_url";

    /**
     *预支付交易会话标识
     */
    public final static String PREPAY_ID ="prepay_id";

    /**
     *商户订单号
     */
    public final static String OUT_TRADE_NO ="out_trade_no";

    /**
     *商户退款单号
     */
    public final static String OUT_REFUND_NO ="out_refund_no";

    /**
     *微信支付系统生成的订单号
     */
    public final static String TRANSACTION_ID ="transaction_id";

    /**
     *微信支付系统生成的订单号
     */
    public final static String REFUND_ID ="refund_id";
    /**
     *订单金额
     */
    public final static String AMOUNT ="amount";
    /**
     *总金额	下单时系统计算金额 单位为分
     */
    public final static String AMOUNT_TOTAL ="total";

    /**
     *支付者
     */
    public final static String PAYER ="payer";

    /**
     *用户在直连商户appid下的唯一标识。 下单前需获取到用户的Openid
     */
    public final static String OPENID ="openid";

    /**
     *总金额	退款时 退款金额
     */
    public final static String AMOUNT_REFUND ="refund";

    /**
     *总金额	退款时 币种
     */
    public final static String AMOUNT_CURRENCY ="currency";

    /**
     *用户支付金额
     */
    public final static String AMOUNT_PAYER_TOTAL ="payer_total";
    /**
     *优惠功能
     */
    public final static String PROMOTION_DETAIL ="promotion_detail";
    /**
     *优惠类型
     */
    public final static String PROMOTION_DETAIL_TYPE ="type";
    /**
     * 微信优惠 CASH：充值型代金券
     */
    public final static String WX_DISCOUNT_TYPE="CASH";

    /**
     *下单回调给微信支付成功信息
     */
    public final static String WX_BACK_OK="SUCCESS";

    /**
     *退款回调 退款状态
     */
    public final static String REFUND_STATUS="refund_status";


    /**
     *微信回调 通知数据
     */
    public final static String RESOURCE="resource";

    /**
     *微信账单类型 交易账单
     */
    public final static String TRADE_BILL="tradebill";
    /**
     *微信账单类型 资金账单
     */
    public final static String FUND_FLOW_BILL="fundflowbill";
    /**
     *微信账单下载地址
     */
    public final static String DOWNLOAD_URL="download_url";

    /**
     *微信回调 通知数据=>数据密文
     */
    public final static String RESOURCE_CIPHERTEXT="ciphertext";

    /**
     *微信回调 通知数据=>附加数据
     */
    public final static String RESOURCE_ASSOCIATED_DATA="associated_data";

    /**
     *微信回调 通知数据=>随机串
     */
    public final static String RESOURCE_NONCE="nonce";




}
