
<!-- PROJECT LOGO -->
<p align="center">
  
  <img src="https://foruda.gitee.com/images/1691218367806324789/a0dd81fb_8992400.png" alt="Logo" width="80" height="80">

  <h3 align="center">微信支付 V3 SpringBoot版</h3>
</p>

### 演示视频

| 视频方 | 视频地址  |
|---|---|
| B站 | [演示地址](https://www.bilibili.com/video/BV16j411r7HN)  |

  <h3>上手指南</h3>

#### 微信支付参数获取
 ```sh
 appId: 微信开放平台申请APPID
 mch_id: 微信商户号
 mch_serial_no: 商户证书序列号
 api_v3_key: api密钥
 private_key: 商户密钥
    入库格式: -----BEGIN PRIVATE KEY-----
             xxxx
             -----END PRIVATE KEY-----
上述参数 获取 可按此文档  https://pay.weixin.qq.com/wiki/doc/apiv3/open/devpreparation/chapter1_1_1.shtml
 openId: 用户在直连商户appid下的唯一标识 获取方式->微信登录
```

#### 代码获取

```sh
git clone https://gitee.com/little-turtle-z/wxpay-applet-back.git
```
#### 源代码获取  需配置点
1. config->mybatisPlus->CodeGenerator 数据库连接 需配置
2. config->mybatisPlus->MybatisPlusConfig 扫描包 需配置
3. constant->SystemConstant  小程序支付回调接口 需配置
4. application.yml 数据库连接 需配置

 
### 源码分享地址

| 前后端 | 项目地址  |
|---|---|
| 前端 | [代码地址](https://github.com/little-xin/wxpay-applet-front)  |
|  后端 | [代码地址](https://github.com/little-xin/wxpay-applet-back)  |


### 支付流程简要
  ![流程简要](https://foruda.gitee.com/images/1691220893690753219/7c6072a2_8992400.jpeg)

### 目录结构

```sh 
|--src
|  |--main
|  |  |--api 返回前端结果集统一处理
|  |  |--config
|  |  |  |--mybatisPlus 
|  |  |--constant 
|  |  |--exception 
|  |  |--controller
|  |  |--service
|  |  |--pojo
|  |  |--utils
|  |  |  |--WxPayUtil 微信支付工具包 
|  |--resources
|  |  |--application.yml
|--wxpay.sql 数据库生成文件
```