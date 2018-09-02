package com.cloud.pay.common.exception;

public class CloudPayException extends RuntimeException {

	private static final long serialVersionUID = 1661905940180955807L;
   
	public CloudPayException( String message) {
	   super(message);
	}
}
