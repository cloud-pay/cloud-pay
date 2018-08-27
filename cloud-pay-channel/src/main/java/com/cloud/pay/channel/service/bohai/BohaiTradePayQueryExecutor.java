package com.cloud.pay.channel.service.bohai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.service.ITradePayExecutor;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;

/**
 * 渤海代付查询接口
 * @author wangy
 */
@Service("bohaiTradeQueryExecutor")
public class BohaiTradePayQueryExecutor implements ITradePayExecutor<PayTradeQueryReqVO> {

	private Logger log = LoggerFactory.getLogger(BohaiTradePayQueryExecutor.class);
	
	@Override
	public BaseTradeResVO execute(PayTradeQueryReqVO reqVO) {
		log.info("代付结果查询-渤海代付查询-请求参数：{}",reqVO);
		return null;
	}

	

}
