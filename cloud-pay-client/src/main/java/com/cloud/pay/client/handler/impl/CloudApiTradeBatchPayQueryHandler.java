package com.cloud.pay.client.handler.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.constants.Constants;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.utils.DateUtil;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayQueryParam;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayQueryResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.dto.BatchTradeDTO;
import com.cloud.pay.trade.service.BatchTradeService;

/**
 * 批量代付结果查询
 * @author THINKPAD
 *
 */
@Service("cloudBatchPayQueryHandler")
public class CloudApiTradeBatchPayQueryHandler implements ICloudPayApiHandler<CloudApiTradeBatchPayQueryParam, CloudApiTradeBatchPayQueryResult> {

	private Logger log = LoggerFactory.getLogger(CloudApiTradeBatchPayQueryHandler.class);
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private BatchTradeService batchTradeService;
	
	@Override
	public CloudApiTradeBatchPayQueryResult handle(CloudApiTradeBatchPayQueryParam reqParam) {
		log.info("批量代付结果查询，请求参数：{}",reqParam);
		CloudApiTradeBatchPayQueryResult result = new CloudApiTradeBatchPayQueryResult();
		result.setMchCode(reqParam.getMchCode());
		result.setBatchNo(reqParam.getBatchNo());
		String mchCode = StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():reqParam.getMchCode();
		result.setSubMchCode(StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():null);
		//根据商户号查询商户信息
		Map<String, Object> merchantMap = merchantService.selectByCode(mchCode);
		if(null == merchantMap || merchantMap.size() <= 0) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.MCH_INVALID);
			result.setErrorMsg("商户不可用");
			log.info("批量代付结果查询，响应结果：{}",result);
			return result;
		}
		MerchantBaseInfo baseInfo = (MerchantBaseInfo) merchantMap.get("baseInfo");
		
		//查询批次信息
		BatchTradeDTO batchTrade = batchTradeService.getBatchByBatchNo(reqParam.getBatchNo(), baseInfo.getId());
		if(null == batchTrade) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.BATCH_NOT_EXIST);
			result.setErrorMsg("批次号不存在");
			log.info("批量代付结果查询，响应结果：{}",result);
			return result;
		}
		result.setTradeTime(DateUtil.getDateTimeFormat(batchTrade.getTradeTime()));
		result.setTotalAmount(batchTrade.getTotalAmount());
		result.setTotalCount(batchTrade.getTotalCount());
		result.setStatus(batchTrade.getStatus());
		//状态为成功时需要生成批量结果文件 TODO...
		log.info("批量代付结果查询，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradeBatchPayQueryParam> getReqParamType() {
		return CloudApiTradeBatchPayQueryParam.class;
	}

}
