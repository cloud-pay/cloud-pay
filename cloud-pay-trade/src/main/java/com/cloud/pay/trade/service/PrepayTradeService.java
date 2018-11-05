package com.cloud.pay.trade.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.entity.MerchantPrepayJournal;
import com.cloud.pay.merchant.mapper.MerchantPrepayInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantPrepayJournalMapper;
import com.cloud.pay.merchant.util.MD5;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.PrepayTradeDTO;
import com.cloud.pay.trade.entity.PrepayTrade;
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.mapper.PrepayTradeMapper;

@Service
public class PrepayTradeService {

	private Logger log = LoggerFactory.getLogger(PrepayTradeService.class);
	
	@Autowired
	private PrepayTradeMapper prepayTradeMapper;
	
	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;
	
	@Autowired
	private MerchantPrepayJournalMapper merchantPrepayJournalMapper;

	/**
	 * 交易列表查询
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<PrepayTradeDTO> selectTradeList(Integer merchantId, 
			Date startTime, Date endTime) {
		return prepayTradeMapper.selectTradeList(merchantId, startTime, endTime);
	}
	
	@Transactional
	public void saveTrade(PrepayTrade trade) throws Exception {
		log.info("保存预缴户交易信息:{}", trade);
		trade.setTradeTime(new Date());
		trade.setStatus(TradeConstant.STATUS_SUCCESS);
		prepayTradeMapper.insert(trade);
		MerchantPrepayInfo merchantPrepayInfo = merchantPrepayInfoMapper.lockByMerchantId(trade.getMerchantId());
		log.info("锁定商户预缴户信息:{}", merchantPrepayInfo);
		String digest =  MD5.md5(String.valueOf(merchantPrepayInfo.getBalance()) + "|" + merchantPrepayInfo.getFreezeAmount() , 
				String.valueOf(merchantPrepayInfo.getMerchantId()));
		if(!digest.equals(merchantPrepayInfo.getDigest())) {
			log.warn("商户预缴户摘要不匹配:{}", merchantPrepayInfo.getMerchantId());
			throw new TradeException("预缴户摘要不匹配", null);
		}
		MerchantPrepayJournal journal = new MerchantPrepayJournal();
		journal.setAmount(trade.getAmount());
		journal.setType(TradeConstant.TRADE_FEE);
		if(TradeConstant.RECHAEGE == trade.getTradeType()) {
			merchantPrepayInfo.setBalance(merchantPrepayInfo.getBalance().add(trade.getAmount()));
		} else if(TradeConstant.WITHDRAW == trade.getTradeType()) {
			if(merchantPrepayInfo.getBalance().subtract(merchantPrepayInfo.getFreezeAmount()).compareTo(trade.getAmount()) < 0) {
				log.warn("现有余额为:{}，小于提现金额：{}", 
						merchantPrepayInfo.getBalance().subtract(merchantPrepayInfo.getFreezeAmount()), trade.getAmount());
				throw new TradeException("现有余额为" + merchantPrepayInfo.getBalance().subtract(merchantPrepayInfo.getFreezeAmount()), null);
			}
			merchantPrepayInfo.setBalance(merchantPrepayInfo.getBalance().subtract(trade.getAmount()));
		}
		journal.setBalance(merchantPrepayInfo.getBalance());
		journal.setCreateTime(new Date());
		journal.setDebit(trade.getTradeType());
		journal.setPrepayId(merchantPrepayInfo.getId());
		journal.setPrepayTradeId(trade.getId());
		log.info("新增预缴户资金流水：{}", journal);
		merchantPrepayJournalMapper.insert(journal);
		merchantPrepayInfo.setDigest(MD5.md5(String.valueOf(merchantPrepayInfo.getBalance()) + "|" + merchantPrepayInfo.getFreezeAmount() , 
				String.valueOf(merchantPrepayInfo.getMerchantId())));
		log.info("修改预缴户信息：{}", merchantPrepayInfo);
		merchantPrepayInfoMapper.updateByPrimaryKey(merchantPrepayInfo);
	}
}
