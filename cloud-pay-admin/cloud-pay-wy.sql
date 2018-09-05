/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : cloud-pay

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-09-05 00:08:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_login`;
CREATE TABLE `sys_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_login
-- ----------------------------
INSERT INTO `sys_login` VALUES ('1', '1', '2018-08-03 15:15:05');
INSERT INTO `sys_login` VALUES ('2', '1', '2018-08-03 15:37:20');
INSERT INTO `sys_login` VALUES ('3', '1', '2018-08-03 15:40:14');
INSERT INTO `sys_login` VALUES ('4', '1', '2018-08-03 15:41:50');
INSERT INTO `sys_login` VALUES ('5', '1', '2018-08-03 15:43:45');
INSERT INTO `sys_login` VALUES ('6', '1', '2018-08-03 18:22:09');
INSERT INTO `sys_login` VALUES ('7', '1', '2018-09-01 22:04:17');
INSERT INTO `sys_login` VALUES ('8', '1', '2018-09-02 11:44:53');
INSERT INTO `sys_login` VALUES ('9', '1', '2018-09-02 12:44:27');
INSERT INTO `sys_login` VALUES ('10', '1', '2018-09-02 18:27:57');
INSERT INTO `sys_login` VALUES ('11', '1', '2018-09-02 18:29:41');
INSERT INTO `sys_login` VALUES ('12', '1', '2018-09-02 18:34:49');
INSERT INTO `sys_login` VALUES ('13', '1', '2018-09-02 18:36:21');
INSERT INTO `sys_login` VALUES ('14', '1', '2018-09-02 18:38:28');
INSERT INTO `sys_login` VALUES ('15', '1', '2018-09-02 18:43:35');
INSERT INTO `sys_login` VALUES ('16', '1', '2018-09-02 18:46:38');
INSERT INTO `sys_login` VALUES ('17', '1', '2018-09-02 18:54:09');
INSERT INTO `sys_login` VALUES ('18', '1', '2018-09-02 18:54:45');
INSERT INTO `sys_login` VALUES ('19', '1', '2018-09-02 18:55:48');
INSERT INTO `sys_login` VALUES ('20', '1', '2018-09-02 18:57:55');
INSERT INTO `sys_login` VALUES ('21', '1', '2018-09-02 21:12:16');
INSERT INTO `sys_login` VALUES ('22', '1', '2018-09-02 21:18:36');
INSERT INTO `sys_login` VALUES ('23', '1', '2018-09-02 21:19:30');
INSERT INTO `sys_login` VALUES ('24', '1', '2018-09-02 21:20:49');
INSERT INTO `sys_login` VALUES ('25', '1', '2018-09-02 21:31:30');
INSERT INTO `sys_login` VALUES ('26', '1', '2018-09-02 21:32:51');
INSERT INTO `sys_login` VALUES ('27', '1', '2018-09-02 21:33:33');
INSERT INTO `sys_login` VALUES ('28', '1', '2018-09-02 21:39:31');
INSERT INTO `sys_login` VALUES ('29', '1', '2018-09-02 21:44:06');
INSERT INTO `sys_login` VALUES ('30', '1', '2018-09-02 21:46:18');
INSERT INTO `sys_login` VALUES ('31', '1', '2018-09-02 21:48:49');
INSERT INTO `sys_login` VALUES ('32', '1', '2018-09-02 21:50:20');
INSERT INTO `sys_login` VALUES ('33', '1', '2018-09-02 21:51:50');
INSERT INTO `sys_login` VALUES ('34', '1', '2018-09-02 21:59:45');
INSERT INTO `sys_login` VALUES ('35', '1', '2018-09-02 22:00:40');
INSERT INTO `sys_login` VALUES ('36', '1', '2018-09-02 22:25:54');
INSERT INTO `sys_login` VALUES ('37', '1', '2018-09-02 22:56:25');
INSERT INTO `sys_login` VALUES ('38', '1', '2018-09-02 22:58:34');
INSERT INTO `sys_login` VALUES ('39', '1', '2018-09-02 22:59:55');
INSERT INTO `sys_login` VALUES ('40', '1', '2018-09-02 23:02:07');
INSERT INTO `sys_login` VALUES ('41', '1', '2018-09-02 23:12:39');
INSERT INTO `sys_login` VALUES ('42', '1', '2018-09-02 23:13:27');
INSERT INTO `sys_login` VALUES ('43', '1', '2018-09-02 23:27:09');
INSERT INTO `sys_login` VALUES ('44', '1', '2018-09-02 23:27:56');
INSERT INTO `sys_login` VALUES ('45', '1', '2018-09-02 23:30:38');
INSERT INTO `sys_login` VALUES ('46', '1', '2018-09-02 23:33:23');
INSERT INTO `sys_login` VALUES ('47', '1', '2018-09-02 23:36:04');
INSERT INTO `sys_login` VALUES ('48', '1', '2018-09-02 23:40:12');
INSERT INTO `sys_login` VALUES ('49', '1', '2018-09-02 23:40:58');
INSERT INTO `sys_login` VALUES ('50', '1', '2018-09-02 23:41:57');
INSERT INTO `sys_login` VALUES ('51', '1', '2018-09-02 23:41:58');
INSERT INTO `sys_login` VALUES ('52', '1', '2018-09-02 23:46:13');
INSERT INTO `sys_login` VALUES ('53', '1', '2018-09-02 23:51:49');
INSERT INTO `sys_login` VALUES ('54', '1', '2018-09-02 23:51:49');
INSERT INTO `sys_login` VALUES ('55', '1', '2018-09-04 23:55:49');
INSERT INTO `sys_login` VALUES ('56', '1', '2018-09-05 00:00:27');
INSERT INTO `sys_login` VALUES ('57', '1', '2018-09-05 00:03:43');
INSERT INTO `sys_login` VALUES ('58', '1', '2018-09-05 00:06:22');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_url` varchar(50) DEFAULT '#',
  `menu_type` enum('2','1') DEFAULT '2' COMMENT '1 -- 系统菜单，2 -- 业务菜单',
  `menu_icon` varchar(50) DEFAULT '#',
  `sort_num` int(11) DEFAULT '1',
  `user_id` int(11) DEFAULT '1' COMMENT '创建这个菜单的用户id',
  `is_del` int(11) DEFAULT '0' COMMENT '1-- 删除状态，0 -- 正常',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', '#', '1', 'fa fa-gears', '1', '1', '0', '2017-09-08 16:15:24', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('2', '1', '菜单管理', 'menu/list', '1', '#', '1', '1', '0', '2017-09-12 11:28:09', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', 'role/list', '1', null, '2', '1', '0', '2017-09-07 17:58:52', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('4', '1', '用户管理', 'user/list', '1', '', '3', '1', '0', '2017-09-12 09:44:48', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('5', '0', '商户管理', '#', '2', 'fa fa-tasks', '2', '1', '0', '2018-07-30 19:12:51', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('6', '5', '商户列表', '/member/list', '2', '', '1', '1', '0', '2018-07-31 14:19:35', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('7', '0', '资金/对账管理', '#', '2', 'fa fa-tasks', '5', '1', '0', '2018-09-02 18:36:07', '2018-09-02 18:31:29');
INSERT INTO `sys_menu` VALUES ('8', '7', '对账', '/recon/list', '2', ' fa-balance-scale', '1', '1', '0', '2018-09-02 18:32:45', '2018-09-02 18:32:12');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) DEFAULT NULL COMMENT '角色名',
  `role_desc` varchar(255) DEFAULT NULL,
  `rights` varchar(255) DEFAULT '0' COMMENT '最大权限的值',
  `add_qx` varchar(255) DEFAULT '0',
  `del_qx` varchar(255) DEFAULT '0',
  `edit_qx` varchar(255) DEFAULT '0',
  `query_qx` varchar(255) DEFAULT '0',
  `user_id` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', '管理员权限', '510', '384', '384', '510', '510', '1', '2018-09-02 18:34:31');
INSERT INTO `sys_role` VALUES ('2', 'tyro', '随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的', '510', '2', '1', '4', '126', '1', '2018-09-02 21:40:31');
INSERT INTO `sys_role` VALUES ('3', 'test', '是测试角色这个是测试角色这个是测试角色这个是测试角色这个是测试角色', '510', '382', '382', '382', '126', '1', '2018-09-02 21:40:34');
INSERT INTO `sys_role` VALUES ('4', '查看', '可以查看所有的东西', '510', '0', '0', '0', '126', '1', '2018-09-02 21:40:38');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `pic_path` varchar(200) DEFAULT '/images/logo.png',
  `status` enum('unlock','lock') DEFAULT 'unlock',
  `sessionId` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '管理员', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'http://www.lrshuai.top/upload/user/20170612/05976238.png', 'unlock', 'B961476B58D9977B58BDC97E54717C10', '2017-08-18 13:57:32');
INSERT INTO `sys_user` VALUES ('2', 'tyro', 'tyro', '481c63e8b904bb8399f1fc1dfdb77cb40842eb6f', '/upload/show/user/82197046.png', 'unlock', null, '2017-09-12 14:03:39');
INSERT INTO `sys_user` VALUES ('3', 'asdf', 'asdf', '3da541559918a808c2402bba5012f6c60b27661c', '/upload/show/user/85610497.png', 'unlock', null, '2017-09-13 14:49:10');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('2', '2', '3', '2017-09-08 17:12:58');
INSERT INTO `sys_user_role` VALUES ('13', '3', '3', '2017-09-14 14:30:02');
INSERT INTO `sys_user_role` VALUES ('14', '1', '1', '2018-09-02 18:33:24');

-- ----------------------------
-- Table structure for t_amount_limit
-- ----------------------------
DROP TABLE IF EXISTS `t_amount_limit`;
CREATE TABLE `t_amount_limit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` int(1) DEFAULT NULL COMMENT '限制类型',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户id',
  `period` int(1) DEFAULT NULL COMMENT '统计周期',
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
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
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
  `channel_code` varchar(50) DEFAULT NULL,
  `modifer` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='渠道信息';

-- ----------------------------
-- Records of t_channel
-- ----------------------------
INSERT INTO `t_channel` VALUES ('1', '渤海', '2018090200000343', '1', '1', '0.02', '1', '2018-09-02 17:16:50', 'bohai', '1', '2018-09-02 17:17:01');
INSERT INTO `t_channel` VALUES ('2', '华夏', '2018090200000345', '1', '1', '1.00', null, '2018-09-02 21:56:53', 'huaxia', '1', '2018-09-02 21:57:02');

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
  `city` varchar(100) DEFAULT NULL COMMENT '所属城市',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `status` int(1) DEFAULT NULL COMMENT '申请状态(1待审核，2审核通过，3审核不通过)',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
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
  `city` varchar(100) DEFAULT NULL COMMENT '所属城市',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
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
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '最后修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路由配置表';

-- ----------------------------
-- Records of t_merchant_route_conf
-- ----------------------------

-- ----------------------------
-- Table structure for t_recon
-- ----------------------------
DROP TABLE IF EXISTS `t_recon`;
CREATE TABLE `t_recon` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID自增',
  `ACCOUNT_DATE` date DEFAULT NULL COMMENT '记账日期（交易日期）',
  `CHANNEL_ID` int(11) DEFAULT NULL COMMENT '渠道表ID',
  `TRADE_TOTAL` int(11) DEFAULT NULL COMMENT '订单总笔数',
  `TRADE_AMT_TOTAL` decimal(10,2) DEFAULT NULL COMMENT '订单总笔数',
  `EXCEPTION_TOTAL` int(11) DEFAULT NULL COMMENT '异常总笔数',
  `RECON_STATUS` int(1) DEFAULT NULL COMMENT '对账状态：0-未对账，1-对账成功，2-对账失败，3-对账中',
  `FAIL_RESON` varchar(255) DEFAULT NULL COMMENT '失败原因',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_recon
-- ----------------------------
INSERT INTO `t_recon` VALUES ('1', '2018-09-04', '1', null, null, null, '3', '2001:对账文件不存在', '2018-09-02 17:18:57');
INSERT INTO `t_recon` VALUES ('2', '2018-08-31', '1', '1000', '9445332.24', null, '2', '2001:对账文件不存在', '2018-09-02 19:11:41');
INSERT INTO `t_recon` VALUES ('3', '2018-08-30', '1', null, null, null, '2', '2001:对账文件不存在', '2018-09-02 19:11:39');
INSERT INTO `t_recon` VALUES ('4', '2018-09-02', '1', null, null, null, '2', '2001:对账文件不存在', '2018-09-02 22:00:43');
INSERT INTO `t_recon` VALUES ('5', '2018-09-02', '2', null, null, null, '2', '渠道不存在', '2018-09-02 22:00:43');

-- ----------------------------
-- Table structure for t_recon_channel_bohai
-- ----------------------------
DROP TABLE IF EXISTS `t_recon_channel_bohai`;
CREATE TABLE `t_recon_channel_bohai` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
  `SINGLE_OR_BATCH` enum('batch','single') DEFAULT 'single' COMMENT '单笔或批量表示',
  `INSTID` varchar(32) DEFAULT NULL COMMENT '机构标识',
  `TRADE_ORDER_NO` varchar(32) DEFAULT NULL COMMENT '交易流水',
  `PAYER_ACCOUNT` varchar(50) DEFAULT NULL COMMENT '付款人账号',
  `PAYER_NAME` varchar(50) DEFAULT NULL COMMENT '付款人账户名',
  `PAYEE_ACCOUNT` varchar(50) DEFAULT NULL COMMENT '收款人账号',
  `PAYEE_NAME` varchar(50) DEFAULT NULL COMMENT '收款人账户名',
  `BANK_CODE` varchar(10) DEFAULT NULL COMMENT '收付款人接收行号',
  `TRADE_AMOUNT` decimal(10,2) DEFAULT NULL,
  `TRADE_STATUS` int(1) DEFAULT NULL COMMENT '交易状态(1-成功，2-失败)',
  `TRADE_STATUS_DESC` varchar(255) DEFAULT NULL COMMENT '交易状态信息',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_recon_channel_bohai
-- ----------------------------

-- ----------------------------
-- Table structure for t_recon_exception_trade
-- ----------------------------
DROP TABLE IF EXISTS `t_recon_exception_trade`;
CREATE TABLE `t_recon_exception_trade` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID自增长',
  `ORDER_NO` varchar(32) NOT NULL COMMENT '交易订单号',
  `EXCEPTION_REASON` varchar(255) DEFAULT NULL COMMENT '异常原因',
  `CREATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_recon_exception_trade
