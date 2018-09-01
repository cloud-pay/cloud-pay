package com.cloud.pay.trade.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantRouteDTO {

	private Integer id;
	
	private Integer type;

    private Integer channelId;
    
    private String channelName;

    private Integer merchantId;
    
    private String merchantName;

    private Integer loaning;

    private Integer loaningOrgId;
    
    private String loaningOrgName;

    private BigDecimal loaningAmount;

    private Integer status;

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

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public Integer getLoaning() {
		return loaning;
	}

	public void setLoaning(Integer loaning) {
		this.loaning = loaning;
	}

	public Integer getLoaningOrgId() {
		return loaningOrgId;
	}

	public void setLoaningOrgId(Integer loaningOrgId) {
		this.loaningOrgId = loaningOrgId;
	}

	public String getLoaningOrgName() {
		return loaningOrgName;
	}

	public void setLoaningOrgName(String loaningOrgName) {
		this.loaningOrgName = loaningOrgName;
	}

	public BigDecimal getLoaningAmount() {
		return loaningAmount;
	}

	public void setLoaningAmount(BigDecimal loaningAmount) {
		this.loaningAmount = loaningAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
		builder.append("MerchantRouteDTO [id=");
		builder.append(id);
		builder.append(", type=");
		builder.append(type);
		builder.append(", channelId=");
		builder.append(channelId);
		builder.append(", channelName=");
		builder.append(channelName);
		builder.append(", merchantId=");
		builder.append(merchantId);
		builder.append(", merchantName=");
		builder.append(merchantName);
		builder.append(", loaning=");
		builder.append(loaning);
		builder.append(", loaningOrgId=");
		builder.append(loaningOrgId);
		builder.append(", loaningOrgName=");
		builder.append(loaningOrgName);
		builder.append(", loaningAmount=");
		builder.append(loaningAmount);
		builder.append(", status=");
		builder.append(status);
		builder.append(", modifer=");
		builder.append(modifer);
		builder.append(", modifyTime=");
		builder.append(modifyTime);
		builder.append("]");
		return builder.toString();
	}
    
    
}
