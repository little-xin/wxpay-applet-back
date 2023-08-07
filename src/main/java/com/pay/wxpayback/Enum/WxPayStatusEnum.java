package com.pay.wxpayback.Enum;

/**
 * @Description 微信支付状态
 * @Author 小乌龟
 * @Date 2022/5/14 10:56
 */
public enum WxPayStatusEnum {
    /** 未支付 */
    NOTPAY(0,"NOTPAY"),

    /** 支付成功 */
    SUCCESS(1,"SUCCESS"),

    /** 转入退款 */
    REFUND(2,"REFUND"),

    /** 已关闭 */
    CLOSED(3,"CLOSED"),
    /**  退款处理中 */
    REF_PROCESSING(2,"PROCESSING"),
    /**  退款成功 */
    REF_SUCCESS(3,"SUCCESS"),
    /** 退款已关闭 */
    REF_CLOSED(4,"CLOSED"),
    /** 退款异常 */
    REF_ABNORMAL(4,"ABNORMAL");




    private final Integer code;
    private final String value;

    WxPayStatusEnum(Integer code,String value) {
        this.code=code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    /**
      *退款状态 转换 String => int
      * @param value 退款String
      * @return Integer
      * @author zhangjunrong
      * @date 2022/5/19 13:47
      */
    public static Integer getCode(String value){
        switch(value){
            case "PROCESSING":
                return REF_PROCESSING.code;
            case "SUCCESS":
                return REF_SUCCESS.code;
            case "CLOSED":
                return REF_CLOSED.code;
            default:
                return REF_ABNORMAL.code;
        }
    }
}
