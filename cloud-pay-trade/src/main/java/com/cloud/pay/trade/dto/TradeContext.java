package com.cloud.pay.trade.dto;

import com.cloud.pay.trade.entity.Trade;

/**
 * 交易上下文
 * @author dbnaxlc
 * @date 2018年11月6日 下午2:35:54
 */
public class TradeContext {

	private Trade trade;
	
	/** 商户所属机构ID */
	private Integer orgId;
	/** 垫资机构ID */
	private Integer loanId;
	public Trade getTrade() {
		return trade;
	}
	public void setTrade(Trade trade) {
		this.trade = trade;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getLoanId() {
		return loanId;
	}
	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}
	
}
