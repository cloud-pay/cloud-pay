package com.cloud.pay.trade.service;

import org.springframework.stereotype.Service;

import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPayRetryReqVO;
import com.cloud.pay.channel.vo.BatchPaySingleQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;

@Service
public class CloudPayServiceMock implements ICloudApiService {

	@Override
	public PayTradeResVO pay(PayTradeReqVO reqVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayTradeQueryResVO queryPay(PayTradeQueryReqVO tradeReq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayTradeResVO unionPay(PayUnionTradeReqVO reqVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReconDownFileResVO downReconFile(ReconDownFileReqVO reqVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BatchPayTradeResVO batchPay(BatchPayTradeReqVO reqVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BatchPayTradeQueryResVO batchPayQuery(BatchPayTradeQueryReqVO reqVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseTradeResVO batchPaySingleQuery(BatchPaySingleQueryReqVO reqVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseTradeResVO batchPayRetry(BatchPayRetryReqVO reqVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
