package com.cloud.pay.channel.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 渠道响应参数基类
 * @author wangy
 */
public class BaseTradeResVo implements Serializable{

	private static final long serialVersionUID = -7869992528965033473L;

	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
