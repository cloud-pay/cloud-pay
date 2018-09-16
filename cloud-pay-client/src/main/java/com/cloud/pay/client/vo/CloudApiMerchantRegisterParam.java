package com.cloud.pay.client.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.cloud.pay.client.vo.base.CloudApiBaseParam;

/**
 * 商户报备
 * @author 
 */
public class CloudApiMerchantRegisterParam extends CloudApiBaseParam{

	private static final long serialVersionUID = 3915329668111980055L;
    
	@NotEmpty(message = "商户名称不能为空")
	private String name;  
	
	@NotEmpty(message = "商户简称不能为空")
	private String shortName;
	
	@NotNull(message = "商户类型不能为空")
	private Integer type;
	
	@NotEmpty(message = "行业类别不能为空")
	private String industryCategory;
	
	@NotEmpty(message = "负责人不能为空")
	private String legal;
	
	@NotEmpty(message = "所在城市不能为空")
	private String city;
	
	@NotEmpty(message = "详细地址不能为空")
	private String address;
	
	@NotEmpty(message = "邮箱不能为空")
	private String email;
	
	@NotEmpty(message = "负责人手机不能为空")
	private String pMobile;
	
    //结算信息
	@NotEmpty(message = "开户银行不能为空")
	private String bankName;
	
	@NotEmpty(message = "联行号不能为空")
	private String bankCode;
	
	@NotEmpty(message = "银行卡号不能为空")
	private String bankCardNo;
	
	@NotNull(message = "账户类型不能为空")
	private Integer bankAccountType;
	
	@NotEmpty(message = "户名不能为空")
	private String bankAccountName;
	
	@NotNull(message = "证件类型不能为空")
	private Integer certType;
	
	@NotEmpty(message = "证件号码不能为空")
	private String certNo;
	
	@NotEmpty(message = "手机号码不能为空")
	private String sMobileNo;
	
	//费率信息
	@NotNull(message = "代付费率类型不能为空")
	private Integer payFeeType;
	
	@NotNull(message = "代付费率不能为空")
	private BigDecimal payFee;
	
	@NotNull(message = "垫资费率类型不能为空")
	private Integer loanFeeType;
	
	@NotNull(message = "垫资费率不能为空")
	private BigDecimal loanFee;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIndustryCategory() {
		return industryCategory;
	}

	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}

	public String getLegal() {
		return legal;
	}

	public void setLegal(String legal) {
		this.legal = legal;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getpMobile() {
		return pMobile;
	}

	public void setpMobile(String pMobile) {
		this.pMobile = pMobile;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public Integer getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(Integer bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public Integer getCertType() {
		return certType;
	}

	public void setCertType(Integer certType) {
		this.certType = certType;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getsMobileNo() {
		return sMobileNo;
	}

	public void setsMobileNo(String sMobileNo) {
		this.sMobileNo = sMobileNo;
	}

	public Integer getPayFeeType() {
		return payFeeType;
	}

	public void setPayFeeType(Integer payFeeType) {
		this.payFeeType = payFeeType;
	}

	public BigDecimal getPayFee() {
		return payFee;
	}

	public void setPayFee(BigDecimal payFee) {
		this.payFee = payFee;
	}

	public Integer getLoanFeeType() {
		return loanFeeType;
	}

	public void setLoanFeeType(Integer loanFeeType) {
		this.loanFeeType = loanFeeType;
	}

	public BigDecimal getLoanFee() {
		return loanFee;
	}

	public void setLoanFee(BigDecimal loanFee) {
		this.loanFee = loanFee;
	}

}
