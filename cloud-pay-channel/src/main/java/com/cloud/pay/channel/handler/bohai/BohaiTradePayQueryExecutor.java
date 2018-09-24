package com.cloud.pay.channel.handler.bohai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BaseTradeResVO;

import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryResult;
import com.cloud.pay.common.contants.ChannelContants;

/**
 * 渤海代付查询接口
 * @author wangy
 */
@Service("bohaiTradeQueryExecutor")
public class BohaiTradePayQueryExecutor extends BohaiTradeExecutor<BohaiCloudTradeQueryParam, BohaiCloudTradeQueryResult>
    implements ITradePayExecutor<PayTradeQueryReqVO,PayTradeQueryResVO>{

	private Logger log = LoggerFactory.getLogger(BohaiTradePayQueryExecutor.class);
	
	
	@Override
	public PayTradeQueryResVO execute(PayTradeQueryReqVO reqParam) {
		log.info("代付结果查询-渤海代付查询-请求参数：{}",reqParam);
		PayTradeQueryResVO resVO = null;
		try {
			BohaiCloudTradeQueryParam queryParam = createParam(reqParam);
			BohaiCloudTradeQueryResult result = request(queryParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCTQ);
			if(!"0".equals(result.getRspCode())) {
				resVO = new PayTradeQueryResVO(reqParam.getMerchantId(), reqParam.getOrderNo(), result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
				return resVO;
			}
			resVO = new PayTradeQueryResVO(reqParam.getMerchantId(), reqParam.getOrderNo(), result.getRspMsg());
			
			return buildQueryResult(resVO,result);
		}catch(Exception e) {
			log.error("代付结果查询-渤海代付查询失败：{}",e);
			resVO = new PayTradeQueryResVO(ChannelContants.CHANNEL_RESP_CODE_UNKNOWN,"9000","系统异常");
		}
		log.info("渠道接口：代付结果查询结束，响应参数：{}",resVO);
		return resVO;
	}
	
	
	/**
	 * 构建代付结果查询请求参数
	 * @param reqVO
	 * @return
	 */
	private BohaiCloudTradeQueryParam createParam(PayTradeQueryReqVO reqVO) {
		BohaiCloudTradeQueryParam queryParam = new BohaiCloudTradeQueryParam();
		queryParam.setSerialNo(reqVO.getOrderNo());
		queryParam.setInstId("12345678");
		queryParam.setDate(reqVO.getTradeDate());
		return queryParam;
	}
	
	/**
	 * 构建代付结果查询响应参数
	 * @param resVO
	 * @param result
	 * @return
	 */
	private PayTradeQueryResVO buildQueryResult(PayTradeQueryResVO resVO,BohaiCloudTradeQueryResult result) {
		resVO.setPayerAccount(result.getPyrAct());
		resVO.setPayerName(result.getPyrNam());
		resVO.setPayeeAccount(result.getPyeAct());
		resVO.setPayeeName(result.getPyeNam());
		resVO.setPayeeBankCode(result.getPyeBnk());
		resVO.setAmt(result.getAmt());
		resVO.setPostscript(result.getPostscript());
		resVO.setAccountDate(result.getActDat());
		return resVO;
	}

	@Override
	protected BohaiCloudTradeQueryResult buildResult(String xmlRsp, String serialNo) {
		BohaiCloudTradeQueryResult result = null;
		if(xmlRsp.contains("<Error>")) {
			 //获取错误信息
			String errorXml = xmlRsp.substring(xmlRsp.indexOf("<Error>"), xmlRsp.indexOf("</Error>")+"</Error>".length());
			BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(errorXml, BohaiCloudTradeErrorResult.class);
			result = new BohaiCloudTradeQueryResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
			BeanUtils.copyProperties(errorResult, result);
			return result;
		}
		String startElement = "<"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCTQ+">";
		String endElement = "</"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCTQ+">";
		String responseXml = xmlRsp.substring(xmlRsp.indexOf(startElement), xmlRsp.indexOf(endElement)+endElement.length());
		result =  JaxbUtil.fromXml(responseXml, BohaiCloudTradeQueryResult.class);
		return result;
	}
}
