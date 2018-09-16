package com.cloud.pay.channel.vo.bohai;

/**
 * 批量单笔结果查询
 * @author 
 */
public class BohaiBatchSingleQueryParam extends BohaiCloudTradeParam {

	private static final long serialVersionUID = 2389070119524352248L;
   
	private String date;
	
	private String fileNam;
	
	private String termJnlno;
	
	private String seqNo;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFileNam() {
		return fileNam;
	}

	public void setFileNam(String fileNam) {
		this.fileNam = fileNam;
	}

	public String getTermJnlno() {
		return termJnlno;
	}

	public void setTermJnlno(String termJnlno) {
		this.termJnlno = termJnlno;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	
}
