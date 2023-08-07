package com.pay.wxpayback.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author 小王八
 * @since 2023-07-28
 */
@Data
@Accessors(chain = true)
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本系统生成的订单
     */
    @TableId("pk_id")
    private Long pkId;

    /**
     * 商品实付金额(单位:分)
     */
    @TableField("amount")
    private Integer amount;

    /**
     * 下单时间
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 支付状态:0 未支付 1 已支付 2 退款中 3 已退款 4 退款失败
     */
    @TableField("pay_status")
    private Integer payStatus;

    /**
     * 订单描述
     */
    @TableField("pay_description")
    private String payDescription;


}
