package com.cloud.pay.channel.dto;

import java.math.BigDecimal;

/**
 * 批量交易结果集
 * @author Eric
 */
public class TradeDTO {
   
	private String orderNo;
	
	private BigDecimal tradeAmount;
	
	private Integer status;
	
	private String payeeName;
	
	private String payeeBankCard;
	
	private String payeeBankCode;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeBankCard() {
		return payeeBankCard;
	}

	public void setPayeeBankCard(String payeeBankCard) {
		this.payeeBankCard = payeeBankCard;
	}

	public String getPayeeBankCode() {
		return payeeBankCode;
	}

	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
	}
	
	
}
