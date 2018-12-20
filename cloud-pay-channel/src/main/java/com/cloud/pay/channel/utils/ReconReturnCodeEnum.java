package com.cloud.pay.channel.utils;

public enum ReconReturnCodeEnum {

	RETURN_SUCCESS(1,"PR04"),RETURN_FAIL(2,"PR09"),RETURN_UNKNOWN(99,"PR99");
	
	private Integer status;
	
	private String channelCode;
	
	private ReconReturnCodeEnum(Integer status,String channelCode) {
		this.status =status;
		this.channelCode = channelCode;
	}

	public static Integer getStatus(String channelCode) {
		for(ReconReturnCodeEnum returnCode:values()) {
			if(channelCode.equals(returnCode.getChannelCode())) {
				return returnCode.getStatus();
			}
		}
		return null;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	
	
}
