package com.cloud.pay.channel.vo.bohai;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.cloud.pay.channel.vo.CloudTradeParam;

/**
 * 渤海请求参数
 * @author wangy
 */
public class BohaiCloudTradeParam extends CloudTradeParam{
   
	private static final long serialVersionUID = 1083624617689736366L;
	
	private static final String version = "1.0.0"; //版本号
	
	private String instId; //机构标识
	
	private String certId; //数字证书标识
	
	private String serialNo; //交易流水
	
	public String getVersion() {
		return version;
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
}
