package com.cloud.pay.channel.vo;

import java.math.BigDecimal;

/**
 * 代付渠道请求接口请求参数
 * @author wangy
 */
public class PayTradeReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = -8713903284380287558L;

	private String pyrAct; //付款人账号
	
	private String pyrNam; //付款人账户名
	
	private String actNo; //电子账户
	
	private String actNam; //电子账户名称
	
	private String pyeAct; //收款人账号
	
	private String pyeNam; //收款人账户名
	
	private String pyeBnk;  //收款人清算行行号
	
	private BigDecimal amt; //交易金额
	
	private String postscript; //附言

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

	public String getPyeBnk() {
		return pyeBnk;
	}

	public void setPyeBnk(String pyeBnk) {
		this.pyeBnk = pyeBnk;
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
