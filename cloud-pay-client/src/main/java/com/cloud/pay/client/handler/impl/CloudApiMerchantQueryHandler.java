package com.cloud.pay.client.handler.impl;

import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiMerchantQueryParam;
import com.cloud.pay.client.vo.CloudApiMerchantQueryResult;

/**
 * 商户信息查询
 * @author 
 */
@Service("cloudMerchantQueryHandler")
public class CloudApiMerchantQueryHandler 
            implements ICloudPayApiHandler<CloudApiMerchantQueryParam, CloudApiMerchantQueryResult> {

	
	@Override
	public CloudApiMerchantQueryResult handle(CloudApiMerchantQueryParam reqParam) {
		
		return null;
	}

	@Override
	public Class<CloudApiMerchantQueryParam> getReqParamType() {
		return CloudApiMerchantQueryParam.class;
	}

}
