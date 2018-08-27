package com.cloud.pay.channel.vo.bohai;

import com.cloud.pay.channel.vo.CloudTradeResult;

/**
 *  渤海银行响应报文
 * @author wangy
 */
public class BohaiCloudTradeResult extends CloudTradeResult{

	private static final long serialVersionUID = -674524132223694022L;
	
    private String version = "1.0.0"; //版本号
	
	private String instId; //机构标识
	
	private String certId; //数字证书标识
	
	private String serialNo; //交易流水
	
	private String errorCode;  //错误代码
	
	private String errorMessage;  //错误描述

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

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
