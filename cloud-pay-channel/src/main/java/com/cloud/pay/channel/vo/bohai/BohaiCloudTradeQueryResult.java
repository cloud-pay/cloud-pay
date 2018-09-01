package com.cloud.pay.channel.vo.bohai;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloud.pay.common.contants.ChannelContants;

/**
 * 代付交易结果查询
 * @author wangy
 * 
 */
@XmlRootElement(name = ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCTQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiCloudTradeQueryResult  extends BohaiCloudTradeErrorResult{

	private static final long serialVersionUID = 8186531723963341795L;
	
	@XmlElement
    private String pyrAct; //付款人账号
	
    @XmlElement
	private String pyrNam; //付款人账户名
	
	@XmlElement
	private String actNo; //电子账户
	
	@XmlElement
	private String actNam; //电子账户名称
	
	@XmlElement
	private String pyeAct; //收款人账号
	
	@XmlElement
	private String pyeNam; //收款人账户名
	
	@XmlElement
	private String pyeBnk; //收款人清算行行号
	
	@XmlElement
	private BigDecimal amt; //交易金额
	
	@XmlElement
	private String postscript; //附言
	
	@XmlElement
	private String actDat;//银行会计日期
	
	public BohaiCloudTradeQueryResult() {}
	
	public BohaiCloudTradeQueryResult(String rspCode) {
		 this.rspCode = rspCode;
	}

	public BohaiCloudTradeQueryResult(String rspCode,String errorCode,String errorMessage) {
		this.rspCode = rspCode;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
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

	public String getActDat() {
		return actDat;
	}

	public void setActDat(String actDat) {
		this.actDat = actDat;
	}
	
}
