package com.cloud.pay.trade.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantFeeInfo;
import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantFeeInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantPrepayInfoMapper;
import com.cloud.pay.merchant.util.MD5;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.entity.MerchantRouteConf;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.mapper.MerchantRouteConfMapper;
import com.cloud.pay.trade.mapper.TradeMapper;
import com.cloud.pay.trade.util.ConvertUtil;

@Service
public class PayHandler {
	
	private Logger log = LoggerFactory.getLogger(PayHandler.class);
	
	@Autowired
	private ICloudApiService payService;
	
	@Autowired
	private MerchantFeeInfoMapper merchantFeeInfoMapper;

	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;
	
	@Autowired
	private MerchantBaseInfoMapper merchantBaseInfoMapper;
	
	@Autowired
	private MerchantRouteConfMapper merchantRouteConfMapper;
	
	@Autowired
	private PrepayInfoService prepayInfoService;
	
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
		//计算商户手续费
		BigDecimal merchantFee = BigDecimal.ZERO;
		BigDecimal[] fees = getFee(trade.getMerchantId(), trade.getTradeAmount());
		merchantFee = fees[0];
		if(TradeConstant.LOANING_YES == trade.getLoaning()) {
			//计算垫资分润
			trade.setLoanBenefit(fees[1]);
		}
		trade.setMerchantFeeAmount(merchantFee.add(trade.getLoanBenefit()));
		BigDecimal orgFee = getOrgFee(trade.getMerchantId(), trade.getTradeAmount());
		trade.setOrgBenefit(merchantFee.subtract(orgFee));
		log.info("保存交易信息：{}", trade);
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
		MerchantPrepayInfo info = merchantPrepayInfoMapper.lockByMerchantId(trade.getMerchantId());
		log.info("商户预缴户信息:{}", info);
		String digest = MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
				String.valueOf(info.getMerchantId()));
		if(!digest.equals(info.getDigest())) {
			log.info("商户{}预缴户被篡改", trade.getMerchantId());
			throw new TradeException("商户预缴户被篡改", TradeConstant.PREPAY_CHANGE);
		}
		if(info.getBalance().subtract(info.getFreezeAmount()).compareTo(trade.getTradeAmount().add(trade.getMerchantFeeAmount())) < 0) {
			log.warn("现有余额为:{}，小于提现金额：{}", 
					info.getBalance().subtract(info.getFreezeAmount()), trade.getTradeAmount());
			throw new TradeException("现有余额为" + info.getBalance().subtract(info.getFreezeAmount()), null);
		}
		info.setFreezeAmount(info.getFreezeAmount().add(trade.getMerchantFeeAmount()).add(trade.getTradeAmount()));
		info.setDigest(MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
				String.valueOf(info.getMerchantId())));
		log.info("冻结商户信息：{}", info);
		merchantPrepayInfoMapper.updateByPrimaryKey(info);
	}
	
	/**
	 * 调用渠道单笔扣款
	 * @author dbnaxlc
	 * @date 2018年9月12日 下午5:22:55
	 */
	public PayTradeResVO invokePay(Trade trade) {
		PayTradeReqVO reqVO = new PayTradeReqVO();
		reqVO.setAmt(trade.getTradeAmount());
		//TODO 路由具体渠道时设置
//		reqVO.setMerchantNo(merchantNo);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		reqVO.setTradeDate(sdf.format(new Date()));
		reqVO.setAmt(trade.getTradeAmount());
		reqVO.setOrderNo(trade.getOrderNo());
		reqVO.setPayeeAccount(trade.getPayeeBankCard());
		reqVO.setPayeeBankCode(trade.getPayeeBankCode());
		reqVO.setPayeeName(trade.getPayeeName());
		reqVO.setPostscript(trade.getRemark());
		log.info("调用渠道入参：{}", reqVO);
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
		log.info("处理商户预缴户");
		//TODO 设置渠道返回结果
//		trade.setStatus();
		trade.setChannelId(resVO.getChannelId());
		trade.setReturnCode(resVO.getRespCode());
		trade.setReturnInfo(resVO.getRespMsg());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		trade.setReconDate(sdf.parse(resVO.getAccountDate()));
		trade.setTradeConfirmTime(new Date());
		if(TradeConstant.STATUS_SUCCESS == trade.getStatus()) {
			List<Integer> merchantIds = new ArrayList<Integer>();
			Integer orgId = null;
			Integer loanId = null;
			merchantIds.add(trade.getMerchantId());
			merchantIds.add(1);//平台账户
			if(trade.getOrgBenefit() != null) {
				MerchantBaseInfo baseInfo =  merchantBaseInfoMapper.selectByPrimaryKey(trade.getMerchantId());
				if(baseInfo.getOrgId() != null) {
					orgId = baseInfo.getOrgId();
					merchantIds.add(baseInfo.getOrgId());
				}
			}
			if(trade.getLoanBenefit() != null) {
				MerchantRouteConf conf = merchantRouteConfMapper.selectByMerchantIdAndChannelId(trade.getMerchantId(), trade.getChannelId());
				if(conf != null && conf.getLoaningOrgId() != null) {
					loanId = conf.getLoaningOrgId();
					merchantIds.add(conf.getLoaningOrgId());
				}
			}
			List<MerchantPrepayInfo> infos = merchantPrepayInfoMapper.lockByMerchantIds(merchantIds);
			Map<Integer, MerchantPrepayInfo> maps = ConvertUtil.convertMap(infos);
			/** 商户资金变动 */
			prepayInfoService.savePrepayInfoJournal(maps.get(trade.getMerchantId()), TradeConstant.TRADE_FEE, trade.getTradeAmount(), TradeConstant.CREDIT, trade.getId());			
			/** 商户手续费资金变动 */
			prepayInfoService.savePrepayInfoJournal(maps.get(trade.getMerchantId()), TradeConstant.HADNING_FEE, trade.getMerchantFeeAmount(), TradeConstant.CREDIT, trade.getId());
			BigDecimal platFee = trade.getMerchantFeeAmount();
			/** 机构资金变动 */
			if(orgId != null) {
				prepayInfoService.savePrepayInfoJournal(maps.get(orgId), TradeConstant.HADNING_FEE, trade.getOrgBenefit(), TradeConstant.DEBIT, trade.getId());
				platFee = platFee.subtract(trade.getOrgBenefit());
			}
			/** 垫资机构资金变动 */
			if(loanId != null) {
				prepayInfoService.savePrepayInfoJournal(maps.get(loanId), TradeConstant.HADNING_FEE, trade.getLoanBenefit(), TradeConstant.DEBIT, trade.getId());
				platFee = platFee.subtract(trade.getLoanBenefit());
			}
			/** 平台资金变动 */
			prepayInfoService.savePrepayInfoJournal(maps.get(1), TradeConstant.HADNING_FEE, platFee, TradeConstant.DEBIT, trade.getId());
			tradeMapper.updateStatus(trade);
		} else if(TradeConstant.STATUS_FAIL == trade.getStatus()) {
			MerchantPrepayInfo info = merchantPrepayInfoMapper.lockByMerchantId(trade.getMerchantId());
			log.info("商户预缴户信息:{}", info);
			String digest = MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
					String.valueOf(info.getMerchantId()));
			if(!digest.equals(info.getDigest())) {
				log.info("商户{}预缴户被篡改", trade.getMerchantId());
				throw new TradeException("商户预缴户被篡改", TradeConstant.PREPAY_CHANGE);
			}
			info.setFreezeAmount(info.getFreezeAmount().subtract(trade.getMerchantFeeAmount()).subtract(trade.getTradeAmount()));
			info.setDigest(MD5.md5(String.valueOf(info.getBalance()) + "|" + info.getFreezeAmount() , 
					String.valueOf(info.getMerchantId())));
			log.info("回滚冻结金额,{}", info);
			merchantPrepayInfoMapper.updateByPrimaryKey(info);
			tradeMapper.updateStatus(trade);
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
	public BigDecimal getOrgFee(Integer merchantId, BigDecimal tradeAmount) {
		BigDecimal fee = BigDecimal.ZERO;
		MerchantBaseInfo baseInfo =  merchantBaseInfoMapper.selectByPrimaryKey(merchantId);
		if(baseInfo.getOrgId() != null) {
			MerchantFeeInfo feeInfo = merchantFeeInfoMapper.selectByMerchantId(baseInfo.getOrgId());
			if(feeInfo != null) {
				if(MerchantConstant.PER_RATE == feeInfo.getPayFeeType()) {
					fee = tradeAmount.multiply(feeInfo.getPayFee()).divide(new BigDecimal(100), RoundingMode.HALF_UP);
				} else if(MerchantConstant.PER == feeInfo.getPayFeeType()) {
					fee = feeInfo.getPayFee();
				} 
			}
		}
		return fee;
	}
}
