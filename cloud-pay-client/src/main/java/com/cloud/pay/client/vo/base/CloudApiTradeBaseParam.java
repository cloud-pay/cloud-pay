package com.cloud.pay.client.vo.base;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 交易请求参数基类
 * @author wangy
 */
public class CloudApiTradeBaseParam extends CloudApiBaseParam {
   
	private static final long serialVersionUID = 1493344623278429409L;
	
	@NotEmpty(message = "交易流水号不能为空")
	@Length(max = 32,message = "交易流水号最长32位")
	private String orderNo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
