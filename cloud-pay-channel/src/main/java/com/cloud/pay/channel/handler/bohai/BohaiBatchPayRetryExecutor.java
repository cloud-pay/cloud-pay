package com.cloud.pay.channel.handler.bohai;

import java.io.File;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.FileDigestUtil;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPayRetryReqVO;
import com.cloud.pay.channel.vo.bohai.BohaiBatchPayRetryParam;
import com.cloud.pay.channel.vo.bohai.BohaiBatchPayRetryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.exception.CloudPayException;

@Service("bohaiBatchPayRetryExecutor")
public class BohaiBatchPayRetryExecutor extends BohaiTradeExecutor<BohaiBatchPayRetryParam, BohaiBatchPayRetryResult> 
                implements ITradePayExecutor<BatchPayRetryReqVO, BaseTradeResVO> {
	
	@Value("${cloud.bohai.batch.pay.file.path}")
	private String batchPayFilePath;  //本地文件路径

	@Override
	public BaseTradeResVO execute(BatchPayRetryReqVO reqVO) {
		BaseTradeResVO resVO = null;
		try {
			//读取文件并生成文件sha1
			String filePath = "";
			if(batchPayFilePath.endsWith(File.separator)) {
				filePath = batchPayFilePath + reqVO.getFileName();
			}else {
				filePath = batchPayFilePath + File.separator + reqVO.getFileName();
			}
			String fileSHA1 = FileDigestUtil.getFileSHA1(new File(filePath));
			//TODO...... 上传文件
			
			
			BohaiBatchPayRetryParam retryParam = createParam(reqVO, fileSHA1);
			BohaiBatchPayRetryResult result = request(retryParam,  ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBT);
			if("1".equals(result.getRspCode())) {
				resVO = new BaseTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				log.info("渤海批量代付-重新触发-响应参数:{}",resVO);
				return resVO;
			}
			resVO = new BaseTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getRspMsg());
		}catch(Exception e) {
			log.error("渤海批量代付重新触发失败：{}",e);
			String msg = "系统异常";
			if(e instanceof CloudPayException) {
				msg  = e.getMessage();
			}
			resVO = new BaseTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,ChannelErrorCode.ERROR_9000,msg);
		}
		log.info("渤海批量代付-重新触发-响应参数:{}",resVO);
		return resVO;
	}

	private BohaiBatchPayRetryParam createParam(BatchPayRetryReqVO reqVO,String fileSHA1) {
		BohaiBatchPayRetryParam retryParam = new BohaiBatchPayRetryParam();
		retryParam.setInstId("12345678");
		retryParam.setCertId("324234242");
		retryParam.setDate(reqVO.getTradeDate());
		retryParam.setSerialNo(reqVO.getOrderNo());
		retryParam.setPyrAct(reqVO.getPayerAccount());
		retryParam.setPyrNam(reqVO.getPayerName());
		retryParam.setTotNum(reqVO.getTotalNum());
		retryParam.setTotAmt(reqVO.getTotalAmt());
		retryParam.setFileNam(reqVO.getFileName());
		retryParam.setFileSHA1(fileSHA1);
		return retryParam;
	}
	
	@Override
	protected BohaiBatchPayRetryResult buildResult(String xmlRsp, String serialNo) {
		BohaiBatchPayRetryResult  result = null;
		if(xmlRsp.contains("<Error>")) {
			 //获取错误信息
			String errorXml = xmlRsp.substring(xmlRsp.indexOf("<Error>"), xmlRsp.indexOf("</Error>")+"</Error>".length());
			BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(errorXml, BohaiCloudTradeErrorResult.class);
			result = new BohaiBatchPayRetryResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
			BeanUtils.copyProperties(errorResult, result);
			return result;
		}
		String startElement = "<"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBT+">";
		String endElement = "</"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBT+">";
		String responseXml = xmlRsp.substring(xmlRsp.indexOf(startElement), xmlRsp.indexOf(endElement)+endElement.length());
		result =  JaxbUtil.fromXml(responseXml, BohaiBatchPayRetryResult.class);
		return result; 
	}
}
