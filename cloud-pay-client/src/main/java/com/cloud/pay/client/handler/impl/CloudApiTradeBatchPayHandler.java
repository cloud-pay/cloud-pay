package com.cloud.pay.client.handler.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayParam;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.service.BatchTradeService;

/**
 * 批量代付
 * @author THINKPAD
 *
 */
@Service("cloudBatchPayHandler")
public class CloudApiTradeBatchPayHandler implements ICloudPayApiHandler<CloudApiTradeBatchPayParam, CloudApiTradeBatchPayResult> {
	
	private Logger log = LoggerFactory.getLogger(CloudApiTradeBatchPayHandler.class);
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private BatchTradeService batchTradeService;
	

	@Override
	public CloudApiTradeBatchPayResult handle(CloudApiTradeBatchPayParam reqParam) {
		log.info("批量代付，请求参数：{}",reqParam);
		CloudApiTradeBatchPayResult result = new CloudApiTradeBatchPayResult();
		result.setMchCode(reqParam.getMchCode());
		result.setBatchNo(reqParam.getBatchNo());
		String mchCode = StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():reqParam.getMchCode();
		result.setSubMchCode(StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():"");
		//根据商户号查询商户信息
		Map<String, Object> merchantMap = merchantService.selectByCode(mchCode);
		if(null == merchantMap || merchantMap.size() <= 0) {
			 throw new CloudApiBusinessException(ApiErrorCode.MCH_INVALID, "商户不可用");
		}
		MerchantApplyBaseInfo baseInfo = (MerchantApplyBaseInfo) merchantMap.get("baseInfo");
		
		log.info("批量代付，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradeBatchPayParam> getReqParamType() {
		return CloudApiTradeBatchPayParam.class;
	}
}
