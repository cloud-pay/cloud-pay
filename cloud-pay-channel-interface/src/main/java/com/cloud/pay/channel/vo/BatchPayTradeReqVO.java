package com.cloud.pay.channel.vo;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.cloud.pay.common.utils.validation.DateValue;

/**
 * 批量代付触发请求
 * @author 
 */
public class BatchPayTradeReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = 543414386150514517L;
	
	@NotBlank(message = "交易日期不能为空")
	@Length(max = 17,message = "交易日期最长17位")
	@DateValue(format="yyyyMMdd HH:mm:ss")
	private String tradeDate;

//	@NotBlank(message = "付款人账号不能为空")
//	@Length(max = 32,message = "付款人账号最长32位")
//	private String payerAccount; //付款人账号
//	
//	@NotBlank(message = "付款人账户名不能为空")
//	@Length(max = 120,message = "付款人账户名最长120位")
//	private String payerName; //付款人账户名
	
	@NotNull(message = "总笔数不能为空")
	private Long totalNum;
	
	@NotNull(message = "总金额不能为空")
	@Digits(integer=18,fraction=2)
	private BigDecimal totalAmt;
	
	@NotBlank(message = "文件名不能为空")
	@Length(max = 32,message = "文件名最长32位")
	private String fileName;
	

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

//	public String getPayerAccount() {
//		return payerAccount;
//	}
//
//	public void setPayerAccount(String payerAccount) {
//		this.payerAccount = payerAccount;
//	}
//
//	public String getPayerName() {
//		return payerName;
//	}
//
//	public void setPayerName(String payerName) {
//		this.payerName = payerName;
//	}

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
//
//	public String getFileSHA1() {
//		return fileSHA1;
//	}
//
//	public void setFileSHA1(String fileSHA1) {
//		this.fileSHA1 = fileSHA1;
//	} 
	
	
	
}
