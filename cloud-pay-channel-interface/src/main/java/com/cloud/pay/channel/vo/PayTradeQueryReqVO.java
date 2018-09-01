package com.cloud.pay.channel.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 单笔代收付交易查询
 * @author wangy
 */
public class PayTradeQueryReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = -8285637793404662543L;

	@NotBlank(message = "交易渠道不能为空")
	private String channelCode; //交易渠道
	
	@NotBlank(message = "交易日期不能为空")
	@Length(max=17)
	private String date;//交易日期（格式：YYYYMMDD HH:MM:SS）
	

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
