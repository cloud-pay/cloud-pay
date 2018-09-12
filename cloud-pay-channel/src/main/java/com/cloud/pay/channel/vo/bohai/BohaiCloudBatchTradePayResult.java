package com.cloud.pay.channel.vo.bohai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloud.pay.common.contants.ChannelContants;

@XmlRootElement(name = ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBP)
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiCloudBatchTradePayResult extends BohaiCloudTradeErrorResult {

	private static final long serialVersionUID = -6882466076101582239L;

}
