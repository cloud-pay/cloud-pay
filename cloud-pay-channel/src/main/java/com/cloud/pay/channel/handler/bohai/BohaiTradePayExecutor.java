package com.cloud.pay.channel.handler.bohai;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;

/**
 * 渤海代付接口（参数为泛型）
 * @author wangy
 */
@Service("bohaiTradePayExecutor")
public class BohaiTradePayExecutor extends BohaiTradeExecutor<BohaiCloudTradePayParam, BohaiCloudTradePayResult>
                      implements ITradePayExecutor<PayTradeReqVO,PayTradeResVO>{
   
	private Logger log = LoggerFactory.getLogger(BohaiTradePayExecutor.class);
    
	@Value("${cloud.bohai.pay.large.sub.value}")
	private BigDecimal largeSubValue;  //渤海代付大额值
	
	@Override
	public PayTradeResVO execute(PayTradeReqVO param) {
		PayTradeResVO resVO = null;
		try {
			 BohaiCloudTradePayParam payParam = createParam(param);
			 BohaiCloudTradePayResult result = request(payParam, param.getAmt().compareTo(largeSubValue) <= 0 ? 
					 ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCS:ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCHP);
			 if(!"0".equals(result.getRspCode())) {
				  resVO = new PayTradeResVO(param.getMerchantNo(),param.getOrderNo(),result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				  log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
				  return resVO;
			 }
			 resVO = new PayTradeResVO(param.getMerchantNo(),param.getOrderNo(),"代付成功",result.getActDat());
			 log.info("渠道接口：代付处理结束，响应参数：{}",resVO);
		}catch(Exception e) {
			log.error("渤海代付失败：{}",e);
			resVO = new PayTradeResVO(ChannelContants.CHANNEL_RESP_CODE_UNKNOWN,ChannelErrorCode.ERROR_9000,"暂时系统异常");
		}
		return resVO;
	}

	
	/**
       * 构建代付参数
     * @param reqVO
     * @return
     */
	private BohaiCloudTradePayParam createParam(PayTradeReqVO reqVO) {
		BohaiCloudTradePayParam payParam = new BohaiCloudTradePayParam();
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
		payParam.setPyeBnk(reqVO.getPayeeBankCode());

		payParam.setAmt(reqVO.getAmt());
		payParam.setPostscript(reqVO.getPostscript());
		return payParam;
	}
	

	@Override
	protected BohaiCloudTradePayResult buildResult(String xmlRsp,String serialNo) {
		BohaiCloudTradePayResult result = null;
		if(xmlRsp.contains("<Error>")) {
			 //获取错误信息
			String errorXml = xmlRsp.substring(xmlRsp.indexOf("<Error>"), xmlRsp.indexOf("</Error>")+"</Error>".length());
			BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(errorXml, BohaiCloudTradeErrorResult.class);
			result = new BohaiCloudTradePayResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
			BeanUtils.copyProperties(errorResult, result);
			return result;
		}
		String startElement = "<"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCS+">";
		String endElement = "</"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCS+">";
		String responseXml = xmlRsp.substring(xmlRsp.indexOf(startElement), xmlRsp.indexOf(endElement)+endElement.length());
		result =  JaxbUtil.fromXml(responseXml, BohaiCloudTradePayResult.class);
		return result;
	}
}
