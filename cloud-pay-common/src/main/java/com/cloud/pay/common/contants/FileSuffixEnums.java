package com.cloud.pay.common.contants;

public enum FileSuffixEnums {
    
	TXT(".txt"),REQ(".req");
	
	private String suffix;
	
	private FileSuffixEnums(String suffix){
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	
}
