package com.cloud.pay.channel.vo.bohai;

import java.math.BigDecimal;

/**
 * 渤海单笔银联代付请求参数
 * @author wangy
 */
public class BohaiCloudUnionTradeParam extends BohaiCloudTradeParam {
  
	private static final long serialVersionUID = 1385951608174606946L;

	private String date; //代付交易日期
	
	private String pyrAct; //付款人账号
	
	private String pyrNam; //付款人账户名
	
	private String actNo; //电子账户
	
	private String actNam; //电子账户名称
	
	private String pyeAct; //收款人账号
	
	private String pyeNam; //收款人账户名
	
	private BigDecimal amt; //交易金额
	
	private String idType; //证件类型
 	
	private String idNo; //证件号码
	
	private String postscript; //附言

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPyrAct() {
		return pyrAct;
	}

	public void setPyrAct(String pyrAct) {
		this.pyrAct = pyrAct;
	}

	public String getPyrNam() {
		return pyrNam;
	}

	public void setPyrNam(String pyrNam) {
		this.pyrNam = pyrNam;
	}

	public String getActNo() {
		return actNo;
	}

	public void setActNo(String actNo) {
		this.actNo = actNo;
	}

	public String getActNam() {
		return actNam;
	}

	public void setActNam(String actNam) {
		this.actNam = actNam;
	}

	public String getPyeAct() {
		return pyeAct;
	}

	public void setPyeAct(String pyeAct) {
		this.pyeAct = pyeAct;
	}

	public String getPyeNam() {
		return pyeNam;
	}

	public void setPyeNam(String pyeNam) {
		this.pyeNam = pyeNam;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
}
