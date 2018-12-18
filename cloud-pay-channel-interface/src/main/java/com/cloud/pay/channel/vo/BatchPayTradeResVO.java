package com.cloud.pay.channel.vo;

public class BatchPayTradeResVO extends BaseTradeResVO {

	private static final long serialVersionUID = -6954718571400196610L;
	
	private Integer status; //0-成功，1-失败，99-未知状态
	
	private String fileName; //批量文件名
	
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
