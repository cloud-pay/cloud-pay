package com.cloud.pay.client.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayParam;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayResult;

/**
 * 批量代付
 * @author THINKPAD
 *
 */
@Service("cloudBatchPayHandler")
public class CloudApiTradeBatchPayHandler implements ICloudPayApiHandler<CloudApiTradeBatchPayParam, CloudApiTradeBatchPayResult> {
	
	private Logger log = LoggerFactory.getLogger(CloudApiTradeBatchPayHandler.class);

	@Override
	public CloudApiTradeBatchPayResult handle(CloudApiTradeBatchPayParam reqParam) {
		log.info("批量代付，请求参数：{}",reqParam);
		CloudApiTradeBatchPayResult result = new CloudApiTradeBatchPayResult();
		
		log.info("批量代付，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradeBatchPayParam> getReqParamType() {
		return CloudApiTradeBatchPayParam.class;
	}
}
