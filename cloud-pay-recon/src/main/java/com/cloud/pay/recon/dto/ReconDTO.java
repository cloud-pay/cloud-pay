package com.cloud.pay.recon.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReconDTO {
  
	 private Integer id;
	 
	 private Date accountDate;
	 
	 private Integer channelId;
	 
	 private Integer tradeTotal;
	 
	 private BigDecimal tradeAmtTotal;
	 
	 private Integer exceptionTotal;
	 
	 private Integer reconStatus;
	 
	 private String failReson;
	  
	 private Date createTime;
	 
	 private String channelName; //渠道名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(Date accountDate) {
		this.accountDate = accountDate;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getTradeTotal() {
		return tradeTotal;
	}

	public void setTradeTotal(Integer tradeTotal) {
		this.tradeTotal = tradeTotal;
	}

	public BigDecimal getTradeAmtTotal() {
		return tradeAmtTotal;
	}

	public void setTradeAmtTotal(BigDecimal tradeAmtTotal) {
		this.tradeAmtTotal = tradeAmtTotal;
	}

	public Integer getExceptionTotal() {
		return exceptionTotal;
	}

	public void setExceptionTotal(Integer exceptionTotal) {
		this.exceptionTotal = exceptionTotal;
	}

	public Integer getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(Integer reconStatus) {
		this.reconStatus = reconStatus;
	}

	public String getFailReson() {
		return failReson;
	}

	public void setFailReson(String failReson) {
		this.failReson = failReson;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	 
	 public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
