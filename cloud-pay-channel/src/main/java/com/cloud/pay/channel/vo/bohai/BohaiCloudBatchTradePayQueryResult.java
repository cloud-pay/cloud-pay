package com.cloud.pay.channel.vo.bohai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloud.pay.common.contants.ChannelContants;

@XmlRootElement(name = ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBR)
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiCloudBatchTradePayQueryResult extends BohaiCloudTradeErrorResult {

	private static final long serialVersionUID = -6518157768712019236L;

	public BohaiCloudBatchTradePayQueryResult(String rspCode) {
		this.rspCode = rspCode;
	}
	
	@XmlElement
	private String FilNam;

	public String getFilNam() {
		return FilNam;
	}

	public void setFilNam(String filNam) {
		FilNam = filNam;
	}
	
	
}
