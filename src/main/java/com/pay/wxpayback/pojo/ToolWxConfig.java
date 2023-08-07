package com.pay.wxpayback.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 微信支付参数表
 * </p>
 *
 * @author 小乌龟
 * @since 2022-11-09
 */
@Data
@TableName("tool_wx_config")
public class ToolWxConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("pk_id")
    private Integer pkId;

    /**
     * 微信开放平台申请APPID
     */
    @TableField("app_id")
    private String appId;
    /**
     * 微信商户号
     */
    @TableField("mch_id")
    private String mchId;

    /**
     * 商户证书序列号
     */
    @TableField("mch_serial_no")
    private String mchSerialNo;

    /**
     * api密钥
     */
    @TableField("api_v3_key")
    private String apiV3Key;

    /**
     * 商户密钥
     */
    @TableField("private_key")
    private String privateKey;


}
