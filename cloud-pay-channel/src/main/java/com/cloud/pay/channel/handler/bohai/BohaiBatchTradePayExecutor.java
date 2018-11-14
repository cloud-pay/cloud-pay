package com.cloud.pay.channel.handler.bohai;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.FileDigestUtil;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BatchPayTradeInnerReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.bohai.BohaiBatchSingleQueryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.mapper.SysConfigMapper;
import com.cloud.pay.common.utils.DateUtil;

@Service("bohaiBatchTradePayExecutor")
public class BohaiBatchTradePayExecutor extends BohaiTradeExecutor<BohaiCloudBatchTradePayParam, BohaiCloudBatchTradePayResult>
      implements ITradePayExecutor<BatchPayTradeInnerReqVO, BatchPayTradeResVO> {

	@Value("${cloud.bohai.batch.pay.file.path}")
	private String batchPayFilePath;  //本地文件路径
	
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	@Override
	public BatchPayTradeResVO execute(BatchPayTradeInnerReqVO reqVO) {
		BatchPayTradeResVO resVO = null;
		try {
			//读取文件并生成文件sha1
			String filePath = ""; 
			if(batchPayFilePath.endsWith(File.separator)) {
				filePath = batchPayFilePath + DateUtil.getDays() + File.separator;
			}else {
				filePath = batchPayFilePath + File.separator + DateUtil.getDays() + File.separator;
			}
			String fileSHA1 = getFileSHA(filePath, reqVO.getFileName());
			Map<String,String> map= issuePacFile(filePath, reqVO.getFileName());
			log.info("渤海批量代付-上传文件：{}",map);
			if(!"0000".equals(map.get("rspCd"))) {
				resVO = new BatchPayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,
						ChannelErrorCode.ERROR_1002,"批量文件上传失败");
				log.info("渤海批量代付-响应参数：{}",resVO);
				return resVO;
			}
			//生成批量触发报文
			BohaiCloudBatchTradePayParam batchPayParam = createParam(reqVO, fileSHA1);
			BohaiCloudBatchTradePayResult result = request(batchPayParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBP);
			if("1".equals(result.getRspCode())) {
				resVO = new BatchPayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),
						result.getErrorCode(),StringUtils.isNotBlank(result.getErrorMessage())?result.getErrorMessage():result.getRspMsg());
				if("1002".equals(result.getErrorCode())) {
					  resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_UNKNOWN);
				}else {
					  resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_FAIL);
				}
				resVO.setRespCode(ChannelContants.CHANNEL_RESP_CODE_SUCCESS);
				log.info("渤海批量代付-响应参数：{}",resVO);
				return resVO;
			}
			resVO = new BatchPayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getRspMsg());
			resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_SUCCESS);
		}catch(Exception e) {
			log.error("渤海批量代付失败：{}",e);
			String msg = "系统异常";
			if(e instanceof CloudPayException) {
				msg  = e.getMessage();
			}
			resVO = new BatchPayTradeResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,ChannelErrorCode.ERROR_9000,msg);
			resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_UNKNOWN);
		}
		log.info("渤海批量代付-响应参数：{}",resVO);
		return resVO;
	}

	/**
	 * 构建上游请求参数
	 * @param reqVO
	 * @return
	 * @throws Exception 
	 */
	private BohaiCloudBatchTradePayParam createParam(BatchPayTradeInnerReqVO reqVO,String fileSHA1) throws Exception {
		BohaiCloudBatchTradePayParam batchPayParam = new BohaiCloudBatchTradePayParam();
		batchPayParam.setDate(reqVO.getTradeDate());
		batchPayParam.setSerialNo(reqVO.getOrderNo());
		SysConfig payerAccountConfig = sysConfigMapper.selectByPrimaryKey("BHPayerAccount");
		SysConfig payerNameConfig = sysConfigMapper.selectByPrimaryKey("BHPayerName");
		if(null == payerAccountConfig || null == payerNameConfig) {
			throw new Exception("系统错误:渠道系统参数未配置");
		}
		batchPayParam.setPyrAct(payerAccountConfig.getSysValue());
		batchPayParam.setPyrNam(payerNameConfig.getSysValue());
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
