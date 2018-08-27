package com.cloud.pay.channel.vo;

/**
 * 单笔代收付交易查询
 * @author wangy
 */
public class PayTradeQueryReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = -8285637793404662543L;

	private String channelNo; //交易渠道
	
	private String date;//交易日期（格式：YYYYMMDD HH:MM:SS）
	
    
	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
