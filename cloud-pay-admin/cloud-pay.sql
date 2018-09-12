/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : cloud-pay

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-08-26 18:22:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_login
-- ----------------------------

-- ----------------------------
-- Table structure for t_amount_limit
-- ----------------------------
DROP TABLE IF EXISTS `t_amount_limit`;
CREATE TABLE `t_amount_limit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` int(1) DEFAULT NULL COMMENT '限制类型',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户id',
  `period` int(1) NULL COMMENT '统计周期',
  `amount_limit` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='限额配置信息';

-- ----------------------------
-- Records of t_amount_limit
-- ----------------------------

-- ----------------------------
-- Table structure for t_bank
-- ----------------------------
DROP TABLE IF EXISTS `t_bank`;
CREATE TABLE `t_bank` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bank_code` varchar(255) DEFAULT NULL COMMENT '联行号',
  `bank_name` varchar(255) DEFAULT NULL COMMENT '银行名称',
  `modifer` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL  COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联行号信息';

-- ----------------------------
-- Records of t_bank
-- ----------------------------

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_name` varchar(100) DEFAULT NULL COMMENT '渠道名称',
  `channel_merchant_id` varchar(50) DEFAULT NULL COMMENT '渠道商户号',
  `channel_type` int(1) DEFAULT NULL COMMENT '渠道类型',
  `fee_type` int(1) DEFAULT NULL COMMENT '费率类型',
  `fee` decimal(10,2) DEFAULT NULL COMMENT '费率',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifer` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渠道信息';

-- ----------------------------
-- Records of t_channel
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_applu_fee_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_applu_fee_conf`;
CREATE TABLE `t_merchant_applu_fee_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(3) DEFAULT NULL COMMENT '版本号',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户ID',
  `pay_fee_type` int(1) DEFAULT NULL COMMENT '代付费率类型（1按比例，2按笔）',
  `pay_fee` decimal(10,2) DEFAULT NULL COMMENT '代付费率',
  `loan_fee_type` int(1) DEFAULT NULL COMMENT '垫资费率类型（1按比例，2按笔）',
  `loan_fee` decimal(10,2) DEFAULT NULL COMMENT '垫资费率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户/机构申请费率配置';

-- ----------------------------
-- Records of t_merchant_applu_fee_conf
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_apply_attachement_info
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_apply_attachement_info`;
CREATE TABLE `t_merchant_apply_attachement_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(3) DEFAULT NULL COMMENT '版本号',
  `merchant_id` int(11) DEFAULT NULL,
  `attachement_type` int(1) DEFAULT NULL COMMENT '附件类型(1营业执照，2银行卡，3身份证，4协议)',
  `attachement_path` varchar(255) DEFAULT NULL COMMENT '附件路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户申请附件信息';

-- ----------------------------
-- Records of t_merchant_apply_attachement_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_apply_bank_info
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_apply_bank_info`;
CREATE TABLE `t_merchant_apply_bank_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(3) DEFAULT NULL COMMENT '版本号',
  `merchant_id` int(11) DEFAULT NULL COMMENT '所属商户ID',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行名称',
  `bank_id` int(11) DEFAULT NULL COMMENT '联行号ID',
  `bank_card_no` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `bank_account_type` int(1) DEFAULT NULL COMMENT '账户类型（1企业，2个人）',
  `bank_account_name` varchar(100) DEFAULT NULL COMMENT '户名',
  `cert_type` int(1) DEFAULT NULL COMMENT '证件类型（1身份证）',
  `cert_no` varchar(30) DEFAULT NULL COMMENT '证件号码',
  `mobile_no` varchar(12) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户申请银行卡信息';

-- ----------------------------
-- Records of t_merchant_apply_bank_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_apply_base_info
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_apply_base_info`;
CREATE TABLE `t_merchant_apply_base_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `version` int(3) DEFAULT NULL COMMENT '版本号',
  `org_id` int(11) DEFAULT NULL COMMENT '所属机构ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(255) DEFAULT NULL COMMENT '简称',
  `type` int(1) DEFAULT NULL COMMENT '类型(1代理商，2第三方支付，3垫资商，4企业商户，5个人商户)',
  `industry_category` varchar(255) DEFAULT NULL COMMENT '行业类别',
  `legal` varchar(100) DEFAULT NULL COMMENT '法人',
  `city` varchar(100) NULL COMMENT '所属城市',
  `address` varchar(255) NULL COMMENT '详细地址',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `status` int(1) DEFAULT NULL COMMENT '申请状态(1待审核，2审核通过，3审核不通过)',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL  COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL  COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户申请基本信息';

-- ----------------------------
-- Records of t_merchant_apply_base_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_attachement_info
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_attachement_info`;
CREATE TABLE `t_merchant_attachement_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `attachement_type` int(1) DEFAULT NULL COMMENT '附件类型(1营业执照，2银行卡，3身份证，4协议)',
  `attachement_path` varchar(255) DEFAULT NULL COMMENT '附件路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户附件信息';

-- ----------------------------
-- Records of t_merchant_attachement_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_bank_info
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_bank_info`;
CREATE TABLE `t_merchant_bank_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL COMMENT '所属商户ID',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行名称',
  `bank_id` int(11) DEFAULT NULL COMMENT '联行号ID',
  `bank_card_no` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `bank_account_type` int(1) DEFAULT NULL COMMENT '账户类型（1企业，2个人）',
  `bank_account_name` varchar(100) DEFAULT NULL COMMENT '户名',
  `cert_type` int(1) DEFAULT NULL COMMENT '证件类型（1身份证）',
  `cert_no` varchar(30) DEFAULT NULL COMMENT '证件号码',
  `mobile_no` varchar(12) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户银行卡信息';

-- ----------------------------
-- Records of t_merchant_bank_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_base_info
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_base_info`;
CREATE TABLE `t_merchant_base_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `org_id` int(11) DEFAULT NULL COMMENT '所属机构ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(255) DEFAULT NULL COMMENT '简称',
  `type` int(1) DEFAULT NULL COMMENT '类型(1代理商，2第三方支付，3垫资商，4企业商户，5个人商户)',
  `industry_category` varchar(255) DEFAULT NULL COMMENT '行业类别',
  `legal` varchar(100) DEFAULT NULL COMMENT '法人',
  `city` varchar(100) NULL COMMENT '所属城市',
  `address` varchar(255) NULL COMMENT '详细地址',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL  COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL  COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户基本信息';

-- ----------------------------
-- Records of t_merchant_base_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_fee_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_fee_conf`;
CREATE TABLE `t_merchant_fee_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户ID',
  `pay_fee_type` int(1) DEFAULT NULL COMMENT '代付费率类型（1按比例，2按笔）',
  `pay_fee` decimal(10,2) DEFAULT NULL COMMENT '代付费率',
  `loan_fee_type` int(1) DEFAULT NULL COMMENT '垫资费率类型（1按比例，2按笔）',
  `loan_fee` decimal(10,2) DEFAULT NULL COMMENT '垫资费率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户/机构费率配置';

-- ----------------------------
-- Records of t_merchant_fee_conf
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_route_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_route_conf`;
CREATE TABLE `t_merchant_route_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` int(1) DEFAULT NULL COMMENT '主体类型',
  `channel_id` int(11) DEFAULT NULL COMMENT '渠道ID',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户ID',
  `loaning` int(1) DEFAULT NULL COMMENT '是否垫资（0不垫资，1垫资）',
  `loaning_org_id` int(11) DEFAULT NULL COMMENT '垫资机构ID',
  `loaning_amount` decimal(10,2) DEFAULT NULL COMMENT '垫资金额',
  `status` int(1) DEFAULT NULL COMMENT '状态（0冻结，1正常）',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL  COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '最后修改人',
  `modify_time` datetime DEFAULT NULL  COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路由配置表';

