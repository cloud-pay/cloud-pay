package com.cloud.pay.channel.handler;

import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.CloudTradeParam;

/**
 * 代付接口
 * @author wangy
 */
public interface ITradePayExecutor<M extends BaseTradeReqVO> {
  
	/**
	 * 执行请求
	 * @param reqVO
	 * @return
	 */
	BaseTradeResVO execute(M reqVO);
	
	/**
	 * 构建渠道请求参数
	 * @param reqVO
	 * @return
	 */
	<T extends CloudTradeParam> T createParam(M reqVO);
}
