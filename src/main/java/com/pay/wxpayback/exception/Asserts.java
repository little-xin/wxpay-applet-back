package com.pay.wxpayback.exception;

import com.pay.wxpayback.api.IErrorCode;

/**
 * @Description 断言处理类，用于抛出各种API异常
 * @Author 小乌龟
 * @Date 2022/11/12 14:49
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
