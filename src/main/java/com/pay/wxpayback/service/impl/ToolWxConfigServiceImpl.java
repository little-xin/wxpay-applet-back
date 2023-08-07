package com.pay.wxpayback.service.impl;

import com.pay.wxpayback.pojo.ToolWxConfig;
import com.pay.wxpayback.mapper.ToolWxConfigMapper;
import com.pay.wxpayback.service.ToolWxConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信支付参数表 服务实现类
 * </p>
 *
 * @author 小乌龟
 * @since 2022-11-09
 */
@Service
@CacheConfig(cacheNames = "wxconf")
public class ToolWxConfigServiceImpl extends ServiceImpl<ToolWxConfigMapper, ToolWxConfig> implements ToolWxConfigService {

    @Override
    @Cacheable(key = "'config'")
    public ToolWxConfig findConf() {
        return getById(1L);
    }
}
