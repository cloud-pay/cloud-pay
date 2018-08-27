package com.cloud.pay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.TradePayTypeHandlerFactory;
import com.cloud.pay.channel.contants.ChannelType;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.service.ICloudApiService;

/**
 * 渠道接口实现类
 * @author wangy
 */
@Service
public class CloudApiServiceImpl implements ICloudApiService {
	
	private Logger log = LoggerFactory.getLogger(CloudApiServiceImpl.class);

	@Autowired
	private TradePayTypeHandlerFactory tradePayTypeHandlerFactory; 
	
	@Override
	public BaseTradeResVO pay(PayTradeReqVO tradeReq) {
		log.info("渠道接口：收到代付请求，请求参数：{}",tradeReq);
		//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
		BaseTradeResVO resVO = tradePayTypeHandlerFactory.getTradePayHandler(ChannelType.BOHAI.getChannelCode()).execute(tradeReq);
		log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
		return resVO;
	}

	@Override
	public BaseTradeResVO queryPay(PayTradeQueryReqVO tradeReq) {
		log.info("渠道接口：收到代付结果查询请求，请求参数：{}",tradeReq);
		//根据传入的订单交易渠道获取渠道信息
		BaseTradeResVO resVO = tradePayTypeHandlerFactory.getTradePayQueryHandler(ChannelType.getChannelCodeByChannelNo(tradeReq.getChannelNo())).execute(tradeReq);
		log.info("渠道接口：代付结果查询结束，响应参数：{}",resVO);
		return resVO;
	}
   
	
}
