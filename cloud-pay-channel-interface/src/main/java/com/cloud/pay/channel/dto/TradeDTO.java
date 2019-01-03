package com.cloud.pay.channel.dto;

import java.math.BigDecimal;

/**
 * 批量交易结果集
 * @author Eric
 */
public class TradeDTO {
   
	private String seqNo; //为考虑其他渠道的可能需要传入订单号，这里定义为字符串
	
	private BigDecimal tradeAmount;
	
	private Integer status; //查询时返回
	
	private String payerAccount;//付款人账号
	
	private String payerName;//付款人账户名
	
	private String payeeName;  //收款人账户名
	
	private String payeeAccount;//收款人账号
	
	private String payeeBankCode; //收款人清算行行号
	
	private String postScript; //附言

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
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

	public String getPayeeBankCode() {
		return payeeBankCode;
	}

	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
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

	public String getPostScript() {
		return postScript;
	}

	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TradeDTO [seqNo=");
		builder.append(seqNo);
		builder.append(", tradeAmount=");
		builder.append(tradeAmount);
		builder.append(", status=");
		builder.append(status);
		builder.append(", payerAccount=");
		builder.append(payerAccount);
		builder.append(", payerName=");
		builder.append(payerName);
		builder.append(", payeeName=");
		builder.append(payeeName);
		builder.append(", payeeAccount=");
		builder.append(payeeAccount);
		builder.append(", payeeBankCode=");
		builder.append(payeeBankCode);
		builder.append(", postScript=");
		builder.append(postScript);
		builder.append("]");
		return builder.toString();
	}
	
}
