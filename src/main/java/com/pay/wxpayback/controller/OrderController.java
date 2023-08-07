package com.pay.wxpayback.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.pay.wxpayback.api.CommonController;
import com.pay.wxpayback.api.CommonResult;
import com.pay.wxpayback.constant.SystemConstant;
import com.pay.wxpayback.constant.wxpay.WXOrderConstant;
import com.pay.wxpayback.pojo.Order;
import com.pay.wxpayback.pojo.ToolWxConfig;
import com.pay.wxpayback.pojo.vo.ReCreateOrderVO;
import com.pay.wxpayback.pojo.vo.ToCreateOrderVO;
import com.pay.wxpayback.service.OrderService;
import com.pay.wxpayback.service.ToolWxConfigService;
import com.pay.wxpayback.utils.WxPayUtil;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author 小王八
 * @since 2023-07-28
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController extends CommonController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ToolWxConfigService toolWxConfigService;

    @PostMapping("/createOrder")
    public CommonResult<ReCreateOrderVO> createOrder(@RequestBody @Valid ToCreateOrderVO toCreatOrderVO){
        Long id = IdUtil.getSnowflake().nextId();
        toCreatOrderVO.setOutTradeNo(id.toString());
        //订单入库
        orderService.save(new Order().setAmount(toCreatOrderVO.getTotal())
                .setPayDescription(toCreatOrderVO.getDescription())
                 .setPkId(id)
                 .setPayStatus(SystemConstant.NUM_ZERO));
        return process(() -> orderService.createOrder(toolWxConfigService.findConf(),toCreatOrderVO));
    }

    @GetMapping("/refundOrder")
    public CommonResult<String> refundOrder(@RequestParam("outTradeNo") String outTradeNo){
        return process(() -> orderService.refundOrder(toolWxConfigService.findConf(), orderService.getById(outTradeNo)));
    }

    /**
     * 支付回调给微信确认
     * @param request
     * @return
     */
    @PostMapping("/wechatPayCallback")
    public String wechatCallback(HttpServletRequest request) {
        ToolWxConfig wxConfig = toolWxConfigService.findConf();
        log.info("微信退款回调通知调用=============================");
        Map<String,String> result = new HashMap(SystemConstant.NUM_16);
        result.put("code", "FAIL");
        result.put("message","失败");
        try {
            //微信回调信息校验
            // 构建request，传入必要参数
            Notification notification = WxPayUtil.verifyBack(request, wxConfig);
            log.info("=================微信验证签名成功=======成功时间=={}=====",notification.getCreateTime());
            notification.getSummary();
            if(SystemConstant.PAY_SUCCESS.equals(notification.getSummary())){
                if (orderService.verifyCreateOrder(notification.getDecryptData())) {
                    log.info("==============================微信退款成功订单=====================================");
                    result.put("code", WXOrderConstant.WX_BACK_OK);
                    result.put("message", "支付回调成功");
                }
            }
        } catch (ValidationException | ParseException | IOException e) {
            log.error("微信支付回调失败验证" + e);
        }
        log.info("微信返回结果"+result);
        return JSONUtil.toJsonStr(result);
    }

    @GetMapping("/closeOrder")
    public void closeOrder(@RequestParam("outTradeNo") String outTradeNo) {
        orderService.closeOrder(toolWxConfigService.findConf(),outTradeNo);
    }

    @PostMapping("/wechatRefundBack")
    public String wechatRefundBack(HttpServletRequest request) {
        ToolWxConfig wxConfig = toolWxConfigService.findConf();
        Map result = new HashMap();
        result.put("code", "FAIL");
        result.put("message","失败");
        try {
            //微信回调信息校验
            // 构建request，传入必要参数
            Notification notification = WxPayUtil.verifyBack(request, wxConfig);
            log.info("=================微信验证签名成功=======成功时间=={}=====", notification.getCreateTime());
            // 根据退款返回结果 更新订单状态 返回结果
            if(orderService.updateRefundOrder(notification.getDecryptData())){
                log.info("==============================微信退款成功订单=====================================");
                result.put("code", WXOrderConstant.WX_BACK_OK);
                result.put("message", "退款回调成功");
            }
        } catch (ValidationException | ParseException | IOException e) {
            log.error("微信退款回调失败" + e);
        }

        return JSONUtil.toJsonStr(result);
    }

    @GetMapping("/toQueryOrderState")
    public CommonResult<String> toQueryOrderState(@RequestParam("outTradeNo") String outTradeNo) {
        return process(() -> orderService.queryCreateOrder(toolWxConfigService.findConf(), outTradeNo));
    }

    @GetMapping("/toQueryRefundState")
    public CommonResult<String> toQueryRefundState(@RequestParam("refundNo") String refundNo) {
        return process(() -> orderService.queryRefundOrder(toolWxConfigService.findConf(), refundNo));
    }
}
