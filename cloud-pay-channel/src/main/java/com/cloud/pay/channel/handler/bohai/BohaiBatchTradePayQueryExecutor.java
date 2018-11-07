package com.cloud.pay.channel.handler.bohai;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
			if("4".equals(resVO.getRespCode())) {
				resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_SUCCESS);
			}else if("5".equals(resVO.getRespCode()) || "6".equals(resVO.getRespCode())) {
				resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_FAIL);
			}else {
				resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_UNKNOWN);
			}
			resVO.setRespCode(ChannelContants.CHANNEL_RESP_CODE_SUCCESS);
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
		batchPayQueryParam.setDate(reqVO.getTradeDate());
		batchPayQueryParam.setSerialNo(reqVO.getOrderNo());
		batchPayQueryParam.setTermJnlno(reqVO.getBatchOrderNo());
		batchPayQueryParam.setFileNam(reqVO.getFileName());
		return batchPayQueryParam;
	}
	
	@Override
	protected BohaiCloudBatchTradePayQueryResult buildResult(String xmlRsp, String serialNo) {
		BohaiCloudBatchTradePayQueryResult  result = null;
		try {
			Document document = DocumentHelper.parseText(xmlRsp);
			Element rootElt = document.getRootElement();
			//拿到根节点的名称
			Element message = (Element)rootElt.element("Message");
			Element error = (Element)message.element("Error");
			if(null != error){
				BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(error.asXML(), BohaiCloudTradeErrorResult.class);
				result = new BohaiCloudBatchTradePayQueryResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
				BeanUtils.copyProperties(errorResult, result);
				return result;
			}
			
			Element response = (Element)message.element(ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBR);
			result =  JaxbUtil.fromXml(response.asXML(), BohaiCloudBatchTradePayQueryResult.class);
		} catch (DocumentException e) {
			log.error("代付，解析xml错误:{}",e);
		}
		return result;
	}
}
