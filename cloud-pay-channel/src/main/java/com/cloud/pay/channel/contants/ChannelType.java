package com.cloud.pay.channel.contants;

/**
 * 渠道类型
 * @author wangy
 */
public enum ChannelType {
     BOHAI(1,"渤海","bohai","1");
	 private Integer channelId; //渠道ID,考虑取数据库的ID值，方便管理，或者仅为在该枚举类里做个标识
	 
	 private String channelName; //渠道名称
	 
	 private String channelCode; //渠道代号
	 
	 private String channelNo;  //渠道代码（考虑取数据库值）
	 
	 private ChannelType(Integer channelId,String channelName,String channelCode,String channelNo) {
		   this.channelId = channelId;
		   this.channelName = channelName;
		   this.channelCode = channelCode;
		   this.channelNo = channelNo;
	 }
	 
	 public static String getChannelCodeByChannelNo(String channelNo) {
		  for(ChannelType channelType :ChannelType.values()) {
			  if(channelNo.equals(channelType.getChannelNo())) {
				  return channelType.getChannelCode();
			  }
		  }
		  return null;
	 }
	 

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	 
	 
}
