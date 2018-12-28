package com.cloud.pay.channel.handler.bohai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.dto.TradeDTO;
import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.utils.ReconReturnCodeEnum;
import com.cloud.pay.channel.vo.BatchPayTradeQueryInnerResVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudBatchTradePayQueryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.utils.DateUtil;
import com.cloud.pay.common.utils.FileUtils;

@Service("bohaiBatchTradePayQueryExecutor")
public class BohaiBatchTradePayQueryExecutor extends BohaiTradeExecutor<BohaiCloudBatchTradePayQueryParam,BohaiCloudBatchTradePayQueryResult>
      implements ITradePayExecutor<BatchPayTradeQueryReqVO, BatchPayTradeQueryResVO> {

	@Value("${cloud.bohai.batch.pay.file.path}")
	private String batchPayFilePath;  //本地文件路径
	
	@Override
	public BatchPayTradeQueryResVO execute(BatchPayTradeQueryReqVO reqVO) {
		BatchPayTradeQueryResVO  resVO = null;
		try {
			BohaiCloudBatchTradePayQueryParam batchQueryParam = createParam(reqVO);
			BohaiCloudBatchTradePayQueryResult result = request(batchQueryParam, ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCBR);
			resVO = new BatchPayTradeQueryInnerResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),result.getRspCode(),result.getRspMsg());
			if("4".equals(resVO.getRespCode()) || "3".equals(resVO.getRespCode())) {
				resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_SUCCESS);
				//resVO.setFileName(result.getFilNam());
			    //获取结果文件
				List<TradeDTO> list = getResultList(result.getFilNam(), reqVO.getBatchOrderNo());
				if(null == list || list.size() <= 0) {
					resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_UNKNOWN);
				}else {
					resVO.setTrades(list);
				}
			}else if("5".equals(resVO.getRespCode()) || "6".equals(resVO.getRespCode())) {
				resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_FAIL);
			}else {
				resVO.setStatus(ChannelContants.CHANNEL_RETURN_STATUS_UNKNOWN);
			}
			resVO.setRespCode(ChannelContants.CHANNEL_RESP_CODE_SUCCESS);
		}catch(Exception e) {
			log.error("渤海批量代付结果查询失败：{}",e);
			String msg  = "系统异常";
			if(e instanceof CloudPayException) {
				msg = e.getMessage();
			}
			resVO = new BatchPayTradeQueryInnerResVO(reqVO.getMerchantId(),reqVO.getOrderNo(),ChannelContants.CHANNEL_RESP_CODE_FAIL,ChannelErrorCode.ERROR_9000,"系统异常");
		}
		return resVO;
	}
	
	/**
	 * 下载并解析结果文件
	 * @param fileName
	 * @param batchNo
	 * @return
	 */
	private List<TradeDTO> getResultList(String fileName,String batchNo){
		String filePath = ""; 
		if(batchPayFilePath.endsWith(File.separator)) {
			filePath = batchPayFilePath + DateUtil.getDays() + File.separator + batchNo + File.separator ;
		}else {
			filePath = batchPayFilePath + File.separator + DateUtil.getDays() + File.separator + batchNo + File.separator;
		}
		//下载文件到本地
		FileUtils.isExist(filePath);
		String getFileResult = downloadFile(fileName, "BPI", filePath);
		log.info("查询批量文件结果：{}",getFileResult);
		if("SUCCESS".equals(getFileResult)) {
			 //解析文件
			String fileFullPath = filePath + fileName;
			return analysisResultFile(fileFullPath);
		}
		return null;
	}
	
	/**
	 * 解析结果文件（如果返回空，则应该返回未知状态，避免因解析文件失败导致数据不正确）
	 * @param fileFullPath
	 * @return
	 */
	private static List<TradeDTO> analysisResultFile(String fileFullPath){
		 List<TradeDTO> list = new ArrayList<TradeDTO>();
		 BufferedReader buf = null;
		 try {
			 String line = null;
			 buf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileFullPath)),"gb2312"));
			 int i = 0;
			 while((line = buf.readLine()) != null){
				 if(i == 0) {
					 i++;
					 continue;
				 }
				 
				 TradeDTO trade = new TradeDTO();
				 String[] str = line.trim().split("~");
				 trade.setSeqNo(StringUtils.isNotBlank(str[0])?str[0]:"");
				 trade.setPayerAccount(StringUtils.isNotBlank(str[1])?str[1]:"");
				 trade.setPayerName(StringUtils.isNotBlank(str[2])?str[2]:"");
				 trade.setPayeeAccount(StringUtils.isNotBlank(str[3])?str[3]:"");
				 trade.setPayeeName(StringUtils.isNotBlank(str[4])?str[4]:"");
				 trade.setPayeeBankCode(StringUtils.isNotBlank(str[5])?str[5]:"");
				 trade.setStatus(StringUtils.isNotBlank(str[9])? ReconReturnCodeEnum.getStatus(str[9]):ReconReturnCodeEnum.RETURN_UNKNOWN.getStatus());
				 list.add(trade);
			 }
		 }catch (FileNotFoundException e) {
			 //log.error("解析结果文件出错：{}",e);
			 return null;
		 } catch (IOException e) {
			 //log.error("解析结果文件出错：{}",e);
			 return null;
		 }finally {
			 if(buf != null) {
				try {
					buf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		 }
		 return list;
	}
	
	public static void main(String[] args) {
		analysisResultFile("C:\\Users\\THINKPAD\\Desktop\\YD1200612018122700000023.rsp");
	}

	private BohaiCloudBatchTradePayQueryParam createParam(BatchPayTradeQueryReqVO reqVO) {
		BohaiCloudBatchTradePayQueryParam batchPayQueryParam = new BohaiCloudBatchTradePayQueryParam();
		batchPayQueryParam.setDate(reqVO.getTradeDate());
		batchPayQueryParam.setSerialNo(reqVO.getOrderNo());
		batchPayQueryParam.setTermJnlno(reqVO.getBatchOrderNo());
		batchPayQueryParam.setFileNam(reqVO.getFileName());
		return batchPayQueryParam;
	}
	
	@Override
	protected BohaiCloudBatchTradePayQueryResult buildResult(String xmlRsp, String serialNo) {
		BohaiCloudBatchTradePayQueryResult  result = null;
		try {
			Document document = DocumentHelper.parseText(xmlRsp);
			Element rootElt = document.getRootElement();
			//拿到根节点的名称
			Element message = (Element)rootElt.element("Message");
			Element error = (Element)message.element("Error");
			if(null != error){
				BohaiCloudTradeErrorResult errorResult = JaxbUtil.fromXml(error.asXML(), BohaiCloudTradeErrorResult.class);
				result = new BohaiCloudBatchTradePayQueryResult(ChannelContants.CHANNEL_RESP_CODE_FAIL);
				BeanUtils.copyProperties(errorResult, result);
				return result;
			}
			
			Element response = (Element)message.element(ChannelContants.CHANNEL_BOHAI_RES_HEADER_SCBR);
			result =  JaxbUtil.fromXml(response.asXML(), BohaiCloudBatchTradePayQueryResult.class);
		} catch (DocumentException e) {
			log.error("代付，解析xml错误:{}",e);
		}
		return result;
	}

}
