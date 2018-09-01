package com.cloud.pay.channel.vo.bohai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloud.pay.common.contants.ChannelContants;

/**
 * 单笔银联代付响应结果
 * @author wangy
 */
@XmlRootElement(name = ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCUR)
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiCloudUnionTradeResult extends BohaiCloudTradeErrorResult {

	private static final long serialVersionUID = 3903218812611919161L;
   
	@XmlElement
	private String actDat;//银行会计日期
	
	public BohaiCloudUnionTradeResult() {
		
	}
	
	public BohaiCloudUnionTradeResult(String rspCode) {
		this.rspCode = rspCode;
	}
	
	public BohaiCloudUnionTradeResult(String rspCode,String rspMsg) {
		this.rspCode = rspCode;
		this.rspMsg = rspMsg;
	}
  
	public BohaiCloudUnionTradeResult(String rspCode,String errorCode,String errorMessage) {
		this.rspCode = rspCode;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getActDat() {
		return actDat;
	}

	public void setActDat(String actDat) {
		this.actDat = actDat;
	}
}
