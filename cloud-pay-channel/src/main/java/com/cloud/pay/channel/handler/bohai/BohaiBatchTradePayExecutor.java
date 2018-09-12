package com.cloud.pay.channel.handler.bohai;

import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayResult;

@Service("bohaiBatchTradePayExecutor")
public class BohaiBatchTradePayExecutor extends BohaiTradeExecutor<BohaiCloudBatchTradePayParam, BohaiCloudBatchTradePayResult>
      implements ITradePayExecutor<BatchPayTradeReqVO, BatchPayTradeResVO> {

	@Override
	public BatchPayTradeResVO execute(BatchPayTradeReqVO reqVO) {
		
		return null;
	}

}
