package com.cloud.pay.channel.vo.bohai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloud.pay.common.contants.ChannelContants;

/**
 * 批量单笔查询
 * @author 
 */
@XmlRootElement(name = ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBS)
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiBatchSingleQueryResult extends BohaiCloudTradeErrorResult {

	private static final long serialVersionUID = -2491846764486969681L;
	
	public BohaiBatchSingleQueryResult(String rspCode) {
		 this.rspCode = rspCode;
	}
}
