package com.cloud.pay.merchant.constant;

public class MerchantConstant {

	//1代理商，2第三方支付，3垫资商，4企业商户，5个人商户
	
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
}
