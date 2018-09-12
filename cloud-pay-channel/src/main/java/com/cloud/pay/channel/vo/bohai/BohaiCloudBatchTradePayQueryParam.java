package com.cloud.pay.channel.vo.bohai;

/**
 * 批量代付结果查询
 * @author THINKPAD
 *
 */
public class BohaiCloudBatchTradePayQueryParam extends BohaiCloudTradeParam {

	private static final long serialVersionUID = -2892159510318089188L;

	private String date;
	
	private String fileNam;
	
	//批量代付原流水
	private String termJnlno;

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
	
}
