package com.cloud.pay.channel.service;

import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;

/**
 * 渠道接口入口
 * @author wangy
 */
public interface ICloudApiService {
    
	/**
	 * 代付接口
	 * @param tradeReq
	 * @return
	 */
	PayTradeResVO pay(PayTradeReqVO reqVO);
	
	/**
	 * 代付结果查询
	 * @param tradeReq
	 * @return
	 */
	PayTradeQueryResVO queryPay(PayTradeQueryReqVO tradeReq);
	
	
	/**
	 * 单笔银联代付
	 * @param reqVO
	 * @return
	 */
	PayTradeResVO unionPay(PayUnionTradeReqVO reqVO);
}
