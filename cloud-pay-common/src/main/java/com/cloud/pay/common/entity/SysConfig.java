package com.cloud.pay.common.entity;

public class SysConfig {

	private String sysKey;

	private String sysValue;

	private String sysDesc;

	public String getSysKey() {
		return sysKey;
	}

	public void setSysKey(String sysKey) {
		this.sysKey = sysKey;
	}

	public String getSysValue() {
		return sysValue;
	}

	public void setSysValue(String sysValue) {
		this.sysValue = sysValue;
	}

	public String getSysDesc() {
		return sysDesc;
	}

	public void setSysDesc(String sysDesc) {
		this.sysDesc = sysDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SysConfig [sysKey=");
		builder.append(sysKey);
		builder.append(", sysValue=");
		builder.append(sysValue);
		builder.append(", sysDesc=");
		builder.append(sysDesc);
		builder.append("]");
		return builder.toString();
	}

}