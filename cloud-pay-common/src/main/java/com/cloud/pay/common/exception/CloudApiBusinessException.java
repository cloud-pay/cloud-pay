package com.cloud.pay.common.exception;

/**
 * 业务类错误
 * @author 
 */
public class CloudApiBusinessException extends CloudApiException {

	private static final long serialVersionUID = 3008900876489627228L;

	public CloudApiBusinessException(String errorCode, String message) {
		super(errorCode, message);
	}
	
	public CloudApiBusinessException(String errorCode, String message, Throwable throwable) {
		super(errorCode, message, throwable);
	}
}
