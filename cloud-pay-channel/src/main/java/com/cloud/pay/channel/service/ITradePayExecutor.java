package com.cloud.pay.channel.service;

import org.springframework.stereotype.Component;

import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVO;

/**
 * 代付接口
 * @author wangy
 */
@Component
public interface ITradePayExecutor<M extends BaseTradeReqVO> {
  
	/**
	 * 
	 * @param reqVO
	 * @return
	 */
	BaseTradeResVO execute(M reqVO);
	
}
