package com.cloud.pay.client.handler.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.constants.ApiErrorCode;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.utils.BeanUtil;
import com.cloud.pay.client.vo.CloudApiMerchantUpdateParam;
import com.cloud.pay.client.vo.CloudApiMerchantUpdateResult;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.merchant.entity.MerchantApplyBankInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.entity.MerchantApplyFeeInfo;
import com.cloud.pay.merchant.service.MerchantApplyService;

/**
 * 商户信息修改
 * @author 
 */
@Service("cloudMerchantUpdateHandler")
public class CloudApiMerchantUpdateHandler 
             implements ICloudPayApiHandler<CloudApiMerchantUpdateParam, CloudApiMerchantUpdateResult> {
	
	private Logger log = LoggerFactory.getLogger(CloudApiMerchantUpdateHandler.class);
	
	@Autowired
	private MerchantApplyService merchantApplyService;
	
	
	@Override
	public CloudApiMerchantUpdateResult handle(CloudApiMerchantUpdateParam reqParam) {
		CloudApiMerchantUpdateResult result = new CloudApiMerchantUpdateResult();
		if(StringUtils.isBlank(reqParam.getSubMchCode())) {
			 throw new CloudApiBusinessException(ApiErrorCode.PARAM_ERROR, "商户编码不可为空");
		}
		//获取商户基本信息
		MerchantApplyBaseInfo baseInfo = null;
		if(null == baseInfo) {
			throw new CloudApiBusinessException(ApiErrorCode.SUB_MCH_INVALID, "商户信息不存在");
		}
		try {
			BeanUtils.copyProperties(reqParam, baseInfo,BeanUtil.getNullPropertyNames(reqParam));
			//获取商户结算信息
			MerchantApplyBankInfo bankInfo = null;
			BeanUtils.copyProperties(reqParam, bankInfo,BeanUtil.getNullPropertyNames(reqParam));
			//获取商户费率信息
			MerchantApplyFeeInfo feeInfo = null;
			BeanUtils.copyProperties(reqParam, feeInfo,BeanUtil.getNullPropertyNames(reqParam));
			merchantApplyService.update(baseInfo, bankInfo, feeInfo, null);
		}catch(Exception e) {
			log.error("修改商户信息失败：{}",e);
			throw new CloudApiBusinessException(ApiErrorCode.SYSTEM_ERROR,"系统错误"); 
		}
		return result;
	}

	@Override
	public Class<CloudApiMerchantUpdateParam> getReqParamType() {
		return CloudApiMerchantUpdateParam.class;
	}
	
	
	public static void main(String[] args) {
		MerchantApplyBaseInfo baseInfo = new MerchantApplyBaseInfo();
		baseInfo.setName("小黑");
		baseInfo.setShortName("小明");
		CloudApiMerchantUpdateParam param = new CloudApiMerchantUpdateParam(); 
		param.setName("小明");
		BeanUtils.copyProperties(param, baseInfo,BeanUtil.getNullPropertyNames(param));
		System.out.println(baseInfo);
	}
	
	
}
