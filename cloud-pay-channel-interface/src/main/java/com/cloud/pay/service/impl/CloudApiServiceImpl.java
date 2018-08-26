package com.cloud.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.TradePayTypeHandlerFactory;
import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVo;
import com.cloud.pay.service.ICloudApiService;

@Service
public class CloudApiServiceImpl implements ICloudApiService {

	@Autowired
	private TradePayTypeHandlerFactory tradePayTypeHandlerFactory; 
	
	@Override
	public BaseTradeResVo trade(BaseTradeReqVO tradeReq) {
		//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
		BaseTradeResVo resVO = tradePayTypeHandlerFactory.getTradePayHandler("bohai_trade_pay").execute(tradeReq);
		return resVO;
	}

}
