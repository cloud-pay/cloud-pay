package com.cloud.pay.channel.handler.bohai;

import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayQueryResult;

@Service("bohaiBatchTradePayQueryExecutor")
public class BohaiBatchTradePayQueryExecutor extends BohaiTradeExecutor<BohaiCloudBatchTradePayQueryParam,BohaiCloudBatchTradePayQueryResult>
      implements ITradePayExecutor<BatchPayTradeQueryReqVO, BatchPayTradeQueryResVO> {

	@Override
	public BatchPayTradeQueryResVO execute(BatchPayTradeQueryReqVO reqVO) {
		
		return null;
	}

}
