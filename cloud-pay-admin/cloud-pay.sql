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
  `period` int(1) DEFAULT NULL COMMENT '统计周期',
  `amount_limit` decimal(10,2) DEFAULT NULL,
  `modifer` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
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
  `channel_code` varchar(100) DEFAULT NULL COMMENT '渠道编码',
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
-- Table structure for t_merchant_apply_fee_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_apply_fee_conf`;
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
  `code` varchar(100) DEFAULT NULL COMMENT '商户编号',
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
  `audit_optinion` varchar(255) DEFAULT NULL COMMENT '审核意见',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='商户申请基本信息';

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
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(255) DEFAULT NULL COMMENT '简称',
  `type` int(1) DEFAULT NULL COMMENT '类型(1代理商，2第三方支付，3垫资商，4企业商户，5个人商户)',
  `status` int(1) DEFAULT NULL COMMENT '状态(1正常;0冻结)',
  `industry_category` varchar(255) DEFAULT NULL COMMENT '行业类别',
  `legal` varchar(100) DEFAULT NULL COMMENT '法人',
  `city` varchar(100) DEFAULT NULL COMMENT '所属城市',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifer` varchar(100) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='商户基本信息';

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
  `loaning` int(1) DEFAULT NULL COMMENT '是否垫资（0不垫资，1垫资）',
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
  `merchant_fee_amount` decimal(15,2) DEFAULT NULL COMMENT '商户手续费',
  `org_benefit` decimal(15,2) DEFAULT NULL COMMENT '机构分润费',
  `loan_benefit` decimal(15,2) DEFAULT NULL COMMENT '垫资分润费',
  `seq_no` int(5) DEFAULT NULL COMMENT '批次序号',
  PRIMARY KEY (`id`),
  KEY `INX_BATCH_NO` (`batch_no`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='交易信息表';

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
  `secret` varchar(255) DEFAULT NULL COMMENT '密钥',
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

DROP TABLE IF EXISTS `t_merchant_prepay_info`;
CREATE TABLE `t_merchant_prepay_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户id',
  `balance` decimal(15,2) DEFAULT NULL COMMENT '余额',
  `freeze_amount` decimal(15,2) DEFAULT NULL COMMENT '冻结金额',
  `overdraw` int(1) DEFAULT NULL COMMENT '是否透资(1可透资;2不可透资)',
  `digest` varchar(255) DEFAULT NULL COMMENT '摘要',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户预缴户信息';

DROP TABLE IF EXISTS `t_merchant_prepay_journal`;
CREATE TABLE `t_merchant_prepay_journal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `debit` int(1) DEFAULT NULL COMMENT '出入账标识(1入账；2出账)',
  `trade_id` int(11) DEFAULT NULL COMMENT '交易ID',
  `prepay_trade_id` int(11) DEFAULT NULL COMMENT '预缴户交易id',
  `prepay_id` int(11) DEFAULT NULL COMMENT '预缴户id',
  `amount` decimal(15,2) DEFAULT NULL COMMENT '发生额',
  `balance` decimal(15,2) DEFAULT NULL COMMENT '变动后余额',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `type` int(1) DEFAULT NULL COMMENT '类型(1交易;2手续费)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户预缴户变动'

CREATE TABLE `t_pay_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `batch_no` varchar(32) DEFAULT NULL COMMENT '批次号',
  `sms_code` varchar(6) DEFAULT NULL COMMENT '短信验证码',
  `sms_biz_id` varchar(255) DEFAULT NULL COMMENT '短信流水号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `verfiy_result` int(1) DEFAULT NULL COMMENT '验证结果(0未验证；1成功；2失败；3超过有效期；4超过最大验证次数)',
  `verify_times` int(1) DEFAULT NULL COMMENT '验证次数',
  `verify_time` datetime DEFAULT NULL COMMENT '验证时间',
  PRIMARY KEY (`id`),
  KEY `INX_BATCH_NO` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手工代付短信记录';

CREATE TABLE `t_sys_config` (
  `sys_key` varchar(30) NOT NULL DEFAULT '' COMMENT '系统key',
  `sys_value` varchar(100) DEFAULT NULL COMMENT '系统值',
  `sys_desc` varchar(100) DEFAULT NULL COMMENT '参数说明',
  PRIMARY KEY (`sys_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数配置信息';
insert into t_sys_config values('accessKeyId','accessKeyId','短信accessKeyId');
insert into t_sys_config values('accessKeySecret','accessKeySecret','短信accessKeySecret');
insert into t_sys_config values('signName','交子元','短信签名');
insert into t_sys_config values('verifyMaxTimes','5','短信最大验证次数');
insert into t_sys_config values('expiryTime','15','短信有效期(分钟)');

CREATE TABLE `t_user_merchant` (
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户商户关联表';

CREATE TABLE `t_prepay_trade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trade_type` int(1) DEFAULT NULL COMMENT '交易类型(1充值；2提现)',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户ID',
  `trade_time` date DEFAULT NULL COMMENT '交易时间',
  `amount` decimal(15,2) DEFAULT NULL COMMENT '交易金额',
  `status` int(1) DEFAULT NULL COMMENT '交易状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='商户预缴户交易';

#省级 t_provincial
CREATE TABLE `t_provincial` (
  `pid` int(11) NOT NULL,
  `provincial` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into t_provincial values(1,'北京市');
insert into t_provincial values(2,'天津市');
insert into t_provincial values(3,'上海市');
insert into t_provincial values(4,'重庆市');
insert into t_provincial values(5,'河北省');
insert into t_provincial values(6,'山西省');
insert into t_provincial values(7,'台湾省');
insert into t_provincial values(8,'辽宁省');
insert into t_provincial values(9,'吉林省');
insert into t_provincial values(10,'黑龙江省');
insert into t_provincial values(11,'江苏省');
insert into t_provincial values(12,'浙江省');
insert into t_provincial values(13,'安徽省');
insert into t_provincial values(14,'福建省');
insert into t_provincial values(15,'江西省');
insert into t_provincial values(16,'山东省');
insert into t_provincial values(17,'河南省');
insert into t_provincial values(18,'湖北省');
insert into t_provincial values(19,'湖南省');
insert into t_provincial values(20,'广东省');
insert into t_provincial values(21,'甘肃省');
insert into t_provincial values(22,'四川省');
insert into t_provincial values(23,'贵州省');
insert into t_provincial values(24,'海南省');
insert into t_provincial values(25,'云南省');
insert into t_provincial values(26,'青海省');
insert into t_provincial values(27,'陕西省');
insert into t_provincial values(28,'广西壮族自治区');
insert into t_provincial values(29,'西藏自治区');
insert into t_provincial values(30,'宁夏回族自治区');
insert into t_provincial values(31,'新疆维吾尔自治区');
insert into t_provincial values(32,'内蒙古自治区');
insert into t_provincial values(33,'澳门特别行政区');
insert into t_provincial values(34,'香港特别行政区');

#城市 t_city
CREATE TABLE `t_city` (
  `cid` int(11) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

###############################-
#插入各个省的城市数据
#4个直辖市
insert into t_city values(1,'北京市',1);
insert into t_city values(2,'天津市',2);
insert into t_city values(3,'上海市',3);
insert into t_city values(4,'重庆市',4);
################################
#5河北省 11个地级市
insert into t_city values(1,'石家庄市',5);
insert into t_city values(2,'唐山市',5);
insert into t_city values(3,'秦皇岛市',5);
insert into t_city values(4,'邯郸市',5);
insert into t_city values(5,'邢台市',5);
insert into t_city values(6,'保定市',5);
insert into t_city values(7,'张家口市',5);
insert into t_city values(8,'承德市',5);
insert into t_city values(9,'沧州市',5);
insert into t_city values(10,'廊坊市',5);
insert into t_city values(11,'衡水市',5);
################################
#6山西省 11个城市
insert into t_city values(1,'太原市',6);
insert into t_city values(2,'大同市',6);
insert into t_city values(3,'阳泉市',6);
insert into t_city values(4,'长治市',6);
insert into t_city values(5,'晋城市',6);
insert into t_city values(6,'朔州市',6);
insert into t_city values(7,'晋中市',6);
insert into t_city values(8,'运城市',6);
insert into t_city values(9,'忻州市',6);
insert into t_city values(10,'临汾市',6);
insert into t_city values(11,'吕梁市',6);
################################
#7台湾省(台湾本岛和澎湖共设7市、16县，其中台北市和高雄市为“院辖市”，直属“行政院”，其余属台湾省；市下设区，县下设市（县辖市）、镇、乡，合称区市镇乡。);
insert into t_city values(1,'台北市',7);
insert into t_city values(2,'高雄市',7);
insert into t_city values(3,'基隆市',7);
insert into t_city values(4,'台中市',7);
insert into t_city values(5,'台南市',7);
insert into t_city values(6,'新竹市',7);
insert into t_city values(7,'嘉义市',7);
insert into t_city values(8,'台北县',7);
insert into t_city values(9,'宜兰县',7);
insert into t_city values(10,'桃园县',7);
insert into t_city values(11,'新竹县',7);
insert into t_city values(12,'苗栗县',7);
insert into t_city values(13,'台中县',7);
insert into t_city values(14,'彰化县',7);
insert into t_city values(15,'南投县',7);
insert into t_city values(16,'云林县',7);
insert into t_city values(17,'嘉义县',7);
insert into t_city values(18,'台南县',7);
insert into t_city values(19,'高雄县',7);
insert into t_city values(20,'屏东县',7);
insert into t_city values(21,'澎湖县',7);
insert into t_city values(22,'台东县',7);
insert into t_city values(23,'花莲县',7);
################################
#8辽宁省 14个地级市
insert into t_city values(1,'沈阳市',8);
insert into t_city values(2,'大连市',8);
insert into t_city values(3,'鞍山市',8);
insert into t_city values(4,'抚顺市',8);
insert into t_city values(5,'本溪市',8);
insert into t_city values(6,'丹东市',8);
insert into t_city values(7,'锦州市',8);
insert into t_city values(8,'营口市',8);
insert into t_city values(9,'阜新市',8);
insert into t_city values(10,'辽阳市',8);
insert into t_city values(11,'盘锦市',8);
insert into t_city values(12,'铁岭市',8);
insert into t_city values(13,'朝阳市',8);
insert into t_city values(14,'葫芦岛市',8);
################################
#9吉林省(2006年，辖：8个地级市、1个自治州；20个市辖区、20个县级市、17个县、3个自治县。);
insert into t_city values(1,'长春市',9);
insert into t_city values(2,'吉林市',9);
insert into t_city values(3,'四平市',9);
insert into t_city values(4,'辽源市',9);
insert into t_city values(5,'通化市',9);
insert into t_city values(6,'白山市',9);
insert into t_city values(7,'松原市',9);
insert into t_city values(8,'白城市',9);
insert into t_city values(9,'延边朝鲜族自治州',9);
################################
#10黑龙江省(2006年，辖：12地级市、1地区；64市辖区、18县级市、45县、1自治县);
insert into t_city values(1,'哈尔滨市',10);
insert into t_city values(2,'齐齐哈尔市',10);
insert into t_city values(3,'鹤 岗 市',10);
insert into t_city values(4,'双鸭山市',10);
insert into t_city values(5,'鸡 西 市',10);
insert into t_city values(6,'大 庆 市',10);
insert into t_city values(7,'伊 春 市',10);
insert into t_city values(8,'牡丹江市',10);
insert into t_city values(9,'佳木斯市',10);
insert into t_city values(10,'七台河市',10);
insert into t_city values(11,'黑 河 市',10);
insert into t_city values(12,'绥 化 市',10);
insert into t_city values(13,'大兴安岭地区',10);
################################
#11江苏省(2005年辖：13个地级市；54个市辖区、27个县级市、25个县);
insert into t_city values(1,'南京市',11);
insert into t_city values(2,'无锡市',11);
insert into t_city values(3,'徐州市',11);
insert into t_city values(4,'常州市',11);
insert into t_city values(5,'苏州市',11);
insert into t_city values(6,'南通市',11);
insert into t_city values(7,'连云港市',11);
insert into t_city values(8,'淮安市',11);
insert into t_city values(9,'盐城市',11);
insert into t_city values(10,'扬州市',11);
insert into t_city values(11,'镇江市',11);
insert into t_city values(12,'泰州市',11);
insert into t_city values(13,'宿迁市',11);
################################
#12浙江省(2006年，辖：11个地级市；32个市辖区、22个县级市、35个县、1个自治县。);
insert into t_city values(1,'杭州市',12);
insert into t_city values(2,'宁波市',12);
insert into t_city values(3,'温州市',12);
insert into t_city values(4,'嘉兴市',12);
insert into t_city values(5,'湖州市',12);
insert into t_city values(6,'绍兴市',12);
insert into t_city values(7,'金华市',12);
insert into t_city values(8,'衢州市',12);
insert into t_city values(9,'舟山市',12);
insert into t_city values(10,'台州市',12);
insert into t_city values(11,'丽水市',12);
################################
#13安徽省(2005年辖：17个地级市；44个市辖区、5县个级市、56个县。);
insert into t_city values(1,'合肥市',13);
insert into t_city values(2,'芜湖市',13);
insert into t_city values(3,'蚌埠市',13);
insert into t_city values(4,'淮南市',13);
insert into t_city values(5,'马鞍山市',13);
insert into t_city values(6,'淮北市',13);
insert into t_city values(7,'铜陵市',13);
insert into t_city values(8,'安庆市',13);
insert into t_city values(9,'黄山市',13);
insert into t_city values(10,'滁州市',13);
insert into t_city values(11,'阜阳市',13);
insert into t_city values(12,'宿州市',13);
insert into t_city values(13,'巢湖市',13);
insert into t_city values(14,'六安市',13);
insert into t_city values(15,'亳州市',13);
insert into t_city values(16,'池州市',13);
insert into t_city values(17,'宣城市',13);
################################
#14福建省(2006年辖：9个地级市；26个市辖区、14个县级市、45个县。);
insert into t_city values(1,'福州市',14);
insert into t_city values(2,'厦门市',14);
insert into t_city values(3,'莆田市',14);
insert into t_city values(4,'三明市',14);
insert into t_city values(5,'泉州市',14);
insert into t_city values(6,'漳州市',14);
insert into t_city values(7,'南平市',14);
insert into t_city values(8,'龙岩市',14);
insert into t_city values(9,'宁德市',14);
################################
#15江西省(2006年全省辖：11个地级市；19个市辖区、10个县级市、70个县。);
insert into t_city values(1,'南昌市',15);
insert into t_city values(2,'景德镇市',15);
insert into t_city values(3,'萍乡市',15);
insert into t_city values(4,'九江市',15);
insert into t_city values(5,'新余市',15);
insert into t_city values(6,'鹰潭市',15);
insert into t_city values(7,'赣州市',15);
insert into t_city values(8,'吉安市',15);
insert into t_city values(9,'宜春市',15);
insert into t_city values(10,'抚州市',15);
insert into t_city values(11,'上饶市',15);
################################
#16山东省(2005年，辖：17个地级市；49个市辖区、31个县级市、60个县。);
insert into t_city values(1,'济南市',16);
insert into t_city values(2,'青岛市',16);
insert into t_city values(3,'淄博市',16);
insert into t_city values(4,'枣庄市',16);
insert into t_city values(5,'东营市',16);
insert into t_city values(6,'烟台市',16);
insert into t_city values(7,'潍坊市',16);
insert into t_city values(8,'济宁市',16);
insert into t_city values(9,'泰安市',16);
insert into t_city values(10,'威海市',16);
insert into t_city values(11,'日照市',16);
insert into t_city values(12,'莱芜市',16);
insert into t_city values(13,'临沂市',16);
insert into t_city values(14,'德州市',16);
insert into t_city values(15,'聊城市',16);
insert into t_city values(16,'滨州市',16);
insert into t_city values(17,'菏泽市',16);
################################
#17河南省 17个地级市
insert into t_city values(1,'郑州市',17);
insert into t_city values(2,'开封市',17);
insert into t_city values(3,'洛阳市',17);
insert into t_city values(4,'平顶山市',17);
insert into t_city values(5,'安阳市',17);
insert into t_city values(6,'鹤壁市',17);
insert into t_city values(7,'新乡市',17);
insert into t_city values(8,'焦作市',17);
insert into t_city values(9,'濮阳市',17);
insert into t_city values(10,'许昌市',17);
insert into t_city values(11,'漯河市',17);
insert into t_city values(12,'三门峡市',17);
insert into t_city values(13,'南阳市',17);
insert into t_city values(14,'商丘市',17);
insert into t_city values(15,'信阳市',17);
insert into t_city values(16,'周口市',17);
insert into t_city values(17,'驻马店市',17);
insert into t_city values(18,'济源市',17);
################################
#18湖北省（截至2005年12月31日，全省辖13个地级单位（12个地级市、1个自治州）；102县级单位（38个市辖区、24个县级市、37个县、2个自治县、1个林区），共有1220个乡级单位（277个街道、733个镇、210个乡）。）
insert into t_city values(1,'武汉市',18);
insert into t_city values(2,'黄石市',18);
insert into t_city values(3,'十堰市',18);
insert into t_city values(4,'荆州市',18);
insert into t_city values(5,'宜昌市',18);
insert into t_city values(6,'襄樊市',18);
insert into t_city values(7,'鄂州市',18);
insert into t_city values(8,'荆门市',18);
insert into t_city values(9,'孝感市',18);
insert into t_city values(10,'黄冈市',18);
insert into t_city values(11,'咸宁市',18);
insert into t_city values(12,'随州市',18);
insert into t_city values(13,'仙桃市',18);
insert into t_city values(14,'天门市',18);
insert into t_city values(15,'潜江市',18);
insert into t_city values(16,'神农架林区',18);
insert into t_city values(17,'恩施土家族苗族自治州',18);
################################
#19湖南省（2005年辖：13个地级市、1个自治州；34个市辖区、16个县级市、65个县、7个自治县。）
insert into t_city values(1,'长沙市',19);
insert into t_city values(2,'株洲市',19);
insert into t_city values(3,'湘潭市',19);
insert into t_city values(4,'衡阳市',19);
insert into t_city values(5,'邵阳市',19);
insert into t_city values(6,'岳阳市',19);
insert into t_city values(7,'常德市',19);
insert into t_city values(8,'张家界市',19);
insert into t_city values(9,'益阳市',19);
insert into t_city values(10,'郴州市',19);
insert into t_city values(11,'永州市',19);
insert into t_city values(12,'怀化市',19);
insert into t_city values(13,'娄底市',19);
insert into t_city values(14,'湘西土家族苗族自治州',19);
################################
#20广东省（截至2005年12月31日，广东省辖：21个地级市，54个市辖区、23个县级市、41个县、3个自治县，429个街道办事处、1145个镇、4个乡、7个民族乡。）
insert into t_city values(1,'广州市',20);
insert into t_city values(2,'深圳市',20);
insert into t_city values(3,'珠海市',20);
insert into t_city values(4,'汕头市',20);
insert into t_city values(5,'韶关市',20);
insert into t_city values(6,'佛山市',20);
insert into t_city values(7,'江门市',20);
insert into t_city values(8,'湛江市',20);
insert into t_city values(9,'茂名市',20);
insert into t_city values(10,'肇庆市',20);
insert into t_city values(11,'惠州市',20);
insert into t_city values(12,'梅州市',20);
insert into t_city values(13,'汕尾市',20);
insert into t_city values(14,'河源市',20);
insert into t_city values(15,'阳江市',20);
insert into t_city values(16,'清远市',20);
insert into t_city values(17,'东莞市',20);
insert into t_city values(18,'中山市',20);
insert into t_city values(19,'潮州市',20);
insert into t_city values(20,'揭阳市',20);
insert into t_city values(21,'云浮市',20);
################################
#21甘肃省 12个地级市、2个自治州
insert into t_city values(1,'兰州市',21);
insert into t_city values(2,'金昌市',21);
insert into t_city values(3,'白银市',21);
insert into t_city values(4,'天水市',21);
insert into t_city values(5,'嘉峪关市',21);
insert into t_city values(6,'武威市',21);
insert into t_city values(7,'张掖市',21);
insert into t_city values(8,'平凉市',21);
insert into t_city values(9,'酒泉市',21);
insert into t_city values(10,'庆阳市',21);
insert into t_city values(11,'定西市',21);
insert into t_city values(12,'陇南市',21);
insert into t_city values(13,'临夏回族自治州',21);
insert into t_city values(14,'甘南藏族自治州',21);
################################
#22四川省18个地级市、3个自治州
insert into t_city values(1,'成都市',22);
insert into t_city values(2,'自贡市',22);
insert into t_city values(3,'攀枝花市',22);
insert into t_city values(4,'泸州市',22);
insert into t_city values(5,'德阳市',22);
insert into t_city values(6,'绵阳市',22);
insert into t_city values(7,'广元市',22);
insert into t_city values(8,'遂宁市',22);
insert into t_city values(9,'内江市',22);
insert into t_city values(10,'乐山市',22);
insert into t_city values(11,'南充市',22);
insert into t_city values(12,'眉山市',22);
insert into t_city values(13,'宜宾市',22);
insert into t_city values(14,'广安市',22);
insert into t_city values(15,'达州市',22);
insert into t_city values(16,'雅安市',22);
insert into t_city values(17,'巴中市',22);
insert into t_city values(18,'资阳市',22);
insert into t_city values(19,'阿坝藏族羌族自治州',22);
insert into t_city values(20,'甘孜藏族自治州',22);
insert into t_city values(21,'凉山彝族自治州',22);
################################
#23贵州省(2006年辖：4个地级市、2个地区、3个自治州；10个市辖区、9个县级市、56个县、11个自治县、2个特区。);
insert into t_city values(1,'贵阳市',23);
insert into t_city values(2,'六盘水市',23);
insert into t_city values(3,'遵义市',23);
insert into t_city values(4,'安顺市',23);
insert into t_city values(5,'铜仁地区',23);
insert into t_city values(6,'毕节地区',23);
insert into t_city values(7,'黔西南布依族苗族自治州',23);
insert into t_city values(8,'黔东南苗族侗族自治州',23);
insert into t_city values(9,'黔南布依族苗族自治州',23);
################################
#24海南省全省有2个地级市，6个县级市，4个县，6个民族自治县，4个市辖区，1个办事处（西南中沙群岛办事处 ，县级）。);
insert into t_city values(1,'海口市',24);
insert into t_city values(2,'三亚市',24);
insert into t_city values(3,'五指山市',24);
insert into t_city values(4,'琼海市',24);
insert into t_city values(5,'儋州市',24);
insert into t_city values(6,'文昌市',24);
insert into t_city values(7,'万宁市',24);
insert into t_city values(8,'东方市',24);
insert into t_city values(9,'澄迈县',24);
insert into t_city values(10,'定安县',24);
insert into t_city values(11,'屯昌县',24);
insert into t_city values(12,'临高县',24);
insert into t_city values(13,'白沙黎族自治县',24);
insert into t_city values(14,'昌江黎族自治县',24);
insert into t_city values(15,'乐东黎族自治县',24);
insert into t_city values(16,'陵水黎族自治县',24);
insert into t_city values(17,'保亭黎族苗族自治县',24);
insert into t_city values(18,'琼中黎族苗族自治县',24);
################################
#25云南省(2006年辖：8个地级市、8个自治州；12个市辖区、9个县级市、79个县、29个自治县。);
insert into t_city values(1,'昆明市',25);
insert into t_city values(2,'曲靖市',25);
insert into t_city values(3,'玉溪市',25);
insert into t_city values(4,'保山市',25);
insert into t_city values(5,'昭通市',25);
insert into t_city values(6,'丽江市',25);
insert into t_city values(7,'思茅市',25);
insert into t_city values(8,'临沧市',25);
insert into t_city values(9,'文山壮族苗族自治州',25);
insert into t_city values(10,'红河哈尼族彝族自治州',25);
insert into t_city values(11,'西双版纳傣族自治州',25);
insert into t_city values(12,'楚雄彝族自治州',25);
insert into t_city values(13,'大理白族自治州',25);
insert into t_city values(14,'德宏傣族景颇族自治州',25);
insert into t_city values(15,'怒江傈傈族自治州',25);
insert into t_city values(16,'迪庆藏族自治州',25);
################################
#26青海省(2006年辖：1个地级市、1个地区、6个自治州；4个市辖区、2个县级市、30个县、7个自治县。);
insert into t_city values(1,'西宁市',26);
insert into t_city values(2,'海东地区',26);
insert into t_city values(3,'海北藏族自治州',26);
insert into t_city values(4,'黄南藏族自治州',26);
insert into t_city values(5,'海南藏族自治州',26);
insert into t_city values(6,'果洛藏族自治州',26);
insert into t_city values(7,'玉树藏族自治州',26);
insert into t_city values(8,'海西蒙古族藏族自治州',26);
################################
#27陕西省(2006年辖：10个地级市；24个市辖区、3个县级市、80个县。);
insert into t_city values(1,'西安市',27);
insert into t_city values(2,'铜川市',27);
insert into t_city values(3,'宝鸡市',27);
insert into t_city values(4,'咸阳市',27);
insert into t_city values(5,'渭南市',27);
insert into t_city values(6,'延安市',27);
insert into t_city values(7,'汉中市',27);
insert into t_city values(8,'榆林市',27);
insert into t_city values(9,'安康市',27);
insert into t_city values(10,'商洛市',27);
################################
#28广西壮族自治区(2005年辖：14个地级市；34个市辖区、7个县级市、56个县、12个自治县。);
insert into t_city values(1,'南宁市',28);
insert into t_city values(2,'柳州市',28);
insert into t_city values(3,'桂林市',28);
insert into t_city values(4,'梧州市',28);
insert into t_city values(5,'北海市',28);
insert into t_city values(6,'防城港市',28);
insert into t_city values(7,'钦州市',28);
insert into t_city values(8,'贵港市',28);
insert into t_city values(9,'玉林市',28);
insert into t_city values(10,'百色市',28);
insert into t_city values(11,'贺州市',28);
insert into t_city values(12,'河池市',28);
insert into t_city values(13,'来宾市',28);
insert into t_city values(14,'崇左市',28);
################################
#29西藏自治区(2005年辖：1个地级市、6个地区；1个市辖区、1个县级市、71个县。);
insert into t_city values(1,'拉萨市',29);
insert into t_city values(2,'那曲地区',29);
insert into t_city values(3,'昌都地区',29);
insert into t_city values(4,'山南地区',29);
insert into t_city values(5,'日喀则地区',29);
insert into t_city values(6,'阿里地区',29);
insert into t_city values(7,'林芝地区',29);
################################
#30宁夏回族自治区
insert into t_city values(1,'银川市',30);
insert into t_city values(2,'石嘴山市',30);
insert into t_city values(3,'吴忠市',30);
insert into t_city values(4,'固原市',30);
insert into t_city values(5,'中卫市',30);
################################
#31新疆维吾尔自治区(2005年辖：2个地级市、7个地区、5个自治州；11个市辖区、20个县级市、62个县、6个自治县);
insert into t_city values(1,'乌鲁木齐市',31);
insert into t_city values(2,'克拉玛依市',31);
insert into t_city values(3,'石河子市　',31);
insert into t_city values(4,'阿拉尔市',31);
insert into t_city values(5,'图木舒克市',31);
insert into t_city values(6,'五家渠市',31);
insert into t_city values(7,'吐鲁番市',31);
insert into t_city values(8,'阿克苏市',31);
insert into t_city values(9,'喀什市',31);
insert into t_city values(10,'哈密市',31);
insert into t_city values(11,'和田市',31);
insert into t_city values(12,'阿图什市',31);
insert into t_city values(13,'库尔勒市',31);
insert into t_city values(14,'昌吉市　',31);
insert into t_city values(15,'阜康市',31);
insert into t_city values(16,'米泉市',31);
insert into t_city values(17,'博乐市',31);
insert into t_city values(18,'伊宁市',31);
insert into t_city values(19,'奎屯市',31);
insert into t_city values(20,'塔城市',31);
insert into t_city values(21,'乌苏市',31);
insert into t_city values(22,'阿勒泰市',31);
################################
#32内蒙古自治区(2006年，辖：9个地级市、3个盟；21个市辖区、11个县级市、17个县、49个旗、3个自治旗。);
insert into t_city values(1,'呼和浩特市',32);
insert into t_city values(2,'包头市',32);
insert into t_city values(3,'乌海市',32);
insert into t_city values(4,'赤峰市',32);
insert into t_city values(5,'通辽市',32);
insert into t_city values(6,'鄂尔多斯市',32);
insert into t_city values(7,'呼伦贝尔市',32);
insert into t_city values(8,'巴彦淖尔市',32);
insert into t_city values(9,'乌兰察布市',32);
insert into t_city values(10,'锡林郭勒盟',32);
insert into t_city values(11,'兴安盟',32);
insert into t_city values(12,'阿拉善盟',32);
################################
#33澳门特别行政区
insert into t_city values(1,'澳门特别行政区',33);
################################
#34香港特别行政区
insert into t_city values(1,'香港特别行政区',34);