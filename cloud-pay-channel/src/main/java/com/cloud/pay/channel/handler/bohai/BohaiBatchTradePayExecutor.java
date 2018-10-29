package com.cloud.pay.channel.handler.bohai;

import java.io.File;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.FileDigestUtil;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.bohai.BohaiBatchSingleQueryResult;
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
			String fileSHA1 = getFileSHA(batchPayFilePath, reqVO.getFileName());
			Map<String,String> map= issuePacFile(batchPayFilePath, reqVO.getFileName());
			log.info("渤海批量代付-上传文件：{}",map);
			//生成批量触发报文
			BohaiCloudBatchTradePayParam batchPayParam = createParam(reqVO, fileSHA1);
			BohaiCloudBatchTradePayResult result = request(batchPayParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBP);
			if("1".equals(result.getRspCode())) {
				resVO = new BatchPayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getErrorCode(),result.getErrorMessage());
				log.info("渤海批量代付-响应参数：{}",resVO);
				return resVO;
			}
			resVO = new BatchPayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getRspMsg());
		}catch(Exception e) {
			log.error("渤海批量代付失败：{}",e);
			String msg = "系统异常";
			if(e instanceof CloudPayException) {
				msg  = e.getMessage();
			}
			resVO = new BatchPayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,ChannelErrorCode.ERROR_9000,msg);
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
		try {
			Document document = DocumentHelper.parseText(xmlRsp);
			Element rootElt = document.getRootElement();
			//拿到根节点的名称
			Element message = (Element)rootElt.element("Message");
			Element error = (Element)message.element("Error");
			if(null != error){
				BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(error.asXML(), BohaiCloudTradeErrorResult.class);
				result = new BohaiCloudBatchTradePayResult();
				BeanUtils.copyProperties(errorResult, result);
				result.setRspCode(ChannelContants.CHANNEL_RESP_CODE_FAIL);
				return result;
			}
			
			Element response = (Element)message.element(ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBP);
			result =  JaxbUtil.fromXml(response.asXML(), BohaiCloudBatchTradePayResult.class);
		} catch (DocumentException e) {
			log.error("代付，解析xml错误:{}",e);
		}
		return result;
	}
}
