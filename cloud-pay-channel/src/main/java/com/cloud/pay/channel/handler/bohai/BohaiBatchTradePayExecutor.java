package com.cloud.pay.channel.handler.bohai;

import java.io.File;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.FileDigestUtil;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.exception.CloudPayException;

@Service("bohaiBatchTradePayExecutor")
public class BohaiBatchTradePayExecutor extends BohaiTradeExecutor<BohaiCloudBatchTradePayParam, BohaiCloudBatchTradePayResult>
      implements ITradePayExecutor<BatchPayTradeReqVO, BatchPayTradeResVO> {

	@Value("${cloud.bohai.batch.pay.file.path}")
	private String batchPayFilePath;  //本地文件路径
	
	@Override
	public BatchPayTradeResVO execute(BatchPayTradeReqVO reqVO) {
		BatchPayTradeResVO resVO = null;
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
			
			
			//生成批量触发报文
			BohaiCloudBatchTradePayParam batchPayParam = createParam(reqVO, fileSHA1);
			BohaiCloudBatchTradePayResult result = request(batchPayParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBP);
			if("1".equals(result.getRspCode())) {
				resVO = new BatchPayTradeResVO(reqVO.getMerchantNo(),reqVO.getOrderNo(),result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				log.info("渤海批量代付-响应参数：{}",resVO);
				return resVO;
			}
			resVO = new BatchPayTradeResVO(reqVO.getMerchantNo(),reqVO.getOrderNo(),result.getRspCode(),result.getRspMsg());
		}catch(Exception e) {
			log.error("渤海批量代付失败：{}",e);
			String msg = "系统异常";
			if(e instanceof CloudPayException) {
				msg  = e.getMessage();
			}
			resVO = new BatchPayTradeResVO(reqVO.getMerchantNo(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,ChannelErrorCode.ERROR_9000,msg);
		}
		log.info("渤海批量代付-响应参数：{}",resVO);
		return resVO;
	}

	/**
	 * 构建上游请求参数
	 * @param reqVO
	 * @return
	 */
	private BohaiCloudBatchTradePayParam createParam(BatchPayTradeReqVO reqVO,String fileSHA1) {
		BohaiCloudBatchTradePayParam batchPayParam = new BohaiCloudBatchTradePayParam();
		batchPayParam.setInstId("12345678");
		batchPayParam.setCertId("234242342");
		batchPayParam.setDate(reqVO.getTradeDate());
		batchPayParam.setSerialNo(reqVO.getOrderNo());
		batchPayParam.setPyrAct(reqVO.getPayerAccount());
		batchPayParam.setPyrNam(reqVO.getPayerName());
		batchPayParam.setTotNum(reqVO.getTotalNum());
		batchPayParam.setTotAmt(reqVO.getTotalAmt());
		batchPayParam.setFileNam(reqVO.getFileName());
		batchPayParam.setFileSHA1(fileSHA1);
		return batchPayParam;
	}
	
	@Override
	protected BohaiCloudBatchTradePayResult buildResult(String xmlRsp, String serialNo) {
		BohaiCloudBatchTradePayResult  result = null;
		if(xmlRsp.contains("<Error>")) {
			 //获取错误信息
			String errorXml = xmlRsp.substring(xmlRsp.indexOf("<Error>"), xmlRsp.indexOf("</Error>")+"</Error>".length());
			BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(errorXml, BohaiCloudTradeErrorResult.class);
			result = new BohaiCloudBatchTradePayResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
			BeanUtils.copyProperties(errorResult, result);
			return result;
		}
		String startElement = "<"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBP+">";
		String endElement = "</"+ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBP+">";
		String responseXml = xmlRsp.substring(xmlRsp.indexOf(startElement), xmlRsp.indexOf(endElement)+endElement.length());
		result =  JaxbUtil.fromXml(responseXml, BohaiCloudBatchTradePayResult.class);
		return result;
	}
}
