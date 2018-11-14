package com.cloud.pay.client.handler;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.pay.client.utils.GroupV1;
import com.cloud.pay.client.utils.GroupV2;
import com.cloud.pay.client.utils.Md5SignUtils;
import com.cloud.pay.client.vo.base.CloudApiBaseParam;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.merchant.entity.MerchantSecret;
import com.cloud.pay.merchant.service.MerchantService;

@Component
public class CloudApiHelper {

	private Logger log = LoggerFactory.getLogger(CloudApiHelper.class);
	
	private Validator validator;
	
	@Autowired
	private MerchantService merchantService;
	
	@PostConstruct
	public void init() {
	        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	        validator = validatorFactory.getValidator();
	}
	 
	 /**
	  * 将请求参数转为请求实体，并校验签名信息
	  * @param requestContent
	  * @param clazz
	  * @return
	  */
	 public <T extends CloudApiBaseParam> T reqContent2ReqParamWithValidSign(String requestContent, Class<T> clazz) {
		  T reqParam = reqContent2ReqParam(requestContent,clazz);
		  if (StringUtils.isBlank(reqParam.getSign())) {
			  throw new CloudApiException(ApiErrorCode.PARAM_ERROR, "签名不能为空");
		  }
		  //验证签名
		  validSign(reqParam);
		  return reqParam;
	 }
	 
	 /**
	  * 请求参数转为请求实体
	  * @param requestContent
	  * @param clazz
	  * @return
	  */
	 public <T extends CloudApiBaseParam> T reqContent2ReqParam(String requestContent, Class<T> clazz) {
		 if (StringUtils.isBlank(requestContent)) {
			 throw new CloudApiException(ApiErrorCode.POST_DATA_EMPTY, "请求数据为空");
		 }
		 JSONObject jsonObject = JSON.parseObject(requestContent);
		 return reqContent2ReqParam(jsonObject,clazz);
	 }
	 
	 /**
	  * 参数校验
	  * @param jsonObject
	  * @param clazz
	  * @return
	  */
	 private <T extends CloudApiBaseParam> T reqContent2ReqParam(JSONObject jsonObject, Class<T> clazz) {
		 T reqParam = JSON.parseObject(jsonObject.toJSONString(), clazz);
		 if (null == reqParam) {
			 throw new CloudApiException(ApiErrorCode.PARAM_ERROR, "参数无效");
		 }
		 //校验基本参数
		 validPropertyWithCommon(reqParam);
		 return reqParam;
	 }
	 
	 /**
	  * 参数校验
	  * @param reqParam
	  */
	 public <T extends CloudApiBaseParam> void validPropertyWithCommon(T reqParam) {
		 Set<ConstraintViolation<T>> violations = validator.validate(reqParam);
		 StringBuilder sb = new StringBuilder();
	     for (ConstraintViolation<T> violation : violations) {
	            String message = violation.getMessage();
	            sb.append(message);
	            sb.append("；");
	      }
	      String errorMsg = sb.toString();
	      if (StringUtils.isNotBlank(errorMsg)) {
	            throw new CloudApiException(ApiErrorCode.PARAM_ERROR, errorMsg);
	      }
	 }
	 
	 /**
	  * 验证签名
	  * @param reqParam
	  */
	 private <T extends CloudApiBaseParam> void validSign(T reqParam) {
		   //获取商户号或者机构号
		   String mchNo = reqParam.getMchCode();
		   String md5 = createSign(mchNo, reqParam);
		   if (!md5.equals(reqParam.getSign())) {
			   throw new CloudApiException(ApiErrorCode.SIGN_ERROR,"签名错误");
		   }
	 }
	 
	 /**
	  * 签名
	  * @param dataInfo
	  * @return
	  */
	 public String createSign(String mchNo,Object dataInfo) {
		 String signJsonPlainText = Md5SignUtils.createSignPlainText(dataInfo);
		 return createSignInfo(getSinKey(mchNo), signJsonPlainText);
	 }
	 
	 /**
	  * 签名
	  * @param signKey
	  * @param signJsonPlainText
	  * @return
	  */
	 private String createSignInfo(String signKey,String signJsonPlainText) {
		 return Md5SignUtils.createSignInfo(signKey, signJsonPlainText);
	 }
	 
	 /**
	  * 获取商户的签名key
	  * @param mchNo
	  * @return
	  */
	 private String getSinKey(String mchNo) {
		 MerchantSecret secret = merchantService.selectSecretByCode(mchNo);
		 if(null == secret) {
			 throw new CloudApiException(ApiErrorCode.MCH_INVALID,"非法商户");
		 }
		 return  secret.getSecret();
	 }
}
