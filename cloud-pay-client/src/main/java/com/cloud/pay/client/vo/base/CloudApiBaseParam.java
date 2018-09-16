package com.cloud.pay.client.vo.base;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cloud.pay.client.utils.GroupV1;
import com.cloud.pay.client.utils.GroupV2;

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
    
    @NotEmpty(message = "机构编码不能为空",groups = GroupV1.class)
    private String orgCode;
    
    @NotEmpty(message = "商户编码不能为空",groups = GroupV2.class)
    private String merchantCode;
    
    @NotEmpty(message = "交易流水号不能为空")
    @Length(max = 32,message = "交易流水号最长32位")
    private String orderNo;
    
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
