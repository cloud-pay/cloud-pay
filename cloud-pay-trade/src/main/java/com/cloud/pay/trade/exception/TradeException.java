package com.cloud.pay.trade.exception;

/**
 * 交易异常
 * @author dbnaxlc
 *
 */
public class TradeException extends RuntimeException {

	private static final long serialVersionUID = -2538437452138756315L;
	
	private String exCode;
	
	public TradeException(String message, String exCode) {
		super(message);
		this.exCode = exCode;
	}

	public String getExCode() {
		return exCode;
	}
	
}
