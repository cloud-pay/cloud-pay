package com.cloud.pay.trade.constant;

/**
 * 交易常量
 * @author dbnaxlc
 *
 */
public class TradeConstant {

	/**处理中 */
	public final static int STATUS_PROCESSING = 1;
	/**成功 */
	public final static int STATUS_SUCCESS = 2;
	/**失败 */
	public final static int STATUS_FAIL = 3;
	
	/**系统内部异常响应码*/
	public final static String SYS_EXCEPTION = "PAY_000000";
	/**订单号重复*/
	public final static String ORDER_NO_EXIST = "PAY_000001";
	/**预缴户交易异常*/
	public final static String PREPAY_TRADE_EXCEPTION = "PAY_000002";
	/**预缴户被篡改*/
	public final static String PREPAY_CHANGE = "PAY_000003";
	
	/**充值 */
	public final static int RECHAEGE = 1;
	/**提现 */
	public final static int WITHDRAW = 2;
	
	/**入账 */
	public final static int DEBIT = 1;
	/**出账 */
	public final static int CREDIT = 2;
	
	/**不垫资 */
	public final static int LOANING_NO = 0;
	/**垫资 */
	public final static int LOANING_YES = 1;
	
	/**交易费 */
	public final static int TRADE_FEE = 1;
	/**手续费 */
	public final static int HADNING_FEE = 2;
}
