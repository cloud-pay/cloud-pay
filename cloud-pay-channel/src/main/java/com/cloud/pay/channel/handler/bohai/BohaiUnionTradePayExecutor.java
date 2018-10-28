package com.cloud.pay.channel.handler.bohai;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
				  resVO = new PayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				  log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
				  return resVO;
			 }
			 resVO = new PayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),"代付成功",result.getActDat());
			 log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
		}catch(Exception e) {
			log.error("渠道接口：单笔银联代付失败，错误消息:{}",e);
			resVO = new PayTradeResVO(ChannelContants.CHANNEL_RESP_CODE_UNKNOWN,"9000","系统异常");
		}
		return resVO;
	}
	
	private BohaiCloudUnionTradeParam createParam(PayUnionTradeReqVO reqVO) {
		BohaiCloudUnionTradeParam payParam = new BohaiCloudUnionTradeParam();
		payParam.setDate(reqVO.getTradeDate());
		payParam.setSerialNo(reqVO.getOrderNo());
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
		try {
			Document document = DocumentHelper.parseText(xmlRsp);
			Element rootElt = document.getRootElement();
			//拿到根节点的名称
			Element message = (Element)rootElt.element("Message");
			Element error = (Element)message.element("Error");
			if(null != error){
				BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(error.asXML(), BohaiCloudTradeErrorResult.class);
				result = new BohaiCloudUnionTradeResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
				BeanUtils.copyProperties(errorResult, result);
				return result;
			}
			
			Element response = (Element)message.element(ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCUR);
			result =  JaxbUtil.fromXml(response.asXML(), BohaiCloudUnionTradeResult.class);
		} catch (DocumentException e) {
			log.error("代付，解析xml错误:{}",e);
		}
		return result;
	}

}
