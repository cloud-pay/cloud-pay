package com.cloud.pay.channel.vo;


/**
 * 下载对账文件响应报文
 * @author admin
 *
 */
public class ReconDownFileResVO  extends BaseTradeResVO {

	private static final long serialVersionUID = 7759307919883368079L;
	
	private Integer channelId;
	
	private String ChannelCode;

	private String filePath;
	
	public ReconDownFileResVO() {}

	public ReconDownFileResVO(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}
	
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelCode() {
		return ChannelCode;
	}

	public void setChannelCode(String channelCode) {
		ChannelCode = channelCode;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
