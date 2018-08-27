package com.cloud.pay.channel.handler.bohai;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;

/**
 * 渤海代付接口（参数为泛型）
 * @author wangy
 */
@Service("bohaiTradePayExecutor")
public class BohaiTradePayExecutor implements ITradePayExecutor<PayTradeReqVO> {
   
	private Logger log = LoggerFactory.getLogger(BohaiTradePayExecutor.class);
    
	@Autowired
	private BohaiPayHelper bohaiPayHelper;
	
	@Override
	public BaseTradeResVO execute(PayTradeReqVO reqVO) {
		BohaiCloudTradePayParam param = createParam(reqVO);
		BohaiCloudTradeResult result = bohaiPayHelper.request(param, "SCSPReq");
		return null;
	}

	@Override
	public BohaiCloudTradePayParam createParam(PayTradeReqVO reqVO) {
		BohaiCloudTradePayParam payParam = new BohaiCloudTradePayParam();
		payParam.setSerialNo(reqVO.getOrderNo());
		payParam.setInstId("12345678");
		payParam.setPyrAct(reqVO.getPayerAccount());
		payParam.setPyrNam(reqVO.getPayerName());
		if(StringUtils.isNotBlank(reqVO.getAccountNo())) {
		   payParam.setActNo(reqVO.getAccountNo());
		}
		if(StringUtils.isNotBlank(reqVO.getAccountName())) {
		   payParam.setActNam(reqVO.getAccountName());
		}
		payParam.setPyeAct(reqVO.getPayeeAccount());
		payParam.setPyeNam(reqVO.getPayeeName());
		payParam.setPyeBnk(reqVO.getPayeeBankCode());
		payParam.setAmt(reqVO.getAmt());
		payParam.setPostscript(reqVO.getPostscript());
		return payParam;
	}

}
