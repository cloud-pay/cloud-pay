package com.cloud.pay.trade.service;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.entity.MerchantPrepayJournal;
import com.cloud.pay.merchant.mapper.MerchantPrepayInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantPrepayJournalMapper;
import com.cloud.pay.merchant.util.MD5;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.exception.TradeException;

@Service
public class PrepayInfoService {

	private Logger log = LoggerFactory.getLogger(PrepayInfoService.class);
	
	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;
	
	@Autowired
	private MerchantPrepayJournalMapper merchantPrepayJournalMapper;
	
	/**
	 * 保存预缴户资金流水
	 * @author dbnaxlc
	 * @date 2018年11月6日 下午4:03:23
	 * @param prepayInfo 预缴户信息
	 * @param journalType 流水类型
	 * @param amount 发生额
	 * @param creditOrDebit 入账/出账
	 * @param tradeId 交易ID
	 * @throws Exception
	 */
	public void savePrepayInfoJournal(MerchantPrepayInfo prepayInfo, Integer journalType, BigDecimal amount, Integer creditOrDebit, Integer tradeId) throws Exception {
		log.info("垫资机构预缴户信息:{}", prepayInfo);
		String orgDigest = MD5.md5(String.valueOf(prepayInfo.getBalance()) + "|" + prepayInfo.getFreezeAmount() , 
				String.valueOf(prepayInfo.getMerchantId()));
		if(!orgDigest.equals(prepayInfo.getDigest())) {
			log.info("{}预缴户被篡改", prepayInfo.getMerchantId());
			throw new TradeException("预缴户被篡改", TradeConstant.PREPAY_CHANGE);
		}
		if(TradeConstant.CREDIT == creditOrDebit) {
			prepayInfo.setFreezeAmount(prepayInfo.getFreezeAmount().subtract(amount));
			prepayInfo.setBalance(prepayInfo.getBalance().subtract(amount));
		} else {
			prepayInfo.setBalance(prepayInfo.getBalance().add(amount));
		}
		prepayInfo.setDigest(MD5.md5(String.valueOf(prepayInfo.getBalance()) + "|" + prepayInfo.getFreezeAmount() , 
				String.valueOf(prepayInfo.getMerchantId())));
		log.info("修改预缴户信息：{}", prepayInfo);
		merchantPrepayInfoMapper.updateByPrimaryKey(prepayInfo);
		//新增流水
		MerchantPrepayJournal journal = new MerchantPrepayJournal();
		journal.setAmount(amount);
		journal.setType(TradeConstant.HADNING_FEE);
		journal.setBalance(prepayInfo.getBalance());
		journal.setCreateTime(new Date());
		journal.setDebit(TradeConstant.CREDIT);
		journal.setPrepayId(prepayInfo.getId());
		journal.setTradeId(tradeId);
		log.info("新增预缴户资金流水：{}", journal);
		merchantPrepayJournalMapper.insert(journal);
	}
}
