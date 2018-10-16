package com.cloud.pay.client.handler.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiMerchantRegisterParam;
import com.cloud.pay.client.vo.CloudApiMerchantRegisterResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.merchant.entity.MerchantApplyBankInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.entity.MerchantApplyFeeInfo;
import com.cloud.pay.merchant.service.MerchantApplyService;

/**
 * 商户信息报备
 * @author THINKPAD
 *
 */
@Service("cloudMerchantRegisterHandler")
public class CloudApiMerchantRegisterHandler 
           implements ICloudPayApiHandler<CloudApiMerchantRegisterParam, CloudApiMerchantRegisterResult> {
	
	private Logger log = LoggerFactory.getLogger(CloudApiMerchantRegisterHandler.class);
	
	@Autowired
	private MerchantApplyService merchantApplyService;
	
	@Override
	public CloudApiMerchantRegisterResult handle(CloudApiMerchantRegisterParam reqParam) {
		log.info("商户信息报备，请求参数：{}",reqParam);
		CloudApiMerchantRegisterResult result = new CloudApiMerchantRegisterResult();
		//商户基础资料
		MerchantApplyBaseInfo baseInfo = new MerchantApplyBaseInfo();
		BeanUtils.copyProperties(reqParam, baseInfo);
		baseInfo.setMobile(reqParam.getpMobile());
		//商户结算资料
		MerchantApplyBankInfo bankInfo = new MerchantApplyBankInfo();
		BeanUtils.copyProperties(reqParam, bankInfo);
		bankInfo.setMobileNo(reqParam.getsMobileNo());
		//商户费率信息
		MerchantApplyFeeInfo feeInfo = new MerchantApplyFeeInfo();
		BeanUtils.copyProperties(reqParam, feeInfo);
		try {
			//获取商户文件信息
		   JSONObject attachementJson = createAttachementJson(reqParam);
		   String code = merchantApplyService.save(baseInfo, bankInfo, feeInfo, attachementJson,reqParam.getMchCode(),true);
		   result.setSubMchCode(code);
		   result.setMchCode(reqParam.getMchCode());
		}catch(IOException e) {
			log.error("商户报备接口，请求错误：{}",e);
			throw new CloudApiBusinessException(ApiErrorCode.SYSTEM_ERROR,"系统错误");
		}
		log.info("商户信息报备，响应结果：{}",result);
		return result;
	}
	
	/**
	 * 构建文件json数据
	 * @param reqParam
	 * @return
	 */
	private JSONObject createAttachementJson(CloudApiMerchantRegisterParam reqParam) {
		if(StringUtils.isNotBlank(reqParam.getBusinessFilePath()) || StringUtils.isNotBlank(reqParam.getCertFilePath())
				||StringUtils.isNotBlank(reqParam.getBankCardFilePath()) || StringUtils.isNotBlank(reqParam.getProtocolFilePath())) {
			JSONObject object = new JSONObject();
			if(StringUtils.isNotBlank(reqParam.getBusinessFilePath())) {
				object.put("businessPath", reqParam.getBusinessFilePath());
			}
			if(StringUtils.isNotBlank(reqParam.getCertFilePath())) {
				object.put("certPath", reqParam.getCertFilePath());
			}
			if(StringUtils.isNotBlank(reqParam.getBankCardFilePath())) {
				object.put("bankCardPath", reqParam.getBankCardFilePath());
			}
			if(StringUtils.isNotBlank(reqParam.getProtocolFilePath())) {
				object.put("protocolPath", reqParam.getProtocolFilePath());
			}
			return object;
		}
		return null;
	}

	

	@Override
	public Class<CloudApiMerchantRegisterParam> getReqParamType() {
		return CloudApiMerchantRegisterParam.class;
	}

}
