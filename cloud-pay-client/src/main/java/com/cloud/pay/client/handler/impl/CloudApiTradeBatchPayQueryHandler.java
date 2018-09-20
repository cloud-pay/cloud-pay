package com.cloud.pay.client.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayQueryParam;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayQueryResult;

/**
 * 批量代付结果查询
 * @author THINKPAD
 *
 */
@Service("cloudBatchPayQueryHandler")
public class CloudApiTradeBatchPayQueryHandler implements ICloudPayApiHandler<CloudApiTradeBatchPayQueryParam, CloudApiTradeBatchPayQueryResult> {

	private Logger log = LoggerFactory.getLogger(CloudApiTradeBatchPayQueryHandler.class);
	@Override
	public CloudApiTradeBatchPayQueryResult handle(CloudApiTradeBatchPayQueryParam reqParam) {
		log.info("批量代付结果查询，请求参数：{}",reqParam);
		CloudApiTradeBatchPayQueryResult result = new CloudApiTradeBatchPayQueryResult();
		
		log.info("批量代付结果查询，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradeBatchPayQueryParam> getReqParamType() {
		return CloudApiTradeBatchPayQueryParam.class;
	}

}