-- ----------------------------

-- ----------------------------
-- Table structure for t_trade
-- ----------------------------
DROP TABLE IF EXISTS `t_trade`;
CREATE TABLE `t_trade` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_no` varchar(32) DEFAULT NULL COMMENT '订单号',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户id',
  `trade_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '交易时间',
  `trade_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `status` int(1) DEFAULT NULL COMMENT '状态（1处理中，2成功，3失败）',
  `channel_id` int(11) DEFAULT NULL COMMENT '渠道ID',
  `return_code` varchar(255) DEFAULT NULL COMMENT '返回码',
  `return_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  `payer_id` int(11) DEFAULT NULL COMMENT '付款方ID',
  `payee_name` varchar(255) DEFAULT NULL COMMENT '收款人姓名',
  `payee_bank_card` varchar(255) DEFAULT NULL COMMENT '收款人银行账号',
  `payee_bank_code` varchar(255) DEFAULT NULL COMMENT '收款人联行号',
  `trade_confirm_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '交易确认时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `settle_status` int(1) DEFAULT NULL COMMENT '结算状态（0：未导出，1已导出）',
  `recon_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '对账日期（格式yyyymmdd）',
  `recon_status` int(1) DEFAULT NULL COMMENT '对账状态(1成功，2失败)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易信息表';

-- ----------------------------
-- Records of t_trade
-- ----------------------------
