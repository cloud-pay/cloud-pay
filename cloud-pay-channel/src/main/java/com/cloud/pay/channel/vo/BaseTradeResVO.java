package com.cloud.pay.channel.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 渠道响应参数基类
 * @author wangy
 */
public class BaseTradeResVO implements Serializable{

	private static final long serialVersionUID = -7869992528965033473L;
	
	private String merchantNo;//平台商户号
	
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
