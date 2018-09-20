package com.cloud.pay.client.contoller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.pay.client.constants.Constants;
import com.cloud.pay.client.handler.CloudApiHelper;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.handler.TradeApiHandlerFactory;
import com.cloud.pay.client.vo.base.CloudApiBaseResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.common.exception.CloudApiException;

@RestController
public class CloudPayApiController {
  
	private Logger log = LoggerFactory.getLogger(CloudPayApiController.class);
	
	@Autowired
	private TradeApiHandlerFactory tradeApiHandlerFactory;
	
	@Autowired
	private CloudApiHelper cloudApiHelper;
	  
	@RequestMapping(value = "/api", method = RequestMethod.POST)
	public CloudApiBaseResult handleTrade(@RequestBody String requestContent) {
		 log.info("云支付平台，代付接口，请求参数：{}",requestContent);
		 CloudApiBaseResult result = null;
		 if(StringUtils.isBlank(requestContent)) {
			 result = new CloudApiBaseResult(Constants.RETURN_CODE_FAIL,"请求报文为空");
			 return result;
		 }
		 JSONObject jsonObject = null;
		 try {
	         jsonObject = JSON.parseObject(requestContent);
	     } catch (Exception e) {
	    	 log.error("云支付平台,请求参数转换异常:{}",e);
	    	 result = new CloudApiBaseResult(Constants.RETURN_CODE_FAIL,"请求报文格式错误");
	    	 return result;
	     }
		 //获取请求接口名称
	     String tradeType = jsonObject.getString("tradeType");
	     if (StringUtils.isBlank(tradeType)) {
	    	 result = new CloudApiBaseResult(Constants.RETURN_CODE_FAIL,"请求报文格式错误,缺少字段(tradeType)");
	    	 return result;
	     }
	     String mchNo = "";
	     try {
	    	 //获取处理类
	    	 ICloudPayApiHandler Handler = tradeApiHandlerFactory.getApiHandler(tradeType);
	    	 //获取请求参数类
	    	 Class reqParamClazz =  Handler.getReqParamType();
	    	 //转换实体类，并验签
	    	 Object reqParam = cloudApiHelper.reqContent2ReqParamWithValidSign(requestContent, reqParamClazz);
	    	 
	    	 result = Handler.handle(reqParam);
	    	 
	    	 //如果没有传入结构合作码，就说明为商户请求
	    	 mchNo =  jsonObject.getString("orgCode");
	    	 if(StringUtils.isBlank(mchNo)) {
	    		 mchNo =  jsonObject.getString("merchantCode");
	    	 }
	    	 result.setSign(cloudApiHelper.createSign(mchNo, result));
	     }catch(CloudApiException e) {
	    	 log.error("云支付平台，业务交易异常：{}",e);
	    	 if(e instanceof CloudApiBusinessException) {
	    		 //业务类错误，需要对结果加签
	    		 result = new CloudApiBaseResult(Constants.RESULT_CODE_FAIL,e.getErrorCode(),e.getMessage());
	    		 result.setSign(cloudApiHelper.createSign(mchNo, result));
	    		 return result;
	    	 }
	    	 result = new CloudApiBaseResult(Constants.RETURN_CODE_FAIL,Constants.RESULT_CODE_FAIL,e.getErrorCode(),e.getMessage());
	     }catch(Exception e) {
	    	 log.error("云支付平台，业务交易异常：{}",e);
	    	 result = new CloudApiBaseResult(Constants.RETURN_CODE_FAIL,Constants.RESULT_CODE_FAIL,ApiErrorCode.SYSTEM_ERROR,"系统错误");
	     }
		 log.info("云支付平台，API接口响应参数:{}",result);
		 return result;
	}
}
