package com.pay.wxpayback.api;

/**
 * controller层 函数编程
 * @author 小乌龟
 */
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
