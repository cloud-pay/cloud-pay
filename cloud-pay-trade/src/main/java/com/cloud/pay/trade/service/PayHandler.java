package com.cloud.pay.trade.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.cloud.pay.merchant.constant.MerchantConstant;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantFeeInfo;
import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.mapper.MerchantBankInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantFeeInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantPrepayInfoMapper;
import com.cloud.pay.merchant.util.MD5;
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
	private MerchantFeeInfoMapper merchantFeeInfoMapper;

	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;
	
	@Autowired
	private MerchantBaseInfoMapper merchantBaseInfoMapper;
	
	/**
	 * 保存单笔代付
	 * @author dbnaxlc
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
	 * 冻结商户手续费
	 * @author dbnaxlc
	 * @date 2018年10月10日 下午3:22:08
	 * @param trade
	 * @throws Exception 
	 */
	@Transactional
	public void freezeMerchantFee(Trade trade) throws Exception {
		BigDecimal maxFee = getMaxFee(trade.getMerchantId(), trade.getTradeAmount());
		MerchantPrepayInfo info = merchantPrepayInfoMapper.lockByMerchantId(trade.getMerchantId());
		log.info("商户预缴户信息:{}", info);
		String digest = MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
				String.valueOf(info.getMerchantId()));
		if(!digest.equals(info.getDigest())) {
			log.info("商户{}预缴户被篡改", trade.getMerchantId());
			throw new TradeException("商户预缴户被篡改", TradeConstant.PREPAY_CHANGE);
		}
		info.setFreezeAmount(info.getFreezeAmount().add(maxFee));
		info.setDigest(MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
				String.valueOf(info.getMerchantId())));
		merchantPrepayInfoMapper.updateByPrimaryKey(info);
		trade.setMerchantFeeAmount(maxFee);
	}
	
	/**
	 * 调用渠道单笔扣款
	 * @author dbnaxlc
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
//		reqVO.setPayerAccount(bankInfo.getBankCardNo());
//		reqVO.setPayerName(bankInfo.getBankAccountName());
		reqVO.setPostscript(trade.getRemark());
		PayTradeResVO resVO = payService.pay(reqVO);
		log.info("订单号{}调用渠道返回结果{}", trade.getOrderNo(), resVO);
		return resVO;
	}
	
	/**
	 * 调用渠道返回后修改交易状态
	 * @author dbnaxlc
	 * @throws Exception 
	 * @date 2018年9月12日 下午5:22:38
	 */
	@Transactional
	public void updateTradeStatus(Trade trade, PayTradeResVO resVO) throws Exception {
		//TODO 设置渠道返回结果
//		trade.setStatus();
//		trade.setChannelId(channelId);
//		trade.setReturnCode(returnCode);
//		trade.setReturnInfo(returnInfo);
//		trade.setReconDate(resVO.getAccountDate());
		trade.setTradeConfirmTime(new Date());
		BigDecimal merchantFee = BigDecimal.ZERO;
		BigDecimal loanBenefit = BigDecimal.ZERO;
		if(TradeConstant.STATUS_SUCCESS == trade.getStatus()) {
			BigDecimal[] fees = getFee(trade.getMerchantId(), trade.getTradeAmount());
			if(TradeConstant.LOANING_NO == trade.getLoaning()) {
				merchantFee = fees[0];
			} else {
				merchantFee = fees[1];
				loanBenefit = fees[1].subtract(fees[0]);
			}
			MerchantPrepayInfo info = merchantPrepayInfoMapper.lockByMerchantId(trade.getMerchantId());
			log.info("商户预缴户信息:{}", info);
			String digest = MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
					String.valueOf(info.getMerchantId()));
			if(!digest.equals(info.getDigest())) {
				log.info("商户{}预缴户被篡改", trade.getMerchantId());
				throw new TradeException("商户预缴户被篡改", TradeConstant.PREPAY_CHANGE);
			}
			info.setFreezeAmount(info.getFreezeAmount().subtract(trade.getMerchantFeeAmount()));
			info.setDigest(MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
					String.valueOf(info.getMerchantId())));
			merchantPrepayInfoMapper.updateByPrimaryKey(info);
		}
		trade.setMerchantFeeAmount(merchantFee);
		trade.setLoanBenefit(loanBenefit);
		BigDecimal orgFee = getOrgFee(trade.getMerchantId(), trade.getTradeAmount(), trade.getLoaning());
		trade.setOrgBenefit(merchantFee.subtract(loanBenefit).subtract(orgFee));
		tradeMapper.updateStatus(trade);
		
	}
	
	/**
	 * 获取商户最大费率
	 * @date 2018年10月10日 下午2:53:58
	 * @param merchantId
	 * @return
	 */
	public BigDecimal getMaxFee(Integer merchantId, BigDecimal tradeAmount) {
		BigDecimal[] fees = getFee(merchantId, tradeAmount);
		if(fees[0].compareTo(fees[1]) >= 0) {
			return fees[0];
		} else {
			return fees[1];
		}
	}
	
	/**
	 * 获取商户手续费组
	 * @author dbxaxlc
	 * @date 2018年10月10日 下午4:36:20
	 * @param merchantId
	 * @param tradeAmount
	 * @return
	 */
	public BigDecimal[] getFee(Integer merchantId, BigDecimal tradeAmount) {
		MerchantFeeInfo feeInfo = merchantFeeInfoMapper.selectByMerchantId(merchantId);
		BigDecimal payFee = BigDecimal.ZERO;
		if(MerchantConstant.PER_RATE == feeInfo.getPayFeeType()) {
			payFee = tradeAmount.multiply(feeInfo.getPayFee()).divide(new BigDecimal(100), RoundingMode.HALF_UP);
		} else if(MerchantConstant.PER == feeInfo.getPayFeeType()) {
			payFee = feeInfo.getPayFee();
		} 
		BigDecimal loanFee = BigDecimal.ZERO;
		if(MerchantConstant.PER_RATE == feeInfo.getLoanFeeType()) {
			loanFee = tradeAmount.multiply(feeInfo.getLoanFee()).divide(new BigDecimal(100), RoundingMode.HALF_UP);
		} else if(MerchantConstant.PER == feeInfo.getLoanFeeType()) {
			loanFee = feeInfo.getLoanFee();
		}
		return new BigDecimal[]{payFee, loanFee};
	}
	
	/**
	 * 获取机构手续费
	 * @author dbnaxlc
	 * @date 2018年10月10日 下午5:09:00
	 * @param merchantId
	 * @param tradeAmount
	 * @param loaning
	 * @return
	 */
	public BigDecimal getOrgFee(Integer merchantId, BigDecimal tradeAmount, Integer loaning) {
		BigDecimal fee = BigDecimal.ZERO;
		MerchantBaseInfo baseInfo =  merchantBaseInfoMapper.selectByPrimaryKey(merchantId);
		if(baseInfo.getOrgId() != null) {
			MerchantFeeInfo feeInfo = merchantFeeInfoMapper.selectByMerchantId(baseInfo.getOrgId());
			//不垫资
			if(TradeConstant.LOANING_NO == loaning) {
				if(MerchantConstant.PER_RATE == feeInfo.getPayFeeType()) {
					fee = tradeAmount.multiply(feeInfo.getPayFee()).divide(new BigDecimal(100), RoundingMode.HALF_UP);
				} else if(MerchantConstant.PER == feeInfo.getPayFeeType()) {
					fee = feeInfo.getPayFee();
				} 
			} else {
				if(MerchantConstant.PER_RATE == feeInfo.getLoanFeeType()) {
					fee = tradeAmount.multiply(feeInfo.getLoanFee()).divide(new BigDecimal(100), RoundingMode.HALF_UP);
				} else if(MerchantConstant.PER == feeInfo.getLoanFeeType()) {
					fee = feeInfo.getLoanFee();
				}
			}
		}
		return fee;
	}
}
