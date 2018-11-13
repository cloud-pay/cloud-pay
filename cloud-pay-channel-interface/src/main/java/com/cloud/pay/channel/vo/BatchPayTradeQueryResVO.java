package com.cloud.pay.channel.vo;

import java.util.List;

import com.cloud.pay.channel.dto.TradeDTO;

public class BatchPayTradeQueryResVO extends BaseTradeResVO{

	private static final long serialVersionUID = -6454477994173419881L;
	
	private Integer status; //0-成功，1-失败，99-未知状态
	
	private Integer channelId;
	
	//批量代付结果集
	private List<TradeDTO> trades;
	
	public BatchPayTradeQueryResVO(String errorCode,String errorMessage) {
		super(errorCode, errorMessage);
	}
   
	public BatchPayTradeQueryResVO(Integer merchantId,String orderNo,String respCode,String respMsg){
		super(merchantId, orderNo, respCode, respMsg);
	}
	
	public BatchPayTradeQueryResVO(Integer merchantId,String orderNo,String respCode,String errorCode,String errorMessage){
		super(merchantId, orderNo, respCode, errorCode, errorMessage);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<TradeDTO> getTrades() {
		return trades;
	}

	public void setTrades(List<TradeDTO> trades) {
		this.trades = trades;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	
}
