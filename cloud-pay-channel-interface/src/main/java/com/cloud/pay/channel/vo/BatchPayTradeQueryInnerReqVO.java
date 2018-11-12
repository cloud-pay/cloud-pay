package com.cloud.pay.channel.vo;

/**
 * 批量代付结果查询
 * @author 
 */
public class BatchPayTradeQueryInnerReqVO extends BatchPayTradeQueryReqVO {

	private static final long serialVersionUID = -8547843964055061101L;
	
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	} 
}

