package com.cloud.pay.channel.vo;

import java.math.BigDecimal;

/**
 * 批量交易实体
 * @author Eric
 */
public class BatchPayTradeInnerReqVO extends BatchPayTradeReqVO {

	private static final long serialVersionUID = 2961087283562752882L;

	private Long totalNum;
	
	private BigDecimal totalAmt;
	
	private String fileName;

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
