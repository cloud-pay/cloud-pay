package com.cloud.pay.channel.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 渠道操作父类（渠道内部使用）
 * @author wangy
 */
public class CloudTradeParam implements Serializable{

	private static final long serialVersionUID = 2212140872270944382L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
