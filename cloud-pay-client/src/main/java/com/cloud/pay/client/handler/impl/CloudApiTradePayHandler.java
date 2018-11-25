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
import com.cloud.pay.client.vo.CloudApiTradePayParam;
import com.cloud.pay.client.vo.CloudApiTradePayResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.PayResponseDTO;
import com.cloud.pay.trade.dto.TradeDTO;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.service.TradeService;

/**
 * 单笔代付
 * @author THINKPAD
 *
 */
@Service("cloudPayHandler")
public class CloudApiTradePayHandler implements ICloudPayApiHandler<CloudApiTradePayParam, CloudApiTradePayResult> {

	private Logger log = LoggerFactory.getLogger(CloudApiTradePayHandler.class);
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private TradeService tradeService;
	
	@Override
	public CloudApiTradePayResult handle(CloudApiTradePayParam reqParam) {
		CloudApiTradePayResult result = new CloudApiTradePayResult();
		result.setOrderNo(reqParam.getOrderNo());
		result.setMchCode(reqParam.getMchCode());
		log.info("单笔代付请求参数：{}",reqParam);
		String mchCode = StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():reqParam.getMchCode();
		result.setSubMchCode(StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():null);
		//根据商户号查询商户信息
		Map<String, Object> merchantMap = merchantService.selectByCode(mchCode);
		if(null == merchantMap || merchantMap.size() <= 0) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.MCH_INVALID);
			result.setErrorMsg("商户不可用");
			log.info("单笔代付响应结果：{}",result);
			return result;
		}
		MerchantBaseInfo baseInfo = (MerchantBaseInfo) merchantMap.get("baseInfo");
		//订单号和商户号确保唯一
		TradeDTO tradeHis = tradeService.selectTradeByMerIdAndOrderNo(baseInfo.getId(), reqParam.getOrderNo());
		if(null != tradeHis) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.ORDER_EXIST);
			result.setErrorMsg("订单号"+reqParam.getOrderNo()+"已存在");
			log.info("单笔代付响应结果：{}",result);
			return result;
		}
		Trade trade = new Trade();
		trade.setMerchantId(baseInfo.getId());
		trade.setOrderNo(reqParam.getOrderNo());
		//trade.setTradeTime(DateUtil.getDateTimeFormat(reqParam.getTradeTime()));
		trade.setTradeTime(DateUtil.formatDate(reqParam.getTradeTime(), "yyyyMMdd HH:mm:ss"));
		trade.setTradeAmount(reqParam.getTradeAmount());
		trade.setPayeeName(reqParam.getPayeeName());
		trade.setPayeeBankCard(reqParam.getPayeeBankCard());
		trade.setPayeeBankCode(reqParam.getPayeeBankCode());
		trade.setLoaning(reqParam.getLoaning() == null ? 0 : reqParam.getLoaning());
		try {
			log.info("单笔代付，请求代付平台参数：{}",trade);
		    PayResponseDTO payResponse = tradeService.pay(trade);
		    log.info("单笔代付，请求代付平台响应结果：{}",payResponse);
		    result.setResultCode(Constants.RESULT_CODE_SUCCESS);
		    if(TradeConstant.STATUS_FAIL == payResponse.getStatus()) {
		    	result.setStatus(Constants.RESULT_CODE_FAIL);
		    }else if(TradeConstant.STATUS_SUCCESS == payResponse.getStatus()){
		    	result.setStatus(Constants.RESULT_CODE_SUCCESS);
		    }else {
		    	result.setStatus(Constants.RESULT_CODE_UNKNOWN);
		    }
		    result.setErrorCode(payResponse.getReturnCode());
	    	result.setErrorMsg(payResponse.getReturnInfo());
	    }catch(Exception e) {
	    	log.error("调用代付平台失败{}",e);
	    	throw new CloudApiBusinessException(ApiErrorCode.SYSTEM_ERROR,"系统错误");
	    }
		log.info("单笔代付响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradePayParam> getReqParamType() {
		return CloudApiTradePayParam.class;
	}

}
