package com.cloud.pay.recon.constant;

public enum ReconTypeEnum {
    SINGLE("SIG","SINGLE"),BATCH("BAT","BATCH");
	
	private String channelCode; //渠道
	
	private String localCode; //本地
	
	private ReconTypeEnum(String channelCode,String localCode) {
		 this.channelCode = channelCode;
		 this.localCode = localCode;
	}
	
	public static String getByChannelCode(String channelCode) {
		for(ReconTypeEnum reconType:values()) {
			if(reconType.channelCode.equals(channelCode)) {
				return reconType.localCode;
			}
		}
		return null;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}
	
	public static void main(String[] args) {
		System.out.println(ReconTypeEnum.getByChannelCode("BAT"));
	}
}
