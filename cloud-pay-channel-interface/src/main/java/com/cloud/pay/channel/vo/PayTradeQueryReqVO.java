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
	
	@NotBlank(message = "交易时间不能为空")
	@Length(max = 17,message = "交易时间最长17位")
	private String tradeDate;;//交易日期（格式：YYYYMMDD HH:MM:SS）
	

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
}
