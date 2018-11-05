package com.cloud.pay.merchant.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantPrepayInfoDTO {
	private Integer id;

	private Integer merchantId;
	
	private String merchantName;

	private BigDecimal balance;

	private BigDecimal freezeAmount;

	private Integer overdraw;

	private String digest;

	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}

	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}

	public Integer getOverdraw() {
		return overdraw;
	}

	public void setOverdraw(Integer overdraw) {
		this.overdraw = overdraw;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MerchantPrepayInfoDTO [id=");
		builder.append(id);
		builder.append(", merchantId=");
		builder.append(merchantId);
		builder.append(", merchantName=");
		builder.append(merchantName);
		builder.append(", balance=");
		builder.append(balance);
		builder.append(", freezeAmount=");
		builder.append(freezeAmount);
		builder.append(", overdraw=");
		builder.append(overdraw);
		builder.append(", digest=");
		builder.append(digest);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append("]");
		return builder.toString();
	}

}