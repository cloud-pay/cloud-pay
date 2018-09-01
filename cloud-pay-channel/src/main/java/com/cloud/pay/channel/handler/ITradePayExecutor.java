package com.cloud.pay.channel.handler;

import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVO;

/**
 * 代付接口
 * @author wangy
 */
public interface ITradePayExecutor<M extends BaseTradeReqVO,R extends BaseTradeResVO> {
  
	/**
	 * 执行请求
	 * @param reqVO
	 * @return
	 */
	R execute(M reqVO);
}
