package com.cloud.pay.client.vo;

import java.math.BigDecimal;

import com.cloud.pay.client.vo.base.CloudApiTradeBatchBaseResult;

/**
 * 批量代付结果查询响应结果
 * @author THINKPAD
 *
 */
public class CloudApiTradeBatchPayQueryResult extends CloudApiTradeBatchBaseResult {

	private static final long serialVersionUID = 200216523662619489L;
	
	private String tradeTime;
	
	private BigDecimal totalAmount;
	
	private Integer totalCount;
	
	private String  fileName;
	
	private Integer status;

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
