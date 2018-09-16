package com.cloud.pay.client.vo;

import com.cloud.pay.client.vo.base.CloudApiBaseResult;

/**
 * 商户信息报备响应结果
 * @author
 */
public class CloudApiMerchantRegisterResult extends CloudApiBaseResult {

	private static final long serialVersionUID = 8645952611901638950L;
	
	//商户编码
	private String code;

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

}
