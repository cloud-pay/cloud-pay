package com.cloud.pay.channel.service.bohai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.service.ITradePayExecutor;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;

/**
 * 渤海代付接口（参数为泛型）
 * @author wangy
 */
@Service("bohaiTradePayExecutor")
public class BohaiTradePayExecutor implements ITradePayExecutor<PayTradeReqVO> {
   
	private Logger log = LoggerFactory.getLogger(BohaiTradePayExecutor.class);
	
	@Override
	public BaseTradeResVO execute(PayTradeReqVO reqVO) {
		log.info("代付-渤海代付-请求参数：{}",reqVO);
		return null;
	}

}
