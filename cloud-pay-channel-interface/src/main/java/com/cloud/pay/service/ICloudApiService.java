package com.cloud.pay.service;

import org.springframework.stereotype.Component;

import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVo;

/**
 * 渠道接口入口
 * @author wangy
 */
@Component
public interface ICloudApiService {
    
	/**
	 * 交易接口
	 * @param tradeReq 请求参数和响应参数先以基类代替，实际编码时替换为实际接口的请求参数和响应参数
	 * @return
	 */
	BaseTradeResVo trade(BaseTradeReqVO tradeReq);
}