-- ----------------------------
-- Records of t_merchant_route_conf
-- ----------------------------

-- ----------------------------
-- Table structure for t_trade
-- ----------------------------
DROP TABLE IF EXISTS `t_trade`;
CREATE TABLE `t_trade` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户id',
  `order_no` varchar(32) DEFAULT NULL COMMENT '订单号',
  `batch_no` varchar(255) DEFAULT NULL COMMENT '批次号',
  `trade_time` datetime DEFAULT NULL COMMENT '交易时间',
  `trade_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `status` int(1) DEFAULT NULL COMMENT '状态（1处理中，2成功，3失败）',
  `channel_id` int(11) DEFAULT NULL COMMENT '渠道ID',
  `return_code` varchar(255) DEFAULT NULL COMMENT '返回码',
  `return_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  `payer_id` int(11) DEFAULT NULL COMMENT '付款方ID',
  `payee_bank_name` varchar(255) DEFAULT NULL COMMENT '银行名称',
  `payee_bank_acct_type` int(1) DEFAULT NULL COMMENT '账户类型(1企业；2个人)',
  `payee_name` varchar(255) DEFAULT NULL COMMENT '收款人姓名',
  `payee_bank_card` varchar(255) DEFAULT NULL COMMENT '收款人银行账号',
  `payee_bank_code` varchar(255) DEFAULT NULL COMMENT '收款人联行号',
  `trade_confirm_time` datetime DEFAULT NULL COMMENT '交易确认时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `settle_status` int(1) DEFAULT NULL COMMENT '结算状态（0：未导出，1已导出）',
  `recon_date` datetime DEFAULT NULL COMMENT '对账日期（格式yyyymmdd）',
  `recon_status` int(1) DEFAULT NULL COMMENT '对账状态(1成功，2失败)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_MERCHANT_ORDERNO` (`merchant_id`,`order_no`),
  KEY `INX_BATCH_NO` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易信息表'

-- ----------------------------
-- Records of t_trade
-- ----------------------------

CREATE TABLE `t_merchant_channel` (
  `merchant_id` int(11) NOT NULL DEFAULT '0' COMMENT '商户ID',
  `channel_id` int(11) NOT NULL DEFAULT '0' COMMENT '渠道ID',
  `merchant_channel_code` varchar(255) DEFAULT NULL COMMENT '商户渠道编号',
  PRIMARY KEY (`merchant_id`,`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户渠道备案信息';

CREATE TABLE `t_merchant_secret` (
  `merchant_id` int(11) NOT NULL DEFAULT '0' COMMENT '商户ID',
  `secret` blob COMMENT '密钥',
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户密钥';

DROP TABLE IF EXISTS `t_batch_trade`;
CREATE TABLE `t_batch_trade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(32) DEFAULT NULL COMMENT '批次号',
  `trade_time` datetime DEFAULT NULL COMMENT '交易时间',
  `total_amount` decimal(15,2) DEFAULT NULL COMMENT '总金额',
  `total_count` int(11) DEFAULT NULL COMMENT '总笔数',
  `payer_merchant_id` int(11) DEFAULT NULL COMMENT '付款方商户ID',
  `status` int(1) DEFAULT NULL COMMENT '状态(1待审核，2审核通过，3审核不通过)',
  `creator` varchar(255) DEFAULT NULL COMMENT '经办人',
  `create_time` datetime DEFAULT NULL COMMENT '经办时间',
  `auditor` varchar(255) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_optinion` varchar(255) DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='批量代付信息表';

