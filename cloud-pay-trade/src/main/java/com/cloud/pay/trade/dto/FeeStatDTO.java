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
	private BigDecimal orgBenefit;

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
		if(feeAmount == null)
			feeAmount = BigDecimal.ZERO;
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public BigDecimal getOrgBenefit() {
		if(orgBenefit == null)
			orgBenefit = BigDecimal.ZERO;
		return orgBenefit;
	}

	public void setOrgBenefit(BigDecimal orgBenefit) {
		this.orgBenefit = orgBenefit;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeeStatDTO [statDate=");
		builder.append(statDate);
		builder.append(", merchantCode=");
		builder.append(merchantCode);
		builder.append(", merchantName=");
		builder.append(merchantName);
		builder.append(", orgName=");
		builder.append(orgName);
		builder.append(", totalCount=");
		builder.append(totalCount);
		builder.append(", totalAmount=");
		builder.append(totalAmount);
		builder.append(", loaningTotalAmount=");
		builder.append(loaningTotalAmount);
		builder.append(", feeAmount=");
		builder.append(feeAmount);
		builder.append(", orgBenefit=");
		builder.append(orgBenefit);
		builder.append("]");
		return builder.toString();
	}
	
}
