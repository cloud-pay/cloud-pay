package com.cloud.pay.channel.handler.bohai;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayQueryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.exception.CloudPayException;

@Service("bohaiBatchTradePayQueryExecutor")
public class BohaiBatchTradePayQueryExecutor extends BohaiTradeExecutor<BohaiCloudBatchTradePayQueryParam,BohaiCloudBatchTradePayQueryResult>
      implements ITradePayExecutor<BatchPayTradeQueryReqVO, BatchPayTradeQueryResVO> {

	@Override
	public BatchPayTradeQueryResVO execute(BatchPayTradeQueryReqVO reqVO) {
		BatchPayTradeQueryResVO  resVO = null;
		try {
			BohaiCloudBatchTradePayQueryParam batchQueryParam = createParam(reqVO);
			BohaiCloudBatchTradePayQueryResult result = request(batchQueryParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBR);
			resVO = new BatchPayTradeQueryResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getRspMsg());
		}catch(Exception e) {
			log.error("渤海批量代付结果查询失败：{}",e);
			String msg  = "系统异常";
			if(e instanceof CloudPayException) {
				msg = e.getMessage();
			}
			resVO = new BatchPayTradeQueryResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,ChannelErrorCode.ERROR_9000,"系统异常");
		}
		return resVO;
	}

	private BohaiCloudBatchTradePayQueryParam createParam(BatchPayTradeQueryReqVO reqVO) {
		BohaiCloudBatchTradePayQueryParam batchPayQueryParam = new BohaiCloudBatchTradePayQueryParam();
		batchPayQueryParam.setCertId("2324242");
		batchPayQueryParam.setInstId("23423432");
		batchPayQueryParam.setDate(reqVO.getTradeDate());
		batchPayQueryParam.setSerialNo(reqVO.getOrderNo());
		batchPayQueryParam.setTermJnlno(reqVO.getBatchOrderNo());
		batchPayQueryParam.setFileNam(reqVO.getFileName());
		return batchPayQueryParam;
	}
	
	@Override
	protected BohaiCloudBatchTradePayQueryResult buildResult(String xmlRsp, String serialNo) {
		BohaiCloudBatchTradePayQueryResult  result = null;
		if(xmlRsp.contains("<Error>")) {
			 //获取错误信息
			String errorXml = xmlRsp.substring(xmlRsp.indexOf("<Error>"), xmlRsp.indexOf("</Error>")+"</Error>".length());
			BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(errorXml, BohaiCloudTradeErrorResult.class);
			result = new BohaiCloudBatchTradePayQueryResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
			BeanUtils.copyProperties(errorResult, result);
			return result;
		}
		String startElement = "<"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBR+">";
		String endElement = "</"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBR+">";
		String responseXml = xmlRsp.substring(xmlRsp.indexOf(startElement), xmlRsp.indexOf(endElement)+endElement.length());
		result =  JaxbUtil.fromXml(responseXml, BohaiCloudBatchTradePayQueryResult.class);
		return result;
	}
}
