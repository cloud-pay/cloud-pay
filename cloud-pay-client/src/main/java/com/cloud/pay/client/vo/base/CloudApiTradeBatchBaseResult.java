package com.cloud.pay.client.vo.base;

/**
 * 批量交易响应结果
 * @author 
 */
public class CloudApiTradeBatchBaseResult extends CloudApiBaseResult {

	private static final long serialVersionUID = 1753680561876722286L;
	
	private String platBatchNo;

	private String batchNo;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getPlatBatchNo() {
		return platBatchNo;
	}

	public void setPlatBatchNo(String platBatchNo) {
		this.platBatchNo = platBatchNo;
	}
}
