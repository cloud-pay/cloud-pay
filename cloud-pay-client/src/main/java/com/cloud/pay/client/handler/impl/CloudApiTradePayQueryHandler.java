package com.cloud.pay.client.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiTradePayQueryParam;
import com.cloud.pay.client.vo.CloudApiTradePayQueryResult;

/**
 * 单笔代付结果查询
 * @author THINKPAD
 *
 */
@Service("cloudPayQueryHandler")
public class CloudApiTradePayQueryHandler implements ICloudPayApiHandler<CloudApiTradePayQueryParam, CloudApiTradePayQueryResult> {

	private Logger log = LoggerFactory.getLogger(CloudApiTradePayQueryHandler.class);
	
	@Override
	public CloudApiTradePayQueryResult handle(CloudApiTradePayQueryParam reqParam) {
		log.info("单笔代付结果查询，请求参数：{}",reqParam);
		CloudApiTradePayQueryResult result = new CloudApiTradePayQueryResult();
		
		log.info("单笔代付结果查询，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradePayQueryParam> getReqParamType() {
		return CloudApiTradePayQueryParam.class;
	}

}
