package com.cloud.pay.common.exception;

/**
 * 交易错误
 * @author wangy
 */
public class CloudApiException extends RuntimeException {

	private static final long serialVersionUID = 1613586855176212949L;
	/**
     * 错误代码
     */
    private String errorCode;
    
    /**
     * @param errorCode 错误代码
     * @param message   错误信息
     */
    public CloudApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CloudApiException(String errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
