package com.cloud.pay.client.handler.impl;

import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiMerchantUpdateParam;
import com.cloud.pay.client.vo.CloudApiMerchantUpdateResult;

/**
 * 商户信息修改
 * @author 
 */
@Service("cloudMerchantUpdateHandler")
public class CloudApiMerchantUpdateHandler 
             implements ICloudPayApiHandler<CloudApiMerchantUpdateParam, CloudApiMerchantUpdateResult> {

	@Override
	public CloudApiMerchantUpdateResult handle(CloudApiMerchantUpdateParam reqParam) {
		
		return null;
	}

	@Override
	public Class<CloudApiMerchantUpdateParam> getReqParamType() {
		return CloudApiMerchantUpdateParam.class;
	}

}
