package com.cloud.pay.channel.vo.bohai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloud.pay.common.contants.ChannelContants;

@XmlRootElement(name = ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBT)
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiBatchPayRetryResult extends BohaiCloudTradeErrorResult {

	private static final long serialVersionUID = -673796971220176757L;

	public BohaiBatchPayRetryResult(String rspCode){
		 this.rspCode = rspCode;
	}
}
