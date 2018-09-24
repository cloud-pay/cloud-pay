package com.cloud.pay.channel.vo;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 *  批量代付重新触发
 * @author 
 */
public class BatchPayRetryReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = 2763448842312436700L;

	@NotBlank(message = "交易日期不能为空")
	@Length(max = 17,message = "交易日期最长17位")
	private String tradeDate;
	  
	@NotBlank(message = "付款人账号不能为空")
	@Length(max = 32,message = "付款人账号最长32位")
	private String payerAccount;
	
	@NotBlank(message = "付款人账户名不能为空")
	@Length(max = 120,message = "付款人账户名最长120位")
	private String payerName;
	
	@NotNull
	@Length(max = 6,message = "总比数最长6位")
	private Long totalNum;
	
	@NotNull(message = "总金额不能为空")
	@Digits(integer=18,fraction=2)
	private BigDecimal totalAmt;
	
	private String fileName;
	
	@NotNull(message = "渠道ID不能为空")
	private Integer channelId;

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
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

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	
	
}
