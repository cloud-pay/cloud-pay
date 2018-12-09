package com.cloud.pay.trade.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.util.ConvertUtil;

@Service
public class PrepayInfoService {

	private Logger log = LoggerFactory.getLogger(PrepayInfoService.class);

	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;

	@Autowired
	private MerchantPrepayJournalMapper merchantPrepayJournalMapper;
	
	/**
	 * 根据商户ID锁定预缴户信息
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public MerchantPrepayInfo lockByMerchantId(Integer merchantId) throws Exception {
		MerchantPrepayInfo info = merchantPrepayInfoMapper.lockByMerchantId(merchantId);
		log.info("商户预缴户信息:{}", info);
		String digest = MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount(),
				String.valueOf(info.getMerchantId()));
		if (!digest.equals(info.getDigest())) {
			log.warn("商户{}预缴户被篡改", merchantId);
			throw new TradeException("商户预缴户被篡改", TradeConstant.PREPAY_CHANGE);
		}
		return info;
	}
	
	/**
	 * 锁定预缴户列表
	 * @param merchantIds
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<Integer, MerchantPrepayInfo> lockByMerchantIds(List<Integer> merchantIds) throws Exception {
		List<MerchantPrepayInfo> infos = merchantPrepayInfoMapper.lockByMerchantIds(merchantIds);
		Map<Integer, MerchantPrepayInfo> maps = ConvertUtil.convertMap(infos);
		return maps;
	}
	
	

	/**
	 * 保存预缴户资金流水
	 * 
	 * @author dbnaxlc
	 * @date 2018年11月6日 下午4:03:23
	 * @param prepayInfo
	 *            预缴户信息
	 * @param journalType
	 *            流水类型
	 * @param amount
	 *            发生额
	 * @param creditOrDebit
	 *            入账/出账
	 * @param tradeId
	 *            交易ID
	 * @throws Exception
	 */
	@Transactional
	public void savePrepayInfoJournal(MerchantPrepayInfo prepayInfo, Integer journalType, BigDecimal amount,
			Integer creditOrDebit, Integer tradeId) throws Exception {
		log.info("机构预缴户信息:{}", prepayInfo);
		if (TradeConstant.CREDIT == creditOrDebit) {
			prepayInfo.setFreezeAmount(prepayInfo.getFreezeAmount().subtract(amount));
			prepayInfo.setBalance(prepayInfo.getBalance().subtract(amount));
		} else {
			prepayInfo.setBalance(prepayInfo.getBalance().add(amount));
		}
		prepayInfo.setDigest(MD5.md5(String.valueOf(prepayInfo.getBalance()) + "|" + prepayInfo.getFreezeAmount(),
				String.valueOf(prepayInfo.getMerchantId())));
		log.info("修改预缴户信息：{}", prepayInfo);
		merchantPrepayInfoMapper.updateByPrimaryKey(prepayInfo);
		// 新增流水
		MerchantPrepayJournal journal = new MerchantPrepayJournal();
		journal.setAmount(amount);
		journal.setType(journalType);
		journal.setBalance(prepayInfo.getBalance());
		journal.setCreateTime(new Date());
		journal.setDebit(creditOrDebit);
		journal.setPrepayId(prepayInfo.getId());
		journal.setTradeId(tradeId);
		log.info("新增预缴户资金流水：{}", journal);
		merchantPrepayJournalMapper.insert(journal);
	}

	/**
	 * 冻结指定金额
	 * 
	 * @param merchantId
	 * @param freezeAmount
	 * @throws Exception
	 */
	@Transactional
	public void freezePrepayInfo(Integer merchantId, BigDecimal freezeAmount) throws Exception {
		MerchantPrepayInfo info = lockByMerchantId(merchantId);
		if (info.getBalance().subtract(info.getFreezeAmount()).compareTo(freezeAmount) < 0) {
			log.warn("现有余额为:{}，小于提现金额：{}", info.getBalance().subtract(info.getFreezeAmount()), freezeAmount);
			throw new TradeException("现有余额为" + info.getBalance().subtract(info.getFreezeAmount()),
					TradeConstant.PREPAY_BALANCE_NO_ENOUGH);
		} else {
			// 冻结金额
			info.setFreezeAmount(info.getFreezeAmount().add(freezeAmount));
			info.setDigest(MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount(),
					String.valueOf(info.getMerchantId())));
			log.info("冻结商户信息：{}", info);
			merchantPrepayInfoMapper.updateByPrimaryKey(info);
		}
	}

	/**
	 * 解冻指定金额
	 * 
	 * @param merchantId
	 * @param unfreezeAmount
	 * @throws Exception
	 */
	@Transactional
	public void unfreezePrepayInfo(Integer merchantId, BigDecimal unfreezeAmount) throws Exception {
		MerchantPrepayInfo info = lockByMerchantId(merchantId);
		// 解冻金额
		info.setFreezeAmount(info.getFreezeAmount().subtract(unfreezeAmount));
		info.setDigest(MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount(),
				String.valueOf(info.getMerchantId())));
		log.info("冻结商户信息：{}", info);
		merchantPrepayInfoMapper.updateByPrimaryKey(info);
	}
	
	/**
	 * 新增预缴户资金流水，但不修改资金账户
	 * 
	 * @param prepayInfo
	 *            预缴户信息
	 * @param journalType
	 *            流水类型
	 * @param amount
	 *            发生额
	 * @param creditOrDebit
	 *            入账/出账
	 * @param tradeId
	 *            交易ID
	 * @throws Exception
	 */
	@Transactional
	public void insertPrepayInfoJournal(MerchantPrepayInfo prepayInfo, Integer journalType, BigDecimal amount,
			Integer creditOrDebit, Integer tradeId) throws Exception {
		if (TradeConstant.CREDIT == creditOrDebit) {
			prepayInfo.setFreezeAmount(prepayInfo.getFreezeAmount().subtract(amount));
			prepayInfo.setBalance(prepayInfo.getBalance().subtract(amount));
		} else {
			prepayInfo.setBalance(prepayInfo.getBalance().add(amount));
		}
		prepayInfo.setDigest(MD5.md5(String.valueOf(prepayInfo.getBalance()) + "|" + prepayInfo.getFreezeAmount(),
				String.valueOf(prepayInfo.getMerchantId())));
		// 新增流水
		MerchantPrepayJournal journal = new MerchantPrepayJournal();
		journal.setAmount(amount);
		journal.setType(journalType);
		journal.setBalance(prepayInfo.getBalance());
		journal.setCreateTime(new Date());
		journal.setDebit(creditOrDebit);
		journal.setPrepayId(prepayInfo.getId());
		journal.setTradeId(tradeId);
		log.info("新增预缴户资金流水：{}", journal);
		merchantPrepayJournalMapper.insert(journal);
	}
	
	/**
	 * 批量修改预缴户
	 * @param infos
	 */
	@Transactional
	public void updatePrepayInfos(Collection<MerchantPrepayInfo> infos) {
		for(MerchantPrepayInfo info : infos) {
			log.info("修改预缴户信息：{}", info);
			merchantPrepayInfoMapper.updateByPrimaryKey(info);
		}
	}
}
