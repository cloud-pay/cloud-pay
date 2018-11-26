package com.cloud.pay.merchant.constant;

public class MerchantConstant {

	//1代理商，2第三方支付，3垫资商，4企业商户，5个人商户
	public final static int MERCHANT_TYPE_AGENT = 1;
	public final static int MERCHANT_TYPE_THIRD = 2; 
	public final static int MERCHANT_TYPE_LOANING = 3;
	public final static int MERCHANT_TYPE_COMPANY = 4;
	public final static int MERCHANT_TYPE_PERSON = 5;
	
	/** 正常 */
	public final static int NORMAL = 1;
	/** 冻结 */
	public final static int FREEZE = 0;
	
	/** 待审核 */
	public final static int AUDITING = 1;
	/** 审核通过 */
	public final static int AUDIT_YES = 2;
	/** 审核不通过 */
	public final static int AUDIT_NO = 3;
	
	/** 营业执照 */
	public final static int BUSINESS = 1;
	/** 银行卡 */
	public final static int BANK_CARD = 2;
	/** 身份证 */
	public final static int CERT = 3;
	/** 协议 */
	public final static int PROTOCOL = 4;
	
	/** 可透资 */
	public final static int OVERDRAW_YES = 1;
	/** 不可透资 */
	public final static int OVERDRAW_NO = 2;
	
	/** 按比例 */
	public final static int PER_RATE = 1;
	/** 按笔数 */
	public final static int PER = 2;
	
}
