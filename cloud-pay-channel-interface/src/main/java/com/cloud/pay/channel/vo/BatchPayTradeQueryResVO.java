package com.cloud.pay.channel.vo;

public class BatchPayTradeQueryResVO extends BaseTradeResVO{

	private static final long serialVersionUID = -6454477994173419881L;
	
	public BatchPayTradeQueryResVO(String errorCode,String errorMessage) {
		super(errorCode, errorMessage);
	}
   
	public BatchPayTradeQueryResVO(Integer merchantId,String orderNo,String respCode,String respMsg){
		super(merchantId, orderNo, respCode, respMsg);
	}
	
	public BatchPayTradeQueryResVO(Integer merchantId,String orderNo,String respCode,String errorCode,String errorMessage){
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
