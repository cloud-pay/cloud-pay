package com.cloud.pay.channel.vo;

public class BatchPayTradeQueryInnerResVO extends BatchPayTradeQueryResVO{

	private static final long serialVersionUID = -6454477994173419881L;
	
	public BatchPayTradeQueryInnerResVO(String errorCode,String errorMessage) {
		super(errorCode, errorMessage);
	}
   
	public BatchPayTradeQueryInnerResVO(Integer merchantId,String orderNo,String respCode,String respMsg){
		super(merchantId, orderNo, respCode, respMsg);
	}
	
	public BatchPayTradeQueryInnerResVO(Integer merchantId,String orderNo,String respCode,String errorCode,String errorMessage){
		super(merchantId, orderNo, respCode, errorCode, errorMessage);
	}
	private String fileName;//结果文件名

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
