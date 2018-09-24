package com.cloud.pay.client.handler.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.constants.Constants;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiTradePayParam;
import com.cloud.pay.client.vo.CloudApiTradePayResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.PayResponseDTO;
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
		result.setSubMchCode(StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():"");
		//根据商户号查询商户信息
		Map<String, Object> merchantMap = merchantService.selectByCode(mchCode);
		if(null == merchantMap || merchantMap.size() <= 0) {
			 throw new CloudApiBusinessException(ApiErrorCode.MCH_INVALID, "商户不可用");
		}
		MerchantApplyBaseInfo baseInfo = (MerchantApplyBaseInfo) merchantMap.get("baseInfo");
		Trade trade = new Trade();
		trade.setMerchantId(baseInfo.getId());
		trade.setOrderNo(reqParam.getOrderNo());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			trade.setTradeTime(sdf.parse(reqParam.getTradeTime()));
		} catch (ParseException e) {
			log.error("单笔代付请求，时间格式错误");
			throw new CloudApiBusinessException(ApiErrorCode.PARAM_ERROR,"时间格式错误");
		}
		trade.setTradeAmount(reqParam.getTradeAmount());
		trade.setPayeeName(reqParam.getPayeeName());
		trade.setPayeeBankCard(reqParam.getPayeeBankCard());
		trade.setPayeeBankCode(reqParam.getPayeeBankCode());
		try {
			log.info("单笔代付，请求代付平台参数：{}",trade);
		    PayResponseDTO payResponse = tradeService.pay(trade);
		    log.info("单笔代付，请求代付平台响应结果：{}",payResponse);
		    if(TradeConstant.STATUS_FAIL == payResponse.getStatus()) {
		    	result.setResultCode(Constants.RESULT_CODE_FAIL);
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
