package com.cloud.pay.channel.vo;

public class BatchPayTradeResVO extends BaseTradeResVO {

	private static final long serialVersionUID = -6954718571400196610L;
	
	public BatchPayTradeResVO(String errorCode,String errorMessage) {
		super(errorCode, errorMessage);
	}
	
	public BatchPayTradeResVO(Integer merchantId, String orderNo, String respMsg) {
		super(merchantId, orderNo, respMsg);
	}
	
	public BatchPayTradeResVO(Integer merchantId,String orderNo,String respCode,String respMsg) {
		super(merchantId, orderNo, respCode, respMsg);
	}
	
	public BatchPayTradeResVO(Integer merchantId,String orderNo,String respCode,String errorCode,String errorMessage){
		super(merchantId, orderNo, respCode, errorCode, errorMessage);
	}

	private Integer channelId; //渠道编码

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
}
