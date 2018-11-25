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
import com.cloud.pay.client.vo.CloudApiTradePayQueryParam;
import com.cloud.pay.client.vo.CloudApiTradePayQueryResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.TradeDTO;
import com.cloud.pay.trade.service.TradeService;

/**
 * 单笔代付结果查询
 * @author THINKPAD
 *
 */
@Service("cloudPayQueryHandler")
public class CloudApiTradePayQueryHandler implements ICloudPayApiHandler<CloudApiTradePayQueryParam, CloudApiTradePayQueryResult> {

	private Logger log = LoggerFactory.getLogger(CloudApiTradePayQueryHandler.class);
	
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private TradeService tradeService;
	
	@Override
	public CloudApiTradePayQueryResult handle(CloudApiTradePayQueryParam reqParam) {
		log.info("单笔代付结果查询，请求参数：{}",reqParam);
		CloudApiTradePayQueryResult result = new CloudApiTradePayQueryResult();
		result.setOrderNo(reqParam.getOrderNo());
		result.setMchCode(reqParam.getMchCode());
		String mchCode = StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():reqParam.getMchCode();
		result.setSubMchCode(StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():null);
		//根据商户号查询商户信息
		Map<String, Object> merchantMap = merchantService.selectByCode(mchCode);
		if(null == merchantMap || merchantMap.size() <= 0) {
			 throw new CloudApiBusinessException(ApiErrorCode.MCH_INVALID, "商户不可用");
		}
		MerchantBaseInfo baseInfo = (MerchantBaseInfo) merchantMap.get("baseInfo");
		TradeDTO trade = tradeService.selectTradeByMerIdAndOrderNo(baseInfo.getId(), reqParam.getOrderNo());
		if(null == trade) {
		    result.setResultCode(Constants.RESULT_CODE_FAIL);
		    result.setErrorCode(ApiErrorCode.ORDER_NOT_EXIST);
		    result.setErrorMsg("相关订单不存在");
		    log.info("单笔代付结果查询，响应结果:{}",result);
		    return result;
		}
		result.setTradeTime(DateUtil.getDateTimeFormat(trade.getTradeTime()));
		result.setTradeAmount(trade.getTradeAmount());
		result.setPayeeName(trade.getPayeeName());
		result.setPayeeBankCard(trade.getPayeeBankCard());
		result.setPayeeBankCode(trade.getPayeeBankCode());
		
		if(TradeConstant.STATUS_FAIL == trade.getStatus()) {
	    	result.setStatus(Constants.RESULT_CODE_FAIL);
	    }else if(TradeConstant.STATUS_SUCCESS == trade.getStatus()){
	    	result.setStatus(Constants.RESULT_CODE_SUCCESS);
	    }else {
	    	result.setStatus(Constants.RESULT_CODE_UNKNOWN);
	    }
		result.setReconDate(trade.getReconDate()==null ? null : DateUtil.getDateTimeFormat(trade.getReconDate()));
		result.setReconStatus(trade.getReconStatus());
		log.info("单笔代付结果查询，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradePayQueryParam> getReqParamType() {
		return CloudApiTradePayQueryParam.class;
	}

}
