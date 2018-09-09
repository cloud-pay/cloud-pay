package com.cloud.pay.recon.constant;

public enum ReconExceptionTypeEnum {
    EXCEPTION_TYPE_SHORT(1,"短款"),
    EXCEPTION_TYPE_POSTPONE(2,"延期"),
    EXCEPTION_TYPE_MISMATH(3,"数据不匹配");
	
	Integer typeCode;
	
	String desc;
	
	private ReconExceptionTypeEnum(Integer typeCode,String desc) {
		this.typeCode = typeCode;
		this.desc = desc;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
