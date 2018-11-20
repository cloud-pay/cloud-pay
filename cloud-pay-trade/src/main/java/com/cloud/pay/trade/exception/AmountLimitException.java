package com.cloud.pay.trade.exception;

/**
 * 限额异常
 * @author dbnaxlc
 *
 */
public class AmountLimitException extends RuntimeException {

	private static final long serialVersionUID = -2538437452138756315L;
	
	private String exCode;
	
	public AmountLimitException(String message, String exCode) {
		super(message);
		this.exCode = exCode;
	}

	public String getExCode() {
		return exCode;
	}
	
}
