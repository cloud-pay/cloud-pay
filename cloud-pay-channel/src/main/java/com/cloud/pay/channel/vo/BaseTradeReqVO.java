package com.cloud.pay.channel.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 渠道请求实体基类
 * @author wangy
 */
public class BaseTradeReqVO implements Serializable{

	private static final long serialVersionUID = 5148292360420264520L;
     
	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
