package com.cloud.pay.client.vo;

import java.math.BigDecimal;

import com.cloud.pay.client.vo.base.CloudApiBaseParam;
import com.cloud.pay.common.utils.validation.IntegerValue;

/**
 * 商户信息修改
 * @author 
 */
public class CloudApiMerchantUpdateParam extends CloudApiBaseParam {

	private static final long serialVersionUID = 4079400229290805433L;
	
	private String name;  
	
	private String shortName;
	
	@IntegerValue(value= {4,5},message="不存在商户类型")
	private Integer type;
	
	private String industryCategory;
	
	private String legal;
	
	private Integer provincial;
	
	private Integer city;
	
	private String address;
	
	private String email;
	
	private String pMobile;
	
    //结算信息
	private String bankName;
	
	private String bankCode;
	
	private String bankCardNo;
	
	@IntegerValue(value= {1,2},message="不存在账户类型")
	private Integer bankAccountType;
	
	private String bankAccountName;
	
	@IntegerValue(value= {1},message="证件类型暂时只支持身份证")
	private Integer certType;
	
	private String certNo;
	
	private String sMobileNo;
	
	//费率信息
	@IntegerValue(value= {1,2},message="不支持的代付费率类型")
	private Integer payFeeType;
	
	private BigDecimal payFee;
	
	@IntegerValue(value= {1,2},message="不支持的垫资费率类型")
	private Integer loanFeeType;

	private BigDecimal loanFee;
	
	//营业执照
	private String businessFilePath;
	
	//身份证
	private String certFilePath;
	
	//银行卡
	private String bankCardFilePath;
	
	//协议文件
	private String protocolFilePath;

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

	public Integer getProvincial() {
		return provincial;
	}

	public void setProvincial(Integer provincial) {
		this.provincial = provincial;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
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

	public String getBusinessFilePath() {
		return businessFilePath;
	}

	public void setBusinessFilePath(String businessFilePath) {
		this.businessFilePath = businessFilePath;
	}

	public String getCertFilePath() {
		return certFilePath;
	}

	public void setCertFilePath(String certFilePath) {
		this.certFilePath = certFilePath;
	}

	public String getBankCardFilePath() {
		return bankCardFilePath;
	}

	public void setBankCardFilePath(String bankCardFilePath) {
		this.bankCardFilePath = bankCardFilePath;
	}

	public String getProtocolFilePath() {
		return protocolFilePath;
	}

	public void setProtocolFilePath(String protocolFilePath) {
		this.protocolFilePath = protocolFilePath;
	}
	
	
}
