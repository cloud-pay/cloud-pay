package com.cloud.pay.trade.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.FeeStatDTO;
import com.cloud.pay.trade.dto.PayResponseDTO;
import com.cloud.pay.trade.dto.TradeDTO;
import com.cloud.pay.trade.dto.TradeRecordDTO;
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
			payHandler.freezeMerchantFee(trade);
			PayTradeResVO resVO = payHandler.invokePay(trade);
			payHandler.updateTradeStatus(trade, resVO);
			resDTO.setStatus(trade.getStatus());
			resDTO.setReturnInfo(trade.getReturnInfo());
			resDTO.setReturnCode(trade.getReturnCode());
		} catch(TradeException e) {
			resDTO.setStatus(TradeConstant.STATUS_FAIL);
			resDTO.setReturnInfo(e.getMessage());
			resDTO.setReturnCode(e.getExCode());
			trade.setStatus(TradeConstant.STATUS_FAIL); 
			trade.setReturnCode(e.getExCode());
			trade.setReturnInfo(e.getMessage());
			trade.setTradeConfirmTime(new Date());
			tradeMapper.updateStatus(trade);
		} catch(Exception e) {
			log.error("单笔代付异常：{}", e);
			resDTO.setStatus(TradeConstant.STATUS_FAIL);
			resDTO.setReturnInfo("系统内部异常");
			resDTO.setReturnCode(TradeConstant.SYS_EXCEPTION);
			trade.setStatus(TradeConstant.STATUS_FAIL); 
			trade.setReturnCode(TradeConstant.SYS_EXCEPTION);
			trade.setReturnInfo(e.getMessage());
			trade.setTradeConfirmTime(new Date());
			tradeMapper.updateStatus(trade);
		}
		return resDTO;
	}
	
	public TradeStatDTO tradeStat(Integer merchantId, Integer orgId, Date startTime, Date endTime) {
		return tradeMapper.tradeStat(merchantId, orgId, startTime, endTime);
	}
	
	public TradeStatDTO loanTradeStat(Integer merchantId, Integer orgId, Date startTime, Date endTime) {
		return tradeMapper.loanTradeStat(merchantId, orgId, startTime, endTime);
	}
	
	/**
	 * 交易列表查询
	 * @author dbnaxlc
	 * @date 2018年9月14日 上午11:07:14
	 * @param merchantId
	 * @param orgId
	 * @param orderNo
	 * @param batchNo
	 * @param loaning
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<TradeRecordDTO> selectTradeList(Integer merchantId, Integer orgId,
		 String orderNo, String batchNo, Integer loaning,
			Date startTime, Date endTime) {
		return tradeMapper.selectTradeList(merchantId, orgId, orderNo, batchNo, loaning, startTime, endTime);
	}
	
	/**
	 * 商户手续费统计
	 * @author dbnaxlc
	 * @date 2018年9月14日 下午3:12:42
	 * @param merchantId
	 * @param orgId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<FeeStatDTO> selectMerchantFeeStats(Integer merchantId, Integer orgId,
			Date startTime, Date endTime) {
		return tradeMapper.selectMerchantFeeStats(merchantId, orgId, startTime, endTime);
	}
	
	public List<FeeStatDTO> selectOrgFeeStats(Integer orgId,
			Date startTime, Date endTime) {
		List<FeeStatDTO> orgStats = tradeMapper.selectOrgTradeFeeStats(orgId, startTime, endTime);
		List<FeeStatDTO> merchantStats = tradeMapper.selectMerchantFeeByOrg(orgId, startTime, endTime);
		if(orgStats == null || orgStats.size() == 0) {
			return merchantStats;
		} else {
			Map<String, FeeStatDTO> feeMap = new HashMap<String, FeeStatDTO>();
			for(FeeStatDTO stat : orgStats) {
				feeMap.put(stat.getStatDate() + stat.getMerchantCode(), stat);
			}
			for(FeeStatDTO stat : merchantStats) {
				if(feeMap.get(stat.getStatDate() + stat.getMerchantCode()) == null) {
					feeMap.put(stat.getStatDate() + stat.getMerchantCode(), stat);
				} else {
					FeeStatDTO orgStat = feeMap.get(stat.getStatDate() + stat.getMerchantCode());
					orgStat.setFeeAmount(stat.getFeeAmount().add(orgStat.getFeeAmount()));
					feeMap.put(stat.getStatDate() + stat.getMerchantCode(), orgStat);
				}
			}
		}
		return merchantStats;
	}
	
	/**
	 * 根据商户和订单号查询代付信息
	 * @param merchantId
	 * @param orderNo
	 * @return
	 */
	public TradeDTO selectTradeByMerIdAndOrderNo(Integer merchantId,String orderNo) {
		 return tradeMapper.selectTradeByMerIdAndOrderNo(merchantId, orderNo);
	}
}
