package com.cloud.pay.channel.handler.bohai;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPaySingleQueryReqVO;
import com.cloud.pay.channel.vo.bohai.BohaiBatchPayRetryResult;
import com.cloud.pay.channel.vo.bohai.BohaiBatchSingleQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiBatchSingleQueryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.exception.CloudPayException;

@Service("bohaiBatchSingleQueryExecutor")
public class BohaiBatchSingleQueryExecutor extends BohaiTradeExecutor<BohaiBatchSingleQueryParam, BohaiBatchSingleQueryResult> 
                       implements ITradePayExecutor<BatchPaySingleQueryReqVO, BaseTradeResVO> {

	@Override
	public BaseTradeResVO execute(BatchPaySingleQueryReqVO reqVO) {
		BaseTradeResVO resVO = null;
		try {
			BohaiBatchSingleQueryParam queryParam = createParam(reqVO);
			BohaiBatchSingleQueryResult result = request(queryParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBS);
			if(!"0".equals(result.getRspCode())) {
				resVO = new BaseTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				log.info("渤海批量代付-单笔查询-响应参数:{}",resVO);
				return resVO; 
			}
			resVO = new BaseTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getRspMsg());
		}catch(Exception e) {
			log.info("渤海批量代付单笔查询失败:",e.getMessage());
			String msg = "";
			if(e instanceof CloudPayException) {
				msg = e.getMessage();
			}
			resVO = new BaseTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,ChannelErrorCode.ERROR_9000,msg);
		}
		log.info("渤海批量代付-单笔查询-响应参数:{}",resVO);
		return resVO;
	}
 
	
	private BohaiBatchSingleQueryParam createParam(BatchPaySingleQueryReqVO reqVO) {
		BohaiBatchSingleQueryParam  queryParam = new BohaiBatchSingleQueryParam();
		queryParam.setDate(reqVO.getTradeDate());
		queryParam.setSerialNo(reqVO.getOrderNo());
		queryParam.setFileNam(reqVO.getFileName());
		queryParam.setTermJnlno(reqVO.getBatchOrderNo());
		queryParam.setSeqNo(reqVO.getSeqNo());
		return queryParam;
	}
	
	@Override
	protected BohaiBatchSingleQueryResult buildResult(String xmlRsp, String serialNo) {
		BohaiBatchSingleQueryResult result = null;
		try {
			Document document = DocumentHelper.parseText(xmlRsp);
			Element rootElt = document.getRootElement();
			//拿到根节点的名称
			Element message = (Element)rootElt.element("Message");
			Element error = (Element)message.element("Error");
			if(null != error){
				BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(error.asXML(), BohaiCloudTradeErrorResult.class);
				result = new BohaiBatchSingleQueryResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
				BeanUtils.copyProperties(errorResult, result);
				return result;
			}
			
			Element response = (Element)message.element(ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBS);
			result =  JaxbUtil.fromXml(response.asXML(), BohaiBatchSingleQueryResult.class);
		} catch (DocumentException e) {
			log.error("代付，解析xml错误:{}",e);
		}
		return result;
	}
}
