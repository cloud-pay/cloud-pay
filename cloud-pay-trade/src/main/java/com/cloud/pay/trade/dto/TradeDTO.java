package com.cloud.pay.trade.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TradeDTO {
  
	    private Integer id;

	    private String orderNo;

	    private Integer merchantId;

	    private Date tradeTime;

	    private BigDecimal tradeAmount;

	    private Integer status;

	    private Integer channelId;

	    private String returnCode;

	    private String returnInfo;

	    private Integer payerId;

	    private String payeeName;

	    private String payeeBankCard;

	    private String payeeBankCode;

	    private Date tradeConfirmTime;

	    private String remark;

	    private Integer settleStatus;

	    private Date reconDate;

	    private Integer reconStatus;
	    
	    private String payerBankCard;
	    
	    private String payerName;
	    
	    private String platOrderNo;

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

		public Integer getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(Integer merchantId) {
			this.merchantId = merchantId;
		}

		public Date getTradeTime() {
			return tradeTime;
		}

		public void setTradeTime(Date tradeTime) {
			this.tradeTime = tradeTime;
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

		public Integer getChannelId() {
			return channelId;
		}

		public void setChannelId(Integer channelId) {
			this.channelId = channelId;
		}

		public String getReturnCode() {
			return returnCode;
		}

		public void setReturnCode(String returnCode) {
			this.returnCode = returnCode;
		}

		public String getReturnInfo() {
			return returnInfo;
		}

		public void setReturnInfo(String returnInfo) {
			this.returnInfo = returnInfo;
		}

		public Integer getPayerId() {
			return payerId;
		}

		public void setPayerId(Integer payerId) {
			this.payerId = payerId;
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

		public Date getTradeConfirmTime() {
			return tradeConfirmTime;
		}

		public void setTradeConfirmTime(Date tradeConfirmTime) {
			this.tradeConfirmTime = tradeConfirmTime;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public Integer getSettleStatus() {
			return settleStatus;
		}

		public void setSettleStatus(Integer settleStatus) {
			this.settleStatus = settleStatus;
		}

		public Date getReconDate() {
			return reconDate;
		}

		public void setReconDate(Date reconDate) {
			this.reconDate = reconDate;
		}

		public Integer getReconStatus() {
			return reconStatus;
		}

		public void setReconStatus(Integer reconStatus) {
			this.reconStatus = reconStatus;
		}

		public String getPayerBankCard() {
			return payerBankCard;
		}

		public void setPayerBankCard(String payerBankCard) {
			this.payerBankCard = payerBankCard;
		}

		public String getPayerName() {
			return payerName;
		}

		public void setPayerName(String payerName) {
			this.payerName = payerName;
		}

		public String getPlatOrderNo() {
			return platOrderNo;
		}

		public void setPlatOrderNo(String platOrderNo) {
			this.platOrderNo = platOrderNo;
		}
		
}
