package com.pay.wxpayback.exception;

import com.pay.wxpayback.api.IErrorCode;

/**
 * @Description 自定义API异常
 * @Author 小乌龟
 * @Date 2022/11/12 14:49
 */
public class ApiException extends RuntimeException{

    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }


    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
