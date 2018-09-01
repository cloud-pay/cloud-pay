package com.cloud.pay.channel.vo.bohai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 渤海错误响应报文
 * @author wangy
 */
@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiCloudTradeErrorResult extends BohaiCloudTradeResult{
  
	private static final long serialVersionUID = -3550272593592884211L;

	@XmlElement
	protected String errorCode;  //错误代码
	
	@XmlElement
	protected String errorMessage;  //错误描述

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
