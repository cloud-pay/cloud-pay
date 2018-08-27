package com.cloud.pay.channel.vo.bohai;

/**
 * 渤海单笔实时代付响应结果
 * @author wangy
 */
public class BohaiCloudTradePayResult extends BohaiCloudTradeResult {

	private static final long serialVersionUID = 5026232140980716389L;
   
	private String actDat;//银行会计日期
	
	private String rspCode;//响应码
	
	private String rspMsg;//响应信息

	public String getActDat() {
		return actDat;
	}

	public void setActDat(String actDat) {
		this.actDat = actDat;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
	
	
}
