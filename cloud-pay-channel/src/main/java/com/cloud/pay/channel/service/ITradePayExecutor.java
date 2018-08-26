package com.cloud.pay.channel.service;

import org.springframework.stereotype.Component;

import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVo;

/**
 * 代付接口
 * @author wangy
 */
@Component
public interface ITradePayExecutor {
  
	/**
	 * 
	 * @param reqVO
	 * @return
	 */
	BaseTradeResVo execute(BaseTradeReqVO reqVO);
}
