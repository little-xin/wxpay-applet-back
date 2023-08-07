package com.pay.wxpayback.service;

import com.pay.wxpayback.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pay.wxpayback.pojo.ToolWxConfig;
import com.pay.wxpayback.pojo.vo.ReCreateOrderVO;
import com.pay.wxpayback.pojo.vo.ToCreateOrderVO;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author 小王八
 * @since 2023-07-28
 */
public interface OrderService extends IService<Order> {

    /**
     * 微信下单
     * @param wxConfig 微信参数
     * @param toCreatOrderVO 订单数据
     * @return
     */
    ReCreateOrderVO createOrder(ToolWxConfig wxConfig, ToCreateOrderVO toCreatOrderVO);

    /**
     *微信支付回调验证判定 核对成功 数据异步入库
     * @param
     * @param decryptOrder
     * @return Boolean
     * @author zhangjunrong
     * @date 2022/5/3 8:23
     */
    Boolean verifyCreateOrder(String decryptOrder);

    /**
     * 关闭订单
     * 使用场景:
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * 注意:关单没有时间限制，建议在订单生成后间隔几分钟（最短5分钟）再调用关单接口，避免出现订单状态同步不及时导致关单失败
     *
     * @param wxConfig
     * @param outTradeNo
     * @return String
     * @author zhangjunrong
     * @date 2022/4/14 17:02
     */
    void closeOrder(ToolWxConfig wxConfig, String outTradeNo);


    /**
     * 微信退款
     * 注意:1、交易时间超过一年的订单无法提交退款
     * 2、微信支付退款支持单笔交易分多次退款（不超50次），多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号
     * 3、错误或无效请求频率限制：6qps，即每秒钟异常或错误的退款申请请求不超过6次
     * 4、每个支付订单的部分退款次数不能超过50次
     * 5、如果同一个用户有多笔退款，建议分不同批次进行退款，避免并发退款导致退款失败
     * 6、申请退款接口的返回仅代表业务的受理情况，具体退款是否成功，需要通过退款查询接口获取结果
     * 7、一个月之前的订单申请退款频率限制为：5000/min
     * 8、同一笔订单多次退款的请求需相隔1分钟
     * @param wxConfig 微信商家配置
     * @param order 订单信息
     * @return String
     * @author zhangjunrong
     * @date 2022/4/21 15:11
     */
    String refundOrder(ToolWxConfig wxConfig, Order order);

    /**
     *微信退款回调 修改订单状态
     * @param decryptOrder
     * @return Boolean
     * @author zhangjunrong
     * @date 2022/5/20 8:30
     */
    Boolean updateRefundOrder(String decryptOrder);

    /**
     * 查询订单
     * 使用场景:
     * 1.当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知。
     * 2.调用支付接口后，返回系统错误或未知交易状态情况。
     * 3.调用付款码支付API，返回USERPAYING(用户付费)的状态。
     * 4.调用关单或撤销接口API之前，需确认支付状态。
     *
     * @param wxConfig   微信配置
     * @param outTradeNo 商户订单号 系统生成
     * @return String 订单交易状态
     * @author zhangjunrong
     * @date 2022/4/14 16:14
     *
     */
    String queryCreateOrder(ToolWxConfig wxConfig, String outTradeNo);


    /**
     *微信退款查询
     *注意:提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，建议在提交退款申请后1分钟发起查询退款状态，一般来说零钱支付的退款5分钟内到账，银行卡支付的退款1-3个工作日到账。
     * @param wxConfig
     * @param refundNo 退款订单号 系统生成
     * @return String
     * @author zhangjunrong
     * @date 2022/4/21 15:15
     */
    String queryRefundOrder(ToolWxConfig wxConfig, String refundNo);



}
