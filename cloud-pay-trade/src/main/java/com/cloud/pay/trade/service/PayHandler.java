package com.cloud.pay.trade.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.mapper.MerchantBankInfoMapper;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.mapper.TradeMapper;

@Service
public class PayHandler {
	
	private Logger log = LoggerFactory.getLogger(PayHandler.class);
	
//	@Autowired
	private ICloudApiService payService;
	
	@Autowired
	private MerchantBankInfoMapper merchantBankInfoMapper;

	@Autowired
	private TradeMapper tradeMapper;
	
	/**
	 * 保存单笔代付
	 * @author 夏志强
	 * @throws TradeException 
	 * @date 2018年9月12日 下午5:23:06
	 */
	@Transactional
	public void singlePay(Trade trade) throws TradeException {
		log.info("单笔付款入参：{}", trade);
		String orderNo = tradeMapper.selectExist(trade.getMerchantId(), trade.getOrderNo());
		if(StringUtils.isNotBlank(orderNo)) {
			log.warn("订单号{}重复", orderNo);
			throw new TradeException("订单号重复", TradeConstant.ORDER_NO_EXIST);
		}
		tradeMapper.insert(trade);
	}
	
	/**
	 * 调用渠道单笔扣款
	 * @author 夏志强
	 * @date 2018年9月12日 下午5:22:55
	 */
	public PayTradeResVO invokePay(Trade trade) {
		MerchantBankInfo bankInfo = merchantBankInfoMapper.selectByMerchantId(trade.getMerchantId());
		PayTradeReqVO reqVO = new PayTradeReqVO();
		reqVO.setAmt(trade.getTradeAmount());
		//TODO 路由具体渠道时设置
//		reqVO.setMerchantNo(merchantNo);
		reqVO.setOrderNo(trade.getOrderNo());
		reqVO.setPayeeAccount(trade.getPayeeBankCard());
		reqVO.setPayeeBankCode(trade.getPayeeBankCode());
		reqVO.setPayeeName(trade.getPayeeName());
		reqVO.setPayerAccount(bankInfo.getBankCardNo());
		reqVO.setPayerName(bankInfo.getBankAccountName());
		reqVO.setPostscript(trade.getRemark());
		PayTradeResVO resVO = payService.pay(reqVO);
		log.info("订单号{}调用渠道返回结果{}", trade.getOrderNo(), resVO);
		return resVO;
	}
	
	/**
	 * 调用渠道返回后修改交易状态
	 * @author 夏志强
	 * @date 2018年9月12日 下午5:22:38
	 */
	@Transactional
	public void updateTradeStatus(Trade trade, PayTradeResVO resVO) {
		//TODO 设置渠道返回结果
//		trade.setStatus();
//		trade.setChannelId(channelId);
//		trade.setReturnCode(returnCode);
//		trade.setReturnInfo(returnInfo);
//		trade.setReconDate(resVO.getAccountDate());
		trade.setTradeConfirmTime(new Date());
		tradeMapper.updateStatus(trade);
	}
}
