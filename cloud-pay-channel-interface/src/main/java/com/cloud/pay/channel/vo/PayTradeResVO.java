package com.cloud.pay.channel.vo;

/**
 * 代付接口响应实体
 * @author wangy
 *
 */
public class PayTradeResVO extends BaseTradeResVO{
   
	private static final long serialVersionUID = 6964116147301941371L;
	
	private String accountDate; //银行会计日期
	
	private String channelCode; //渠道编码
	
	public PayTradeResVO(String errorCode,String errorMessage) {
	   	super(errorCode, errorMessage);
	}
	
	public PayTradeResVO(String merchantNo, String orderNo, String respMsg) {
		super(merchantNo, orderNo, respMsg);
	}
	
	public PayTradeResVO(String merchantNo, String orderNo, String respMsg,String accountDate) {
		super(merchantNo, orderNo, respMsg);
		this.accountDate = accountDate;
	}
   
	public PayTradeResVO(String merchantNo,String orderNo,String respCode,String errorCode,String errorMessage) {
		super(merchantNo, orderNo, respCode, errorCode, errorMessage);
	}

	public String getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
}
