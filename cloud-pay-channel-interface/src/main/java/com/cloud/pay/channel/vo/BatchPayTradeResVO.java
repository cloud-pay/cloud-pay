package com.cloud.pay.channel.vo;

public class BatchPayTradeResVO extends BaseTradeResVO {

	private static final long serialVersionUID = -6954718571400196610L;

	private String channelCode; //渠道编码

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
}
