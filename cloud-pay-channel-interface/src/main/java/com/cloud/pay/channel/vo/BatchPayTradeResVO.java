package com.cloud.pay.channel.vo;

public class BatchPayTradeResVO extends BaseTradeResVO {

	private static final long serialVersionUID = -6954718571400196610L;
	
	public BatchPayTradeResVO(String errorCode,String errorMessage) {
		super(errorCode, errorMessage);
	}
	
	public BatchPayTradeResVO(String merchantNo, String orderNo, String respMsg) {
		super(merchantNo, orderNo, respMsg);
	}
	
	public BatchPayTradeResVO(String merchantNo,String orderNo,String respCode,String respMsg) {
		super(merchantNo, orderNo, respCode, respMsg);
	}
	
	public BatchPayTradeResVO(String merchantNo,String orderNo,String respCode,String errorCode,String errorMessage){
		super(merchantNo, orderNo, respCode, errorCode, errorMessage);
	}

	private String channelCode; //渠道编码

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
}
