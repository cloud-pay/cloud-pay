package com.cloud.pay.channel.handler.bohai;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudUnionTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudUnionTradeResult;
import com.cloud.pay.common.contants.ChannelContants;

@Service("bohaiUnionTradePayExecutor")
public class BohaiUnionTradePayExecutor extends BohaiTradeExecutor<BohaiCloudUnionTradeParam, BohaiCloudUnionTradeResult>
                                implements ITradePayExecutor<PayUnionTradeReqVO, PayTradeResVO> {

	@Override
	public PayTradeResVO execute(PayUnionTradeReqVO reqVO) {
		PayTradeResVO resVO = null;
		try {
			BohaiCloudUnionTradeParam payParam = createParam(reqVO);
			BohaiCloudUnionTradeResult result = request(payParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCUR);
			 if(!"0".equals(result.getRspCode())) {
				  resVO = new PayTradeResVO(reqVO.getMerchantNo(),reqVO.getOrderNo(),result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				  log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
				  return resVO;
			 }
			 resVO = new PayTradeResVO(reqVO.getMerchantNo(),reqVO.getOrderNo(),"代付成功",result.getActDat());
			 log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
		}catch(Exception e) {
			log.error("渠道接口：单笔银联代付失败，错误消息:{}",e);
			resVO = new PayTradeResVO(ChannelContants.CHANNEL_RESP_CODE_UNKNOWN,"9000","暂时系统异常");
		}
		return resVO;
	}
	
	private BohaiCloudUnionTradeParam createParam(PayUnionTradeReqVO reqVO) {
		BohaiCloudUnionTradeParam payParam = new BohaiCloudUnionTradeParam();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
		payParam.setDate(sdf.format(new Date()));
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
		if(StringUtils.isNotBlank(reqVO.getIdNo())) {
			payParam.setIdType(reqVO.getIdType());
		}
		if(StringUtils.isNotBlank(reqVO.getIdNo())) {
			payParam.setIdNo(reqVO.getIdNo());
		}
		payParam.setAmt(reqVO.getAmt());
		payParam.setPostscript(reqVO.getPostscript());
		return payParam;
	}
	
	@Override
	protected BohaiCloudUnionTradeResult buildResult(String xmlRsp,String serialNo) {
		BohaiCloudUnionTradeResult result = null;
		if(xmlRsp.contains("<Error>")) {
			 //获取错误信息
			String errorXml = xmlRsp.substring(xmlRsp.indexOf("<Error>"), xmlRsp.indexOf("</Error>")+"</Error>".length());
			BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(errorXml, BohaiCloudTradeErrorResult.class);
			result = new BohaiCloudUnionTradeResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
			BeanUtils.copyProperties(errorResult, result);
			return result;
		}
		String startElement = "<"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCUR+">";
		String endElement = "</"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCUR+">";
		String responseXml = xmlRsp.substring(xmlRsp.indexOf(startElement), xmlRsp.indexOf(endElement)+endElement.length());
		result =  JaxbUtil.fromXml(responseXml, BohaiCloudUnionTradeResult.class);
		return result;
	}

}
