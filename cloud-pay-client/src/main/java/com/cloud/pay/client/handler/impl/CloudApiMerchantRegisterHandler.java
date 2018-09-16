package com.cloud.pay.client.handler.impl;

import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiMerchantRegisterParam;
import com.cloud.pay.client.vo.CloudApiMerchantRegisterResult;

/**
 * 商户信息报备
 * @author THINKPAD
 *
 */
@Service("cloudMerchantRegisterHandler")
public class CloudApiMerchantRegisterHandler 
           implements ICloudPayApiHandler<CloudApiMerchantRegisterParam, CloudApiMerchantRegisterResult> {

	@Override
	public CloudApiMerchantRegisterResult handle(CloudApiMerchantRegisterParam reqParam) {
		
		return null;
	}

	@Override
	public Class<CloudApiMerchantRegisterParam> getReqParamType() {
		return CloudApiMerchantRegisterParam.class;
	}

}
