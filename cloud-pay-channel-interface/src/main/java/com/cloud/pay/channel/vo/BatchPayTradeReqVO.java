package com.cloud.pay.channel.vo;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.cloud.pay.common.utils.validation.DateValue;

/**
 * 批量代付触发请求
 * @author 
 */
public class BatchPayTradeReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = 543414386150514517L;
	
	@NotBlank(message = "交易日期不能为空")
	@Length(max = 17,message = "交易日期最长17位")
	@DateValue(format="yyyyMMdd HH:mm:ss")
	private String tradeDate;
	

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	
}
