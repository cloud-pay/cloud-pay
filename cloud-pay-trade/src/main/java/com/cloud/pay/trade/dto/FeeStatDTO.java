package com.cloud.pay.trade.dto;

import java.math.BigDecimal;

public class FeeStatDTO {

	private String statDate;
	private String merchantCode;
	private String merchantName;
	private String orgName;
	private int totalCount;
	private BigDecimal totalAmount;
	private BigDecimal loaningTotalAmount;
	private BigDecimal feeAmount;

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
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

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getLoaningTotalAmount() {
		return loaningTotalAmount;
	}

	public void setLoaningTotalAmount(BigDecimal loaningTotalAmount) {
		this.loaningTotalAmount = loaningTotalAmount;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

}
