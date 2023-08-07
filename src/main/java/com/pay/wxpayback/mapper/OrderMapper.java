package com.pay.wxpayback.mapper;

import com.pay.wxpayback.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author 小王八
 * @since 2023-07-28
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
