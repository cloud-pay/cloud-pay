package com.cloud.pay.client.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiTradePayParam;
import com.cloud.pay.client.vo.CloudApiTradePayResult;

/**
 * 单笔代付
 * @author THINKPAD
 *
 */
@Service("cloudPayHandler")
public class CloudApiTradePayHandler implements ICloudPayApiHandler<CloudApiTradePayParam, CloudApiTradePayResult> {

	private Logger log = LoggerFactory.getLogger(CloudApiTradePayHandler.class);
	
	@Override
	public CloudApiTradePayResult handle(CloudApiTradePayParam reqParam) {
		CloudApiTradePayResult result = new CloudApiTradePayResult();
		log.info("单笔代付请求参数：{}",reqParam);
		
		log.info("单笔代付响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradePayParam> getReqParamType() {
		return CloudApiTradePayParam.class;
	}

}
