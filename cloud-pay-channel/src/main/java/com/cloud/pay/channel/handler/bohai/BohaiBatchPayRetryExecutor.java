package com.cloud.pay.channel.handler.bohai;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.JaxbUtil;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPayRetryReqVO;
import com.cloud.pay.channel.vo.bohai.BohaiBatchPayRetryParam;
import com.cloud.pay.channel.vo.bohai.BohaiBatchPayRetryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeErrorResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.mapper.SysConfigMapper;

@Service("bohaiBatchPayRetryExecutor")
public class BohaiBatchPayRetryExecutor extends BohaiTradeExecutor<BohaiBatchPayRetryParam, BohaiBatchPayRetryResult> 
                implements ITradePayExecutor<BatchPayRetryReqVO, BaseTradeResVO> {
	
	@Value("${cloud.bohai.batch.pay.file.path}")
	private String batchPayFilePath;  //本地文件路径
	
	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Override
	public BaseTradeResVO execute(BatchPayRetryReqVO reqVO) {
		BaseTradeResVO resVO = null;
		try {
			//读取文件并生成文件sha1
			String fileSHA1 = getFileSHA(batchPayFilePath, reqVO.getFileName());
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
	


	private BohaiBatchPayRetryParam createParam(BatchPayRetryReqVO reqVO,String fileSHA1) throws Exception {
		BohaiBatchPayRetryParam retryParam = new BohaiBatchPayRetryParam();
		retryParam.setDate(reqVO.getTradeDate());
		retryParam.setSerialNo(reqVO.getOrderNo());
		SysConfig payerAccountConfig = sysConfigMapper.selectByPrimaryKey("BHPayerAccount");
		SysConfig payerNameConfig = sysConfigMapper.selectByPrimaryKey("BHPayerName");
		if(null == payerAccountConfig || null == payerNameConfig) {
			throw new Exception("系统错误:渠道系统参数未配置");
		}
		retryParam.setPyrAct(payerAccountConfig.getSysValue());
		retryParam.setPyrNam(payerNameConfig.getSysValue());
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
				result = new BohaiBatchPayRetryResult();
				BeanUtils.copyProperties(errorResult, result);
				result.setRspCode(ChannelContants.CHANNEL_RESP_CODE_FAIL);
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
