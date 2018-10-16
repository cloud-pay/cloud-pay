package com.cloud.pay.client.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.common.utils.ApplicationContextHolder;

/**
 * 下游接口业务处理工厂
 * @author 
 */
@Component
public class TradeApiHandlerFactory {
   
	private Map<String, String> tradeApiHandlerMapper = new HashMap<String, String>();
	
	@PostConstruct
	public void init() {
		if(tradeApiHandlerMapper.size() <= 0) {
			tradeApiHandlerMapper.put("cloud.merchant.register", "cloudMerchantRegisterHandler"); //商户信息录入
			tradeApiHandlerMapper.put("cloud.merchant.update", "cloudMerchantUpdateHandler"); //商户信息修改
			tradeApiHandlerMapper.put("cloud.merchant.query", "cloudMerchantQueryHandler"); //商户信息查询
			tradeApiHandlerMapper.put("cloud.pay", "cloudPayHandler");  //单笔代付
			tradeApiHandlerMapper.put("cloud.pay.query", "cloudPayQueryHandler");//单笔代付结果查询
			tradeApiHandlerMapper.put("cloud.batch.pay", "cloudBatchPayHandler"); //批量代付
			tradeApiHandlerMapper.put("cloud.batch.pay.query", "cloudBatchPayQueryHandler"); //批量代付结果查询
			//tradeApiHandlerMapper.put("cloud.batch.pay.retry", "cloudBatchPayRetryHandler"); //批量代付重新触发
			tradeApiHandlerMapper.put("cloud.query.recon.file", "cloudReconFileHandler");
		}
	}
	
	
	public ICloudPayApiHandler getApiHandler(String tradeType) {
		  String handlerName = tradeApiHandlerMapper.get(tradeType);
		  if(null == handlerName) {
			  throw new CloudApiException(ApiErrorCode.PARAM_ERROR, "参数无效(tradeType)");
		  }
		  if(!ApplicationContextHolder.containsBean(handlerName)) {
			  throw new CloudApiException(ApiErrorCode.PARAM_ERROR,"暂不支持的业务类型(tradeType)");
		  }
		  return ApplicationContextHolder.getBean(handlerName);
	}
}
