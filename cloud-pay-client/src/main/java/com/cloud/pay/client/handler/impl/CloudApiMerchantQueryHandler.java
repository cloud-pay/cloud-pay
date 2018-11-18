package com.cloud.pay.client.handler.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.constants.Constants;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiMerchantQueryParam;
import com.cloud.pay.client.vo.CloudApiMerchantQueryResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.merchant.entity.MerchantApplyBankInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.entity.MerchantApplyFeeInfo;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantFeeInfo;
import com.cloud.pay.merchant.service.MerchantApplyService;
import com.cloud.pay.merchant.service.MerchantService;

/**
 * 商户信息查询
 * @author 
 */
@Service("cloudMerchantQueryHandler")
public class CloudApiMerchantQueryHandler 
            implements ICloudPayApiHandler<CloudApiMerchantQueryParam, CloudApiMerchantQueryResult> {

	private Logger log = LoggerFactory.getLogger(CloudApiMerchantQueryHandler.class);
	
	@Autowired
	private MerchantApplyService merchantApplyService;
	
	@Autowired
	private MerchantService merchantService;
	
	@Override
	public CloudApiMerchantQueryResult handle(CloudApiMerchantQueryParam reqParam) {
		log.info("商户信息查询，请求参数：{}",reqParam);
		CloudApiMerchantQueryResult result = new CloudApiMerchantQueryResult();
		if(StringUtils.isBlank(reqParam.getSubMchCode())) {
			 throw new CloudApiBusinessException(ApiErrorCode.PARAM_ERROR, "商户编码不可为空");
		}
		//获取机构信息,并判断是否为机构,只有机构才允许调用商户相关接口
		MerchantBaseInfo orgBaseInfo = (MerchantBaseInfo) merchantService.selectByCode(reqParam.getMchCode()).get("baseInfo");
		if(null != orgBaseInfo && 1 != orgBaseInfo.getType()) {
			throw new CloudApiBusinessException(ApiErrorCode.NOT_AUTHORITY, "该商户无此接口权限");
		}
		result.setMchCode(reqParam.getMchCode());
		result.setSubMchCode(reqParam.getSubMchCode());
		//根据商户先查询正式表，正式表没又再获取申请表
		Map<String, Object> map = merchantService.selectByCode(reqParam.getSubMchCode());
		if(null != map) {
			MerchantBaseInfo baseInfo = (MerchantBaseInfo) map.get("baseInfo");
			BeanUtils.copyProperties(baseInfo, result);
			result.setpMobile(baseInfo.getMobile());
			if(baseInfo.getStatus() == 1) {
				result.setStatus(2);
			}
			MerchantBankInfo bankInfo = (MerchantBankInfo) map.get("bankInfo");
			BeanUtils.copyProperties(bankInfo, result);
			result.setsMobileNo(bankInfo.getMobileNo());
			MerchantFeeInfo feeInfo = (MerchantFeeInfo) map.get("feeInfo");
			BeanUtils.copyProperties(feeInfo, result);
		}else {
			Map<String, Object> applyMap = merchantApplyService.selectByCode(reqParam.getSubMchCode());
			if(null == applyMap) {
				result.setResultCode(Constants.RESULT_CODE_FAIL);
				result.setErrorCode(ApiErrorCode.SUB_MCH_INVALID);
				result.setErrorMsg("商户信息不存在");
				log.info("商户信息查询，响应结果：{}",result);
				return result;
			}
			MerchantApplyBaseInfo baseInfo = (MerchantApplyBaseInfo) applyMap.get("baseInfo");
			BeanUtils.copyProperties(baseInfo, result);
			result.setpMobile(baseInfo.getMobile());
			MerchantApplyBankInfo bankInfo = (MerchantApplyBankInfo) applyMap.get("bankInfo");
			BeanUtils.copyProperties(bankInfo, result);
			result.setsMobileNo(bankInfo.getMobileNo());
			MerchantApplyFeeInfo feeInfo = (MerchantApplyFeeInfo) applyMap.get("feeInfo");
			BeanUtils.copyProperties(feeInfo, result);
		}
		log.info("商户信息查询，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiMerchantQueryParam> getReqParamType() {
		return CloudApiMerchantQueryParam.class;
	}

}
