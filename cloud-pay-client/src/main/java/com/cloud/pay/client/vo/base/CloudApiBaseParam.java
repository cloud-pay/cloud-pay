package com.cloud.pay.client.vo.base;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 对外接口请求参数基类
 * @author
 */
public class CloudApiBaseParam implements Serializable{

	private static final long serialVersionUID = 1609873337208440456L;
    
	/**
	 * 版本号
	 */
	private String version = "0.0.1";
	
    @NotEmpty(message = "交易类型不能为空")
	private String tradeType;
    
    @NotEmpty(message = "mchCode不能为空")
    private String mchCode;
    
    private String subMchCode;
    
    @NotEmpty(message = "签名信息不能为空")
    private String sign;
    
    //签名方式
    private String signType = "MD5";

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	

	public String getMchCode() {
		return mchCode;
	}

	public void setMchCode(String mchCode) {
		this.mchCode = mchCode;
	}

	public String getSubMchCode() {
		return subMchCode;
	}

	public void setSubMchCode(String subMchCode) {
		this.subMchCode = subMchCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
