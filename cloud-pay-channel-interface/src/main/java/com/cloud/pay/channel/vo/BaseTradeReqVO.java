package com.cloud.pay.channel.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 渠道请求实体基类
 * @author wangy
 */
public class BaseTradeReqVO implements Serializable{

	private static final long serialVersionUID = 5148292360420264520L;
	
	@NotBlank(message = "平台商户号不能为空")
	private String merchantNo;//平台商户号
	
	@NotBlank(message = "平台订单号不能为空")
	private String orderNo; //平台订单号
	
     
	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
