package com.cloud.pay.trade.constant;

/**
 * 交易常量
 * @author dbnaxlc
 *
 */
public class SmsConstant {

	/**未验证 */
	public final static int VERIFY_NO = 0;
	/**成功 */
	public final static int VERIFY_SUCCESS = 1;
	/**失败 */
	public final static int VERIFY_FAIL = 2;
	/**超过有效期 */
	public final static int VERIFY_EXPIRY_DATE = 3;
	/**超过最大次数 */
	public final static int VERIFY_OVER_MAX_TIMES = 4;
	
	/**验证失败*/
	public final static String VERIFY_FAIL_CODE = "SMS_000000";
	/**超过有效期*/
	public final static String VERIFY_EXPIRY_DATE_CODE = "SMS_000001";
	/**超过最大次数*/
	public final static String VERIFY_OVER_MAX_TIMES_CODE = "SMS_000002";
	
	
}
