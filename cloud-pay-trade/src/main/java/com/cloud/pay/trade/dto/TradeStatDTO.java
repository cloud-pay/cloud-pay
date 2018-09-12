package com.cloud.pay.trade.dto;

import java.math.BigDecimal;

public class TradeStatDTO {

	private Integer totalCount;
	
	private BigDecimal totalAmount;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
}
