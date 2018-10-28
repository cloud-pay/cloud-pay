package com.cloud.pay.channel.vo;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.cloud.pay.common.utils.validation.DateValue;

/**
 * 代付渠道请求接口请求参数
 * @author wangy
 */
public class PayTradeReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = -8713903284380287558L;
	
	@NotBlank(message = "交易时间不能为空")
	@Length(max = 17,message = "交易时间最长17位")
	@DateValue(format="yyyyMMdd HH:mm:ss")
	private String tradeDate;

	@NotBlank(message = "付款人账号不能为空")
	@Length(max = 32,message = "付款人账号最长32位")
	private String payerAccount; //付款人账号
	
	@NotBlank(message = "付款人账户名不能为空")
	@Length(max = 120,message = "付款人账户名最长120位")
	private String payerName; //付款人账户名

	@Length(max = 32,message = "电子账户最长32位")
	private String accountNo; //电子账户
	
	@Length(max = 120,message = "电子账户名称最长120位")
	private String accountName; //电子账户名称
	
	@NotBlank(message = "收款人账号不能为空")
	@Length(max = 32,message = "收款人账号最长32位")
	private String payeeAccount; //收款人账号
	
	@NotBlank(message = "收款人账号名不能为空")
	@Length(max = 120,message = "收款人账户名最长120位")
	private String payeeName; //收款人账户名
	
	@NotBlank(message = "收款人清算行行号不能为空")
	@Length(max = 14,message = "收款人账户名最长14位")
	private String payeeBankCode;  //收款人清算行行号
	
	@NotNull(message = "交易金额不能为空")
	@Digits(integer=18,fraction=2)
	private BigDecimal amt; //交易金额
	
	private String postscript; //附言
	

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

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

	public String getPayeeBankCode() {
		return payeeBankCode;
	}

	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	
}
