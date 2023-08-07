/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.37-log : Database - wxpay
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wxpay` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `wxpay`;

/*Table structure for table `t_order` */

CREATE TABLE `t_order` (
  `pk_id` bigint(19) NOT NULL COMMENT '本系统生成的订单',
  `amount` int(11) NOT NULL COMMENT '商品实付金额(单位:分)',
  `gmt_create` datetime DEFAULT NULL COMMENT '下单时间',
  `pay_status` tinyint(4) DEFAULT NULL COMMENT '支付状态:0 未支付 1 已支付 2 退款中 3 已退款 4 退款失败',
  `pay_description` varchar(64) DEFAULT NULL COMMENT '订单描述',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

/*Table structure for table `tool_wx_config` */

CREATE TABLE `tool_wx_config` (
  `pk_id` tinyint(4) NOT NULL,
  `app_id` varchar(100) DEFAULT NULL COMMENT '微信开放平台申请APPID',
  `mch_id` varchar(100) DEFAULT NULL COMMENT '微信商户号',
  `mch_serial_no` varchar(64) DEFAULT NULL COMMENT '商户证书序列号',
  `api_v3_key` varchar(32) DEFAULT NULL COMMENT 'api密钥',
  `private_key` text COMMENT '商户密钥',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信支付参数表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
