package com.pay.wxpayback.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Description 前端=>微信创建订单
 * @Author 小乌龟
 * @Date 2022/4/13 19:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToCreateOrderVO {

    /**
     * 订单号
     */
    String outTradeNo;

    /**
     * 金额
     */
    @NotNull
    Integer total;

    /**
     * 订单描述
     */
    String description;

    /**
     * 用户openId
     */
    String openId;
}
