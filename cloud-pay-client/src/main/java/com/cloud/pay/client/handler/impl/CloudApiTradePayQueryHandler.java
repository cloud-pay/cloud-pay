package com.cloud.pay.client.handler.impl;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.utils.DateUtil;
import com.cloud.pay.client.constants.Constants;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiTradePayQueryParam;
import com.cloud.pay.client.vo.CloudApiTradePayQueryResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
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
		result.setSubMchCode(StringUtils.isNotBlank(reqParam.getSubMchCode())?reqParam.getSubMchCode():"");
		//根据商户号查询商户信息
		Map<String, Object> merchantMap = merchantService.selectByCode(mchCode);
		if(null == merchantMap || merchantMap.size() <= 0) {
			 throw new CloudApiBusinessException(ApiErrorCode.MCH_INVALID, "商户不可用");
		}
		MerchantApplyBaseInfo baseInfo = (MerchantApplyBaseInfo) merchantMap.get("baseInfo");
		TradeDTO trade = tradeService.selectTradeByMerIdAndOrderNo(baseInfo.getId(), reqParam.getOrderNo());
		if(null == trade) {
		    result.setResultCode(Constants.RESULT_CODE_FAIL);
		    result.setErrorCode(ApiErrorCode.ORDER_INVALID);
		    result.setErrorMsg("相关订单不存在");
		    log.info("单笔代付结果查询，响应结果:{}",result);
		    return result;
		}
		result.setTradeTime(DateUtil.formatDate(trade.getTradeTime(), "yyyy-MM-dd HH:mm:ss"));
		result.setTradeAmount(trade.getTradeAmount());
		result.setPayeeName(trade.getPayeeName());
		result.setPayeeBankCard(trade.getPayeeBankCard());
		result.setPayeeBankCode(trade.getPayeeBankCode());
		result.setStatus(trade.getStatus());
		result.setReconDate(DateUtil.formatDate(trade.getReconDate(),"yyyy-MM-dd HH:mm:ss"));
		result.setReconStatus(trade.getReconStatus());
		log.info("单笔代付结果查询，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiTradePayQueryParam> getReqParamType() {
		return CloudApiTradePayQueryParam.class;
	}

}
