package com.cloud.pay.channel.vo;

import java.util.List;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import com.cloud.pay.channel.dto.TradeDTO;
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
	
	private List<TradeDTO> trades;

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public List<TradeDTO> getTrades() {
		return trades;
	}

	public void setTrades(List<TradeDTO> trades) {
		this.trades = trades;
	}
	
	
}
