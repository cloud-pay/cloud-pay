package com.cloud.pay.client.handler.impl;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.pay.client.constants.Constants;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiMerchantRegisterParam;
import com.cloud.pay.client.vo.CloudApiMerchantRegisterResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.merchant.entity.MerchantApplyBankInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.entity.MerchantApplyFeeInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.service.MerchantApplyService;
import com.cloud.pay.merchant.service.MerchantService;

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
	
	@Autowired
	private MerchantService merchantService;
	
	@Override
	public CloudApiMerchantRegisterResult handle(CloudApiMerchantRegisterParam reqParam) {
		log.info("商户信息报备，请求参数：{}",reqParam);
		CloudApiMerchantRegisterResult result = new CloudApiMerchantRegisterResult();
		//获取机构信息,并判断是否为机构,只有机构才允许调用商户相关接口
		MerchantBaseInfo orgBaseInfo = (MerchantBaseInfo) merchantService.selectByCode(reqParam.getMchCode()).get("baseInfo");
		if(null != orgBaseInfo && 1 != orgBaseInfo.getType()) {
			throw new CloudApiBusinessException(ApiErrorCode.NOT_AUTHORITY, "该商户无此接口权限");
		}
		//商户基础资料
		MerchantApplyBaseInfo baseInfo = new MerchantApplyBaseInfo();
		BeanUtils.copyProperties(reqParam, baseInfo);
		baseInfo.setMobile(reqParam.getpMobile());
		baseInfo.setCreateTime(new Date());
		baseInfo.setModifyTime(new Date());
		baseInfo.setOrgId(orgBaseInfo.getId());
		baseInfo.setVersion(1);
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
		if(StringUtils.isNotBlank(reqParam.getBusinessFileName()) || StringUtils.isNotBlank(reqParam.getCertFileName())
				||StringUtils.isNotBlank(reqParam.getBankCardFileName()) || StringUtils.isNotBlank(reqParam.getProtocolFileName())) {
			JSONObject object = new JSONObject();
			if(StringUtils.isNotBlank(reqParam.getBusinessFileName())) {
				object.put("businessPath", reqParam.getBusinessFileName());
			}
			if(StringUtils.isNotBlank(reqParam.getCertFileName())) {
				object.put("certPath", reqParam.getCertFileName());
			}
			if(StringUtils.isNotBlank(reqParam.getBankCardFileName())) {
				object.put("bankCardPath", reqParam.getBankCardFileName());
			}
			if(StringUtils.isNotBlank(reqParam.getProtocolFileName())) {
				object.put("protocolPath", reqParam.getProtocolFileName());
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
