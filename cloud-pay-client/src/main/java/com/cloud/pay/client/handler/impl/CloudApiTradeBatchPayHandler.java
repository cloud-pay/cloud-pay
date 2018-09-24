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
import com.cloud.pay.client.vo.CloudApiTradeBatchPayParam;
import com.cloud.pay.client.vo.CloudApiTradeBatchPayResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.dto.BatchTradeDTO;
import com.cloud.pay.trade.entity.BatchTrade;
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
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.MCH_INVALID);
			result.setErrorMsg("商户不可用");
			log.info("批量代付结果查询，响应结果：{}",result);
			return result;
		}
		MerchantApplyBaseInfo baseInfo = (MerchantApplyBaseInfo) merchantMap.get("baseInfo");
		//商户+批次号需保证唯一
		BatchTradeDTO batchTradeHis = batchTradeService.getBatchByBatchNo(reqParam.getBatchNo(), baseInfo.getId());
		if(null != batchTradeHis) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.BATCH_EXIST);
			result.setErrorMsg("批次号已存在");
			log.info("批量代付，响应结果：{}",result);
			return result;
		}
		//TODO .....
		BatchTrade batchTrade = new BatchTrade();
		batchTrade.setPayerMerchantId(baseInfo.getId());
		batchTrade.setBatchNo(reqParam.getBatchNo());
		batchTrade.setTotalAmount(reqParam.getTotalAmount());
		batchTrade.setTotalCount(reqParam.getTotalCount());
		batchTrade.setTradeTime(DateUtil.getDateTimeFormat(reqParam.getTradeTime()));
		String errorDetails = batchTradeService.batchPay(batchTrade, reqParam.getFilePath());//如果数据有问题直接全部不处理算了
		if(StringUtils.isNotBlank(errorDetails)) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.BATCH_DATA_ERROR);
			result.setErrorMsg("批次文件数据异常:" + errorDetails);
			log.info("批量代付，响应结果：{}",result);
			return result;
		}
		//如果提交到上游，需要等待十分钟后再过来查询
		result.setResultCode(Constants.RESULT_CODE_UNKNOWN);
		log.info("批量代付，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradeBatchPayParam> getReqParamType() {
		return CloudApiTradeBatchPayParam.class;
	}
}
