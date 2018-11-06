package com.cloud.pay.channel.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.TradePayTypeHandlerFactory;
import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.utils.ValidationUtils;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPayRetryReqVO;
import com.cloud.pay.channel.vo.BatchPaySingleQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.contants.ChannelType;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.merchant.entity.MerchantChannel;
import com.cloud.pay.merchant.mapper.MerchantChannelMapper;

/**
 * 渠道接口实现类
 * @author wangy
 */
@Service("cloudApiService")
public class CloudApiServiceImpl implements ICloudApiService {
	
	private Logger log = LoggerFactory.getLogger(CloudApiServiceImpl.class);

	@Autowired
	private TradePayTypeHandlerFactory tradePayTypeHandlerFactory; 
	
	@Autowired
	private MerchantChannelMapper merchantChannelMapper;
	
	@Override
	public PayTradeResVO pay(PayTradeReqVO tradeReq) {
		try {
			log.info("渠道接口：收到代付请求，请求参数：{}",tradeReq);
			PayTradeResVO resVO = null;
			try {
				ValidationUtils.validate(tradeReq);
			}catch(CloudApiException e) {
				log.error("参数校验失败:{}",e.getMessage());
				resVO = new PayTradeResVO(e.getErrorCode(),e.getMessage());
				return resVO;
			}
			//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
			List<MerchantChannel> merchantChannels = merchantChannelMapper.selectByMerchantId(tradeReq.getMerchantId());
		    if(null == merchantChannels || merchantChannels.size() <=0 ) {
		    	log.error("商户未配置渠道信息");
				resVO = new PayTradeResVO(ChannelErrorCode.ERROR_0003,"商户未配置渠道信息");
				return resVO;
		    }
		    MerchantChannel merchantChannel = merchantChannels.get(0);
			ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradePayHandler(ChannelType.getChannelByChannelId(merchantChannel.getChannelId()));
			resVO = (PayTradeResVO) tradePayExecutor.execute(tradeReq);
			resVO.setChannelId(ChannelType.BOHAI.getChannelId());
			log.info("渠道接口：代付，响应参数：{}",resVO);
			return resVO;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
  

	@Override
	public PayTradeQueryResVO queryPay(PayTradeQueryReqVO tradeReq) {
		log.info("渠道接口：收到代付结果查询请求，请求参数：{}",tradeReq);
		PayTradeQueryResVO result  = null;
		try {
			ValidationUtils.validate(tradeReq);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			result = new PayTradeQueryResVO(e.getErrorCode(),e.getMessage());
			return result;
		}
		//TODO ...根据传入的订单交易渠道获取渠道信息
		ITradePayExecutor tradePayExecutor = 	tradePayTypeHandlerFactory.getTradePayQueryHandler(ChannelType.getChannelByChannelId(tradeReq.getChannelId()));
		 result = (PayTradeQueryResVO) tradePayExecutor.execute(tradeReq);
		log.info("渠道接口：代付结果查询结束，响应参数：{}",result);
		return result;
	}



	@Override
	public PayTradeResVO unionPay(PayUnionTradeReqVO reqVO) {
		log.info("渠道接口：单笔银联代付，请求参数：{}",reqVO);
		PayTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new PayTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
	    List<MerchantChannel> merchantChannels = merchantChannelMapper.selectByMerchantId(reqVO.getMerchantId());
		if(null == merchantChannels) {
			log.error("商户未配置渠道信息");
			resVO = new PayTradeResVO(ChannelErrorCode.ERROR_0003,"商户未配置渠道信息");
			return resVO;
		}
		MerchantChannel merchantChannel = merchantChannels.get(0);
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradeUnionPayHandler(
				ChannelType.getChannelByChannelId(merchantChannel.getChannelId()));
		resVO = (PayTradeResVO) tradePayExecutor.execute(reqVO);
		resVO.setChannelId(ChannelType.BOHAI.getChannelId());
		log.info("渠道接口：单笔银联代付，响应参数：{}",resVO);
		return resVO;
	}



	@Override
	public ReconDownFileResVO downReconFile(ReconDownFileReqVO reqVO) {
		log.info("渠道接口：下载对账文件，请求参数：{}",reqVO);
		ReconDownFileResVO resVO = null;
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradeDownReconFileHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		resVO = (ReconDownFileResVO) tradePayExecutor.execute(reqVO);
		log.info("渠道接口：下载对账文件，响应结果：{}",resVO);
		return resVO;
	}



	@Override
	public BatchPayTradeResVO batchPay(BatchPayTradeReqVO reqVO) {
		log.info("渠道接口，批量代付，请求参数：{}",reqVO);
		BatchPayTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BatchPayTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
	    List<MerchantChannel> merchantChannels = merchantChannelMapper.selectByMerchantId(reqVO.getMerchantId());
		if(null == merchantChannels) {
			log.error("商户未配置渠道信息");
			resVO = new BatchPayTradeResVO(ChannelErrorCode.ERROR_0003,"商户未配置渠道信息");
			return resVO;
		}
		MerchantChannel merchantChannel = merchantChannels.get(0);
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchTraeHandler(ChannelType.getChannelByChannelId(merchantChannel.getChannelId()));
		resVO = (BatchPayTradeResVO) tradePayExecutor.execute(reqVO);
		resVO.setChannelId(ChannelType.BOHAI.getChannelId());
		log.info("渠道接口，批量代付，响应结果：{}",resVO);
		return resVO;
	}



	@Override
	public BatchPayTradeQueryResVO batchPayQuery(BatchPayTradeQueryReqVO reqVO) {
		log.info("渠道接口，批量代付结果查询，请求参数：{}",reqVO);
		BatchPayTradeQueryResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BatchPayTradeQueryResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchTradeQueryHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		resVO = (BatchPayTradeQueryResVO) tradePayExecutor.execute(reqVO);
		log.info("渠道接口，批量代付结果查询，响应结果:{}",resVO);
		return resVO;
	}



	@Override
	public BaseTradeResVO batchPaySingleQuery(BatchPaySingleQueryReqVO reqVO) {
		log.info("渠道接口，批量代付单笔结果查询，请求参数：{}",reqVO);
		BaseTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BaseTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchSingleQueryHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		resVO = tradePayExecutor.execute(reqVO);
		log.info("渠道接口，批量代付单笔结果查询，响应结果：{}",resVO);
		return resVO;
	}



	@Override
	public BaseTradeResVO batchPayRetry(BatchPayRetryReqVO reqVO) {
		log.info("渠道接口，批量代付触发查询，请求参数：{}",reqVO);
		BaseTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BaseTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchPayRetryHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		resVO = tradePayExecutor.execute(reqVO);
		log.info("渠道接口，批量代付触发查询，响应结果：{}",resVO);
		return resVO;
	}

}
