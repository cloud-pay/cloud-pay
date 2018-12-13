package com.cloud.pay.trade.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.mapper.SysConfigMapper;
import com.cloud.pay.trade.constant.SmsConstant;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.entity.BatchTrade;
import com.cloud.pay.trade.entity.PaySms;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.mapper.BatchTradeMapper;
import com.cloud.pay.trade.mapper.PaySmsMapper;
import com.cloud.pay.trade.mapper.TradeMapper;


@Service
public class BatchTradeHandler {
	
	private Logger log =LoggerFactory.getLogger(BatchTradeHandler.class);

	@Autowired
	private BatchTradeMapper batchTradeMapper;

	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private PaySmsMapper paySmsMapper;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	@Autowired
	private PrepayInfoService prepayInfoService;
	
	@Autowired
	private ICloudApiService payService;
	
	
	/**
	 * 计算总金额
	 * @param merchantFee
	 * @param loanBenefit
	 * @param orgBenefit
	 * @return
	 */
	private BigDecimal add(BigDecimal merchantFee, BigDecimal loanBenefit, BigDecimal orgBenefit) {
		if(merchantFee == null) {
			merchantFee = BigDecimal.ZERO;
		}
		if(loanBenefit == null) {
			loanBenefit = BigDecimal.ZERO;
		}
		if(orgBenefit == null) {
			orgBenefit = BigDecimal.ZERO;
		}
		return merchantFee.add(orgBenefit).add(loanBenefit);
	}
	
	/**
	 * 审批不通过
	 * @param batchTrade
	 * @param smsCode
	 * @throws Exception
	 */
	@Transactional
	public void auditNo(BatchTrade batchTrade, String smsCode) throws Exception {
		batchTradeMapper.audit(batchTrade);
		//审核不通过，则交易置为失败
		tradeMapper.updateByBatchNo(new Date(), 3, batchTrade.getBatchNo());
	}
	
	/**
	 * 审批通过后，发起批量代付
	 * @param batchTrade
	 * @param smsCode
	 * @throws Exception
	 */
	@Transactional
	public BigDecimal audit(BatchTrade batchTrade, String smsCode) throws Exception {
		log.info("审核信息:{}", batchTrade);
		batchTradeMapper.audit(batchTrade);
		batchTrade = batchTradeMapper.selectByPrimaryKey(batchTrade.getId());
			//验证短信验证码
			PaySms sms = paySmsMapper.selectByBatchNo(batchTrade.getBatchNo());
			log.info("手工代付短信验证码信息：{}", sms);
			SysConfig config = sysConfigMapper.selectByPrimaryKey("verifyMaxTimes");
			int verifyMaxTimes = Integer.parseInt(config.getSysValue());
			config = sysConfigMapper.selectByPrimaryKey("expiryTime");
			int expiryTime = Integer.parseInt(config.getSysValue());
			if(sms.getVerfiyResult() != SmsConstant.VERIFY_SUCCESS 
					&& sms.getVerifyTimes() < verifyMaxTimes
					&& getDatePoor(new Date(), sms.getVerifyTime(), expiryTime)) {
				sms.setVerifyTimes(1 + sms.getVerifyTimes());
				sms.setVerifyTime(new Date());
				if(!smsCode.equals(sms.getSmsCode())) {
					log.info("手工代付短信验证码校验未通过");
					sms.setVerfiyResult(SmsConstant.VERIFY_FAIL);
					throw new TradeException("验证码不通过", SmsConstant.VERIFY_FAIL_CODE);
				} else {
					sms.setVerfiyResult(SmsConstant.VERIFY_SUCCESS);
				}
				paySmsMapper.updateVerifyResult(sms);
			} else {
				throw new TradeException("验证码不通过", SmsConstant.VERIFY_FAIL_CODE);
			}
			tradeMapper.updateByBatchNo(new Date(), 1, batchTrade.getBatchNo());
			//TODO 异步发起批量交易
			//判断商户余额
			List<Trade> trades = tradeMapper.selectByBatchNo(batchTrade.getBatchNo());
			BigDecimal total = BigDecimal.ZERO;
			for(Trade temp : trades) {
				total.add(temp.getTradeAmount()).add(add(temp.getMerchantFeeAmount(), temp.getLoanBenefit(), temp.getOrgBenefit()));
			}
			try {
				prepayInfoService.freezePrepayInfo(batchTrade.getPayerMerchantId(), total);
			} catch(Exception e1) {
				log.warn("冻结商户余额异常：", e1);
				//修改批量交易失败
				tradeMapper.updateStatusByBatchNo(batchTrade.getBatchNo(),
						e1.getMessage(), TradeConstant.PREPAY_BALANCE_NO_ENOUGH, TradeConstant.STATUS_FAIL, new Date());
				throw new TradeException(e1.getMessage(), TradeConstant.PREPAY_BALANCE_NO_ENOUGH);
			}
			return total;
	}
	
	/**
	 * 调用批量代付，返回触发结果
	 * @param batchTrade
	 * @return
	 */
	public BatchPayTradeResVO invokeBatchPay(BatchTrade batchTrade) {
		//发送渠道
		BatchPayTradeReqVO reqVO = new BatchPayTradeReqVO();
		//reqVO.setFileName(FileUtil.createFile(trades, batchTrade.getBatchNo()));
		reqVO.setMerchantId(batchTrade.getPayerMerchantId());
		reqVO.setOrderNo(batchTrade.getPlatBatchNo());
		//reqVO.setTotalAmt(totalAmount);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		reqVO.setTradeDate(sdf.format(new Date()));
		//reqVO.setTotalNum(Long.valueOf(count));
		log.info("批量付款入参：{}", reqVO);
		BatchPayTradeResVO resVO = payService.batchPay(reqVO);
		log.info("批量付款結果：{}", resVO);
		return resVO;
	}
	
	
	/**
	 * 处理代付触发结果
	 * @param resVO
	 * @param batchNo
	 * @param payerMerchantId
	 * @param total
	 * @throws Exception
	 */
	@Transactional
	public void dealBatchPayTradeRes(BatchPayTradeResVO resVO, String batchNo, 
			Integer payerMerchantId, BigDecimal total) throws Exception {
		if("1".equals(resVO.getRespCode())) {
			//触发失败，修改交易状态为失败
			tradeMapper.updateStatusByBatchNo(batchNo,
					resVO.getRespMsg(), resVO.getRespCode(), TradeConstant.STATUS_FAIL, new Date());
			prepayInfoService.unfreezePrepayInfo(payerMerchantId, total);
		}
	}
	
	/**
	 * 查询时间间隔
	 * @param endDate
	 * @param nowDate
	 * @param expiryDate
	 * @return
	 */
	private boolean getDatePoor(Date endDate, Date nowDate, int expiryDate) {
		if(nowDate == null) {
			return true;
		}
	    long diff = endDate.getTime() - nowDate.getTime();
	    return diff < 1000l * 60 * expiryDate;
	}	
}
