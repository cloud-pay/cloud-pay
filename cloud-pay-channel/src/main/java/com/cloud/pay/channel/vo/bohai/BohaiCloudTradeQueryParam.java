package com.cloud.pay.channel.vo.bohai;

/**
 * 单笔代收付交易查询
 * @author wangy
 */
public class BohaiCloudTradeQueryParam extends BohaiCloudTradeParam {

	private static final long serialVersionUID = -3405249898881824123L;
	
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
