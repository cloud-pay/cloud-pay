package com.cloud.pay.recon.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReconExceptionBohaiDTO {

    private Integer id;
    
    private Date tradeTime;
    
    private Integer channelId;

    private String orderNo;

    private String payerAccount;

    private String payerName;

    private String payeeAccount;

    private String payeeName;

    private String bankCode;

    private BigDecimal tradeAmount;

    private String tradeStatus;

    private String tradeStatusDesc;

    private String exceptionReason;


    private Date createTime;

    
    public Date getTradeTime() {
		return tradeTime;
	}


	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getExceptionReason() {
		return exceptionReason;
	}


	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Integer getChannelId() {
		return channelId;
	}


	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}


	public String getPayerAccount() {
		return payerAccount;
	}


	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}


	public String getPayerName() {
		return payerName;
	}


	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}


	public String getPayeeAccount() {
		return payeeAccount;
	}


	public void setPayeeAccount(String payeeAccount) {
		this.payeeAccount = payeeAccount;
	}


	public String getPayeeName() {
		return payeeName;
	}


	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}


	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}


	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}


	public String getTradeStatus() {
		return tradeStatus;
	}


	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}


	public String getTradeStatusDesc() {
		return tradeStatusDesc;
	}


	public void setTradeStatusDesc(String tradeStatusDesc) {
		this.tradeStatusDesc = tradeStatusDesc;
	}


	@Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
