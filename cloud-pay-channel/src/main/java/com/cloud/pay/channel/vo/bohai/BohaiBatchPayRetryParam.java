package com.cloud.pay.channel.vo.bohai;

import java.math.BigDecimal;

/**
 * 批量代付重新触发
 * @author 
 */
public class BohaiBatchPayRetryParam extends BohaiCloudTradeParam {

	private static final long serialVersionUID = -9161515362370048346L;

	private String date;
	
	private String pyrAct;
	
	private String pyrNam;
	
	private Long totNum;
	
	private BigDecimal totAmt;
	
	private String fileNam;
	
	private String fileSHA1;

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

	public Long getTotNum() {
		return totNum;
	}

	public void setTotNum(Long totNum) {
		this.totNum = totNum;
	}

	public BigDecimal getTotAmt() {
		return totAmt;
	}

	public void setTotAmt(BigDecimal totAmt) {
		this.totAmt = totAmt;
	}

	public String getFileNam() {
		return fileNam;
	}

	public void setFileNam(String fileNam) {
		this.fileNam = fileNam;
	}

	public String getFileSHA1() {
		return fileSHA1;
	}

	public void setFileSHA1(String fileSHA1) {
		this.fileSHA1 = fileSHA1;
	}
}
