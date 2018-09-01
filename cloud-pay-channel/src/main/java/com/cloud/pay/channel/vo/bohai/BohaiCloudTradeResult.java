package com.cloud.pay.channel.vo.bohai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloud.pay.channel.vo.CloudTradeResult;

/**
 *  渤海银行响应报文
 * @author wangy
 */
@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.FIELD)
public class BohaiCloudTradeResult extends CloudTradeResult{

	private static final long serialVersionUID = -674524132223694022L;
	
	@XmlElement
    private String version = "1.0.0"; //版本号
	
	@XmlElement
	private String instId; //机构标识
	
	@XmlElement
	private String certId; //数字证书标识
	
	@XmlElement
	private String serialNo; //交易流水
	
	@XmlElement
	protected String rspCode;//响应码
	
	@XmlElement
	protected String rspMsg; //响应消息

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

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
	
}
