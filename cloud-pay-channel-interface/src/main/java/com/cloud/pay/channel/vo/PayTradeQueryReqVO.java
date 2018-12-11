package com.cloud.pay.channel.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.cloud.pay.common.utils.validation.DateValue;

/**
 * 单笔代收付交易查询
 * @author wangy
 */
public class PayTradeQueryReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = -8285637793404662543L;

//	@NotNull(message = "交易渠道不能为空")
	private Integer channelId; //交易渠道
	
	@NotBlank(message = "交易时间不能为空")
	@Length(max = 17,message = "交易时间最长17位")
	@DateValue(format="yyyyMMdd HH:mm:ss")
	private String tradeDate;;//交易日期（格式：YYYYMMDD HH:MM:SS）
	

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
}
