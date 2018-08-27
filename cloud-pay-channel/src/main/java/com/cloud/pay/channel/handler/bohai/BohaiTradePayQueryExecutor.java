package com.cloud.pay.channel.handler.bohai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.CloudTradeParam;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;

/**
 * 渤海代付查询接口
 * @author wangy
 */
@Service("bohaiTradeQueryExecutor")
public class BohaiTradePayQueryExecutor implements ITradePayExecutor<PayTradeQueryReqVO> {

	private Logger log = LoggerFactory.getLogger(BohaiTradePayQueryExecutor.class);
	
	@Autowired
	private BohaiPayHelper bohaiPayHelper;
	
	@Override
	public BaseTradeResVO execute(PayTradeQueryReqVO reqVO) {
		log.info("代付结果查询-渤海代付查询-请求参数：{}",reqVO);
		BohaiCloudTradeQueryParam queryParam = createParam(reqVO);
		BohaiCloudTradeResult result = bohaiPayHelper.request(queryParam, "SCTQReq");
		return null;
	}

	@Override
	public BohaiCloudTradeQueryParam createParam(PayTradeQueryReqVO reqVO) {
		BohaiCloudTradeQueryParam queryParam = new BohaiCloudTradeQueryParam();
		queryParam.setSerialNo(reqVO.getOrderNo());
		queryParam.setInstId("12345678");
		queryParam.setDate(reqVO.getDate());
		return queryParam;
	}

	

}
