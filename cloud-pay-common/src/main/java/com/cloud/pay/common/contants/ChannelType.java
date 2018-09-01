package com.cloud.pay.common.contants;

/**
 * 渠道类型
 * @author wangy
 */
public enum ChannelType {
     BOHAI(1,"渤海","bohai","1");
	 private Integer channelId; //渠道ID,考虑取数据库的ID值，方便管理，或者仅为在该枚举类里做个标识
	 
	 private String channelName; //渠道名称
	 
	 private String channelENName; //渠道代号
	 
	 private String channeCode;  //渠道代码（考虑取数据库值）
	 
	 private ChannelType(Integer channelId,String channelName,String channelENName,String channeCode) {
		   this.channelId = channelId;
		   this.channelName = channelName;
		   this.channelENName = channelENName;
		   this.channeCode = channeCode;
	 }
	 
	 public static String getChannelCodeByChannelCode(String channeCode) {
		  for(ChannelType channelType :ChannelType.values()) {
			  if(channeCode.equals(channelType.getChanneCode())) {
				  return channelType.getChannelENName();
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
    
	

	public String getChannelENName() {
		return channelENName;
	}

	public void setChannelENName(String channelENName) {
		this.channelENName = channelENName;
	}

	public String getChanneCode() {
		return channeCode;
	}

	public void setChanneCode(String channeCode) {
		this.channeCode = channeCode;
	}

    
}
