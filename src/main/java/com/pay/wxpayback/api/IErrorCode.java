package com.pay.wxpayback.api;

/**
 * @Description 常用API返回对象接口
 * @Author 小乌龟
 * @Date 2022/11/12 14:58
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    int getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
