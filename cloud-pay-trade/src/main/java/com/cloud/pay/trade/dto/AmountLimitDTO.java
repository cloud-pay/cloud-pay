package com.cloud.pay.trade.dto;

import java.math.BigDecimal;
import java.util.Date;

public class AmountLimitDTO {
	private Integer id;

	private Integer type;

	private String orgName;

	private String merchantName;

	private Integer period;

	private BigDecimal amountLimit;

	private String modifer;

	private Date modifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public BigDecimal getAmountLimit() {
		return amountLimit;
	}

	public void setAmountLimit(BigDecimal amountLimit) {
		this.amountLimit = amountLimit;
	}

	public String getModifer() {
		return modifer;
	}

	public void setModifer(String modifer) {
		this.modifer = modifer;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AmountLimitDTO [id=");
		builder.append(id);
		builder.append(", type=");
		builder.append(type);
		builder.append(", orgName=");
		builder.append(orgName);
		builder.append(", merchantName=");
		builder.append(merchantName);
		builder.append(", period=");
		builder.append(period);
		builder.append(", amountLimit=");
		builder.append(amountLimit);
		builder.append(", modifer=");
		builder.append(modifer);
		builder.append(", modifyTime=");
		builder.append(modifyTime);
		builder.append("]");
		return builder.toString();
	}

}