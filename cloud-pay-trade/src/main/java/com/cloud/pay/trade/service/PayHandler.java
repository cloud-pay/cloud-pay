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
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;
import com.cloud.pay.common.utils.TableCodeUtils;
import com.cloud.pay.merchant.constant.MerchantConstant;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantFeeInfo;
import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.entity.MerchantRouteConf;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantFeeInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantRouteConfMapper;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.mapper.TradeMapper;

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
		trade.setStatus(TradeConstant.STATUS_PROCESSING);
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
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMddHHmmss");
		String platOrderNo = TableCodeUtils.getTableCode(trade.getId(), sdfTime.format(new Date()));
		//生成平台订单号
		trade.setPlatOrderNo(platOrderNo);
		tradeMapper.updateByPrimaryKeySelective(trade);
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
		BigDecimal freezeAmount = trade.getMerchantFeeAmount().add(trade.getTradeAmount());
		prepayInfoService.freezePrepayInfo(trade.getMerchantId(), freezeAmount);
	}
	
	/**
	 * 调用渠道单笔扣款
	 * @author dbnaxlc
	 * @date 2018年9月12日 下午5:22:55
	 */
	public PayTradeResVO invokePay(Trade trade) {
		PayUnionTradeReqVO reqVO = new PayUnionTradeReqVO();
		reqVO.setAmt(trade.getTradeAmount());
		//TODO 路由具体渠道时设置
//		reqVO.setMerchantNo(merchantNo);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		reqVO.setTradeDate(sdf.format(new Date()));
		reqVO.setAmt(trade.getTradeAmount());
		reqVO.setOrderNo(trade.getPlatOrderNo());
		reqVO.setPayeeAccount(trade.getPayeeBankCard());
		//reqVO.setPayeeBankCode(trade.getPayeeBankCode());
		reqVO.setPayeeName(trade.getPayeeName());
		reqVO.setPostscript(trade.getRemark());
		reqVO.setMerchantId(trade.getMerchantId());
		log.info("调用渠道入参：{}", reqVO);
		PayTradeResVO resVO = payService.unionPay(reqVO);//payService.pay(reqVO);
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
		if("1".equals(resVO.getRespCode())) {
			trade.setStatus(3); 
		}else {
			if(0 == resVO.getStatus()) {
				trade.setStatus(TradeConstant.STATUS_SUCCESS);
			} else if(1 == resVO.getStatus()){
				trade.setStatus(TradeConstant.STATUS_FAIL);
			} else if(99 == resVO.getStatus()){
				trade.setStatus(TradeConstant.STATUS_PROCESSING);
			}
			trade.setChannelId(resVO.getChannelId());
			trade.setReturnCode(resVO.getRespCode());
			trade.setReturnInfo(resVO.getRespMsg());
			if(StringUtils.isNotBlank(resVO.getAccountDate())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				trade.setReconDate(sdf.parse(resVO.getAccountDate()));
			}
			trade.setTradeConfirmTime(new Date());
		}
		
		if(TradeConstant.STATUS_SUCCESS == trade.getStatus()) {
			saveJournal(trade);
		} else if(TradeConstant.STATUS_FAIL == trade.getStatus()) {
			BigDecimal unfreezeAmount = trade.getMerchantFeeAmount().add(trade.getTradeAmount());
			prepayInfoService.unfreezePrepayInfo(trade.getMerchantId(), unfreezeAmount);
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
	@Transactional
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
		return new BigDecimal[]{payFee.setScale(2,BigDecimal.ROUND_HALF_UP), loanFee.setScale(2,BigDecimal.ROUND_HALF_UP)};
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
	@Transactional
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
	
	/**
	 * 调用渠道单笔查询
	 * @author dbnaxlc
	 */
	public PayTradeQueryResVO invokeQuery(Trade trade) {
		PayTradeQueryReqVO tradeReq = new PayTradeQueryReqVO();
		
		tradeReq.setMerchantId(trade.getMerchantId());
		tradeReq.setOrderNo(trade.getPlatOrderNo());
		tradeReq.setChannelId(trade.getChannelId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		tradeReq.setTradeDate(sdf.format(trade.getTradeTime()));
		log.info("调用渠道查询入参：{}", tradeReq);
		PayTradeQueryResVO resVO = payService.queryPay(tradeReq);
		log.info("订单号{}调用渠道查询返回结果{}", trade.getOrderNo(), resVO);
		return resVO;
	}
	
	/**
	 * 交易成功后保存账户流水并修改交易状态
	 * @param trade
	 * @throws Exception 
	 */
	@Transactional
	public void saveJournal(Trade trade) throws Exception {
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
		Map<Integer, MerchantPrepayInfo> maps = prepayInfoService.lockByMerchantIds(merchantIds);
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
	}
}
