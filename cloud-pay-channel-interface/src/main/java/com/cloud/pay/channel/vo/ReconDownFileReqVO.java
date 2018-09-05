package com.cloud.pay.channel.vo;

/**
 * 下载渠道对账文件
 * @author wangy
 */
public class ReconDownFileReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = 7785416274117897519L;
     
	
	private String channelCode; //渠道编码

	private String reconDate; //对账日期，格式:yyyymmdd


	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getReconDate() {
		return reconDate;
	}

	public void setReconDate(String reconDate) {
		this.reconDate = reconDate;
	}
	
	
	
}
