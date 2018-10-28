package com.cloud.pay.channel.handler.bohai;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.handler.bohai.core.PacUtil;
import com.cloud.pay.channel.utils.FileDigestUtil;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPayRetryReqVO;
import com.cloud.pay.channel.vo.bohai.BohaiBatchPayRetryParam;
import com.cloud.pay.channel.vo.bohai.BohaiBatchPayRetryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradePayResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.exception.CloudPayException;

@Service("bohaiBatchPayRetryExecutor")
public class BohaiBatchPayRetryExecutor extends BohaiTradeExecutor<BohaiBatchPayRetryParam, BohaiBatchPayRetryResult> 
                implements ITradePayExecutor<BatchPayRetryReqVO, BaseTradeResVO> {
	
	@Value("${cloud.bohai.batch.pay.file.path}")
	private String batchPayFilePath;  //本地文件路径
	
	private final static String charset = "utf-8";

	@Override
	public BaseTradeResVO execute(BatchPayRetryReqVO reqVO) {
		BaseTradeResVO resVO = null;
		try {
			//读取文件并生成文件sha1
			String fileSHA1 = getFileSHA(batchPayFilePath, reqVO.getFileName());
			Map<String,String> map= issuePacFile(batchPayFilePath, reqVO.getFileName());
			log.info("渤海批量代付-重新触发-上传文件：{}",map);
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
	
	/**
	 * 获取文件的SHA1信息
	 * @param folder
	 * @param fileName
	 * @return
	 */
    private String getFileSHA(String folder, String fileName) {
    	File uploadFile = null;
		FileInputStream fis = null;
		String fileSHA1 = null;
		try {
			uploadFile = new File(folder, fileName);
			fis = new FileInputStream(uploadFile);

			byte[] fileCont = PacUtil.readAllByteFromStream(fis);

			if (null != fileCont) {
				fileSHA1 = DigestUtils.shaHex(fileCont);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != uploadFile) {
				uploadFile = null;
			}
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fis = null;
			}
		}
		return fileSHA1;
    }


	private BohaiBatchPayRetryParam createParam(BatchPayRetryReqVO reqVO,String fileSHA1) {
		BohaiBatchPayRetryParam retryParam = new BohaiBatchPayRetryParam();
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
		try {
			Document document = DocumentHelper.parseText(xmlRsp);
			Element rootElt = document.getRootElement();
			//拿到根节点的名称
			Element message = (Element)rootElt.element("Message");
			Element error = (Element)message.element("Error");
			if(null != error){
				BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(error.asXML(), BohaiCloudTradeErrorResult.class);
				result = new BohaiBatchPayRetryResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
				BeanUtils.copyProperties(errorResult, result);
				return result;
			}
			
			Element response = (Element)message.element(ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBT);
			result =  JaxbUtil.fromXml(response.asXML(), BohaiBatchPayRetryResult.class);
		} catch (DocumentException e) {
			log.error("代付，解析xml错误:{}",e);
		}
		return result;
	}
}
