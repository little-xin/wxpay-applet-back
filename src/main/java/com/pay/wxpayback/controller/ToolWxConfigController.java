package com.pay.wxpayback.controller;


import com.pay.wxpayback.constant.SystemConstant;
import com.pay.wxpayback.constant.wxpay.WXOrderConstant;
import com.pay.wxpayback.pojo.ToolWxConfig;
import com.pay.wxpayback.service.ToolWxConfigService;
import com.pay.wxpayback.utils.WxPayUtil;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 微信支付参数表 前端控制器
 * </p>
 *
 * @author 小乌龟
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/tool-wx-config")
@Slf4j
public class ToolWxConfigController {

    @Autowired
    private ToolWxConfigService toolWxConfigService;


}
