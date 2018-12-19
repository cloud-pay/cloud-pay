package com.cloud.pay.trade.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TradeRecordDTO {

	private String orderNo;

	private String batchNo;

	private String merchantCode;

	private String merchantName;

	private String orgName;

	private Date tradeTime;

	private BigDecimal tradeAmount;

	private Integer status;

	private Integer loaning;
	
	private Integer reconStatus;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLoaning() {
		return loaning;
	}

	public void setLoaning(Integer loaning) {
		this.loaning = loaning;
	}

	public Integer getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(Integer reconStatus) {
		this.reconStatus = reconStatus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TradeRecordDTO [orderNo=");
		builder.append(orderNo);
		builder.append(", batchNo=");
		builder.append(batchNo);
		builder.append(", merchantCode=");
		builder.append(merchantCode);
		builder.append(", merchantName=");
		builder.append(merchantName);
		builder.append(", orgName=");
		builder.append(orgName);
		builder.append(", tradeTime=");
		builder.append(tradeTime);
		builder.append(", tradeAmount=");
		builder.append(tradeAmount);
		builder.append(", status=");
		builder.append(status);
		builder.append(", loaning=");
		builder.append(loaning);
		builder.append(", reconStatus=");
		builder.append(reconStatus);
		builder.append("]");
		return builder.toString();
	}
	
}
