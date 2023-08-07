package com.pay.wxpayback.service;

import com.pay.wxpayback.pojo.ToolWxConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 微信支付参数表 服务类
 * </p>
 *
 * @author 小乌龟
 * @since 2022-11-09
 */
public interface ToolWxConfigService extends IService<ToolWxConfig> {

    /**
      *查询微信支付参数
      * @param
      * @return ToolWxConfig
      * @author zhangjunrong
      * @date 2022/11/10 10:33
      */
    ToolWxConfig findConf();
}
