package com.cloud.pay.channel.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.TradePayTypeHandlerFactory;
import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.utils.ValidationUtils;
import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryResult;
import com.cloud.pay.common.contants.ChannelType;
import com.cloud.pay.common.exception.CloudApiExcetion;

/**
 * 渠道接口实现类
 * @author wangy
 */
@Service("cloudApiService")
public class CloudApiServiceImpl implements ICloudApiService {
	
	private Logger log = LoggerFactory.getLogger(CloudApiServiceImpl.class);

	@Autowired
	private TradePayTypeHandlerFactory tradePayTypeHandlerFactory; 
	
	@Override
	public PayTradeResVO pay(PayTradeReqVO tradeReq) {
		log.info("渠道接口：收到代付请求，请求参数：{}",tradeReq);
		PayTradeResVO resVO = null;
		try {
			ValidationUtils.validate(tradeReq);
		}catch(CloudApiExcetion e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new PayTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		//TODO .....根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradePayHandler(ChannelType.BOHAI.getChannelENName());
		resVO = (PayTradeResVO) tradePayExecutor.execute(tradeReq);
		log.info("渠道接口：代付，响应参数：{}",resVO);
		return resVO;
	}
	
  

	@Override
	public PayTradeQueryResVO queryPay(PayTradeQueryReqVO tradeReq) {
		log.info("渠道接口：收到代付结果查询请求，请求参数：{}",tradeReq);
		PayTradeQueryResVO result  = null;
		try {
			ValidationUtils.validate(tradeReq);
		}catch(CloudApiExcetion e) {
			log.error("参数校验失败:{}",e.getMessage());
			result = new PayTradeQueryResVO(e.getErrorCode(),e.getMessage());
			return result;
		}
		//TODO ...根据传入的订单交易渠道获取渠道信息
		ITradePayExecutor tradePayExecutor = 	tradePayTypeHandlerFactory.getTradePayQueryHandler(ChannelType.getChannelByChannelCode(tradeReq.getChannelCode()));
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
		}catch(CloudApiExcetion e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new PayTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradeUnionPayHandler(ChannelType.BOHAI.getChannelENName());
		resVO = (PayTradeResVO) tradePayExecutor.execute(reqVO);
		log.info("渠道接口：单笔银联代付，响应参数：{}",resVO);
		return resVO;
	}



	@Override
	public ReconDownFileResVO downReconFile(ReconDownFileReqVO reqVO) {
		log.info("渠道接口：下载对账文件，请求参数：{}",reqVO);
		ReconDownFileResVO resVO = null;
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradeDownReconFileHandler(ChannelType.getChannelByChannelCode(reqVO.getChannelCode()));
		resVO = (ReconDownFileResVO) tradePayExecutor.execute(reqVO);
		log.info("渠道接口：下载对账文件，响应结果：{}",resVO);
		return resVO;
	}

}
