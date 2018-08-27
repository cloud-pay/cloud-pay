package com.cloud.pay.channel.service;

import org.springframework.stereotype.Component;

import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;

/**
 * 渠道接口入口
 * @author wangy
 */
@Component
public interface ICloudApiService {
    
	/**
	 * 代付接口
	 * @param tradeReq 请求参数和响应参数先以基类代替，实际编码时替换为实际接口的请求参数和响应参数
	 * @return
	 */
	BaseTradeResVO pay(PayTradeReqVO reqVO);
	
	/**
	 * 代付结果查询
	 * @param tradeReq
	 * @return
	 */
	BaseTradeResVO queryPay(PayTradeQueryReqVO tradeReq);
}
