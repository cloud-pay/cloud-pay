package com.cloud.pay.trade.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.PayResponseDTO;
import com.cloud.pay.trade.dto.TradeStatDTO;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.mapper.TradeMapper;

@Service
public class TradeService {

	private Logger log = LoggerFactory.getLogger(TradeService.class);
	
	@Autowired
	private TradeMapper tradeMapper;

	@Autowired
	private PayHandler payHandler;

	public List<Trade> selectByBatchNo(String batchNo) {
		return tradeMapper.selectByBatchNo(batchNo);
	}
	
	/**
	 * 单笔代付
	 * @param trade
	 * @return
	 */
	public PayResponseDTO pay(Trade trade) {
		PayResponseDTO resDTO = new PayResponseDTO();
		resDTO.setOrderNo(trade.getOrderNo());
		try {
			payHandler.singlePay(trade);
			PayTradeResVO resVO = payHandler.invokePay(trade);
			payHandler.updateTradeStatus(trade, resVO);
			
			resDTO.setStatus(trade.getStatus());
			resDTO.setReturnInfo(trade.getReturnInfo());
			resDTO.setReturnCode(trade.getReturnCode());
		} catch(TradeException e) {
			resDTO.setStatus(TradeConstant.STATUS_FAIL);
			resDTO.setReturnInfo(e.getMessage());
			resDTO.setReturnCode(e.getExCode());
		} catch(Exception e) {
			log.error("单笔代付异常：{}", e);
			resDTO.setStatus(TradeConstant.STATUS_FAIL);
			resDTO.setReturnInfo("系统内部异常");
			resDTO.setReturnCode(TradeConstant.SYS_EXCEPTION);
		}
		return resDTO;
	}
	
	public TradeStatDTO tradeStat(Integer merchantId, Integer orgId, Date startTime, Date endTime) {
		return tradeMapper.tradeStat(merchantId, orgId, startTime, endTime);
	}
	
	public TradeStatDTO loanTradeStat(Integer merchantId, Integer orgId, Date startTime, Date endTime) {
		return tradeMapper.loanTradeStat(merchantId, orgId, startTime, endTime);
	}
}
