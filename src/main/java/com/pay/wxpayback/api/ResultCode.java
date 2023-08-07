package com.pay.wxpayback.api;

/**
 * @Description 常用API返回对象
 * @Author 小乌龟
 * @Date 2022/11/12 14:57
 */
public enum ResultCode implements IErrorCode{
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),

    PAY_FAILED(412, "参数检验失败");
    private int code;
    private String message;

    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
