package com.cloud.pay.channel.handler.bohai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;

@Service("bohaiDownReconFileExecutor")
public class BohaiTradeDownReconFileExecutor 
       extends BohaiTradeExecutor<BohaiCloudTradeParam,BohaiCloudTradeResult> 
       implements ITradePayExecutor<ReconDownFileReqVO,ReconDownFileResVO>{
	
	private Logger log = LoggerFactory.getLogger(BohaiTradeDownReconFileExecutor.class);
	
	@Value("${cloud.bohai.pay.instId}")
	private String instId;
	
	@Override
	public ReconDownFileResVO execute(ReconDownFileReqVO reqVO) {
		ReconDownFileResVO resVO = null;
		String fileName = "DCHK" + instId+ reqVO.getReconDate();
		
		return resVO;
	}
  
}
