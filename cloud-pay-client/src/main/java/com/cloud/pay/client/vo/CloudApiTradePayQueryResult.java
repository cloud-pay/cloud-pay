package com.cloud.pay.client.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.cloud.pay.client.vo.base.CloudApiTradeBaseResult;

/**
 * 单笔代付结果查询响应结果
 * @author THINKPAD
 *
 */
public class CloudApiTradePayQueryResult extends CloudApiTradeBaseResult {

	private static final long serialVersionUID = -7785807680313162010L;

    private String tradeTime;
	
	private BigDecimal tradeAmount;
	
	private String payeeName;
	
	private String payeeBankCard;
	
	private String payeeBankCode;
	
	private Integer status;
	
	private String reconDate;
	
	private Integer reconStatus;
	
	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
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

	public String getReconDate() {
		return reconDate;
	}

	public void setReconDate(String reconDate) {
		this.reconDate = reconDate;
	}

	public Integer getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(Integer reconStatus) {
		this.reconStatus = reconStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
