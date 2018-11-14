package com.cloud.pay.channel.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.TradePayTypeHandlerFactory;
import com.cloud.pay.channel.dto.TradeDTO;
import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.utils.ValidationUtils;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPayRetryReqVO;
import com.cloud.pay.channel.vo.BatchPaySingleQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeInnerReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.contants.ChannelType;
import com.cloud.pay.common.contants.FileSuffixEnums;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.common.mapper.SysConfigMapper;
import com.cloud.pay.common.utils.DateUtil;
import com.cloud.pay.common.utils.FileUtils;
import com.cloud.pay.merchant.entity.MerchantChannel;
import com.cloud.pay.merchant.mapper.MerchantChannelMapper;

/**
 * 渠道接口实现类
 * @author wangy
 */
@Service("cloudApiService")
public class CloudApiServiceImpl implements ICloudApiService {
	
	private Logger log = LoggerFactory.getLogger(CloudApiServiceImpl.class);
	
	@Value("${cloud.bohai.pay.instId}")
	private String instId;
	
	@Value("${cloud.bohai.batch.pay.file.path}")
	private String batchPayFilePath;  //本地文件路径

	@Autowired
	private TradePayTypeHandlerFactory tradePayTypeHandlerFactory; 
	
	@Autowired
	private MerchantChannelMapper merchantChannelMapper;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	@Override
	public PayTradeResVO pay(PayTradeReqVO tradeReq) {
		PayTradeResVO resVO = null;
		try {
			log.info("渠道接口：收到代付请求，请求参数：{}",tradeReq);
			try {
				ValidationUtils.validate(tradeReq);
			}catch(CloudApiException e) {
				log.error("参数校验失败:{}",e.getMessage());
				resVO = new PayTradeResVO(e.getErrorCode(),e.getMessage());
				return resVO;
			}
			//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
			List<MerchantChannel> merchantChannels = merchantChannelMapper.selectByMerchantId(tradeReq.getMerchantId());
		    if(null == merchantChannels || merchantChannels.size() <=0 ) {
		    	log.error("商户未配置渠道信息");
				resVO = new PayTradeResVO(ChannelErrorCode.ERROR_0003,"商户未配置渠道信息");
				return resVO;
		    }
		    MerchantChannel merchantChannel = merchantChannels.get(0);
			ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradePayHandler(ChannelType.getChannelByChannelId(merchantChannel.getChannelId()));
			resVO = (PayTradeResVO) tradePayExecutor.execute(tradeReq);
			resVO.setChannelId(ChannelType.BOHAI.getChannelId());
		}catch(Exception e){
			log.error("系统异常：{}",e.getMessage());
			resVO = new PayTradeResVO(ChannelErrorCode.ERROR_9000,"系统异常");
		}
		log.info("渠道接口：代付，响应参数：{}",resVO);
		return resVO;
	}
	
  

	@Override
	public PayTradeQueryResVO queryPay(PayTradeQueryReqVO tradeReq) {
		log.info("渠道接口：收到代付结果查询请求，请求参数：{}",tradeReq);
		PayTradeQueryResVO result  = null;
		try {
			ValidationUtils.validate(tradeReq);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			result = new PayTradeQueryResVO(e.getErrorCode(),e.getMessage());
			return result;
		}
		try{
			ITradePayExecutor tradePayExecutor = 	tradePayTypeHandlerFactory.getTradePayQueryHandler(ChannelType.getChannelByChannelId(tradeReq.getChannelId()));
			result = (PayTradeQueryResVO) tradePayExecutor.execute(tradeReq);
		}catch(Exception e) {
			log.error("系统错误:{}",e.getMessage());
			result = new PayTradeQueryResVO(ChannelErrorCode.ERROR_9000,"系统异常");
		}
		log.info("渠道接口：代付结果查询结束，响应参数：{}",result);
		return result;
	}



	@Override
	public PayTradeResVO unionPay(PayUnionTradeReqVO reqVO) {
		log.info("渠道接口：单笔银联代付，请求参数：{}",reqVO);
		PayTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new PayTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
	    List<MerchantChannel> merchantChannels = merchantChannelMapper.selectByMerchantId(reqVO.getMerchantId());
		if(null == merchantChannels) {
			log.error("商户未配置渠道信息");
			resVO = new PayTradeResVO(ChannelErrorCode.ERROR_0003,"商户未配置渠道信息");
			return resVO;
		}
		MerchantChannel merchantChannel = merchantChannels.get(0);
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradeUnionPayHandler(
				ChannelType.getChannelByChannelId(merchantChannel.getChannelId()));
		resVO = (PayTradeResVO) tradePayExecutor.execute(reqVO);
		resVO.setChannelId(ChannelType.BOHAI.getChannelId());
		log.info("渠道接口：单笔银联代付，响应参数：{}",resVO);
		return resVO;
	}



	@Override
	public ReconDownFileResVO downReconFile(ReconDownFileReqVO reqVO) {
		log.info("渠道接口：下载对账文件，请求参数：{}",reqVO);
		ReconDownFileResVO resVO = null;
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getTradeDownReconFileHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		resVO = (ReconDownFileResVO) tradePayExecutor.execute(reqVO);
		log.info("渠道接口：下载对账文件，响应结果：{}",resVO);
		return resVO;
	}



	@Override
	public BatchPayTradeResVO batchPay(BatchPayTradeReqVO reqVO) {
		log.info("渠道接口，批量代付，请求参数：{}",reqVO);
		BatchPayTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BatchPayTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		//根据请求信息判断走那条渠道，目前只有一条渠道，不根据路由信息制定
	    List<MerchantChannel> merchantChannels = merchantChannelMapper.selectByMerchantId(reqVO.getMerchantId());
		if(null == merchantChannels) {
			log.error("商户未配置渠道信息");
			resVO = new BatchPayTradeResVO(ChannelErrorCode.ERROR_0003,"商户未配置渠道信息");
			return resVO;
		}
		MerchantChannel merchantChannel = merchantChannels.get(0);
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchTraeHandler(ChannelType.getChannelByChannelId(merchantChannel.getChannelId()));
		
		//根据批次号生成批量文件
		//文件名BD机构标识号YYYYMMDD4至8位的序号
		String fileName = "BD"+instId+DateUtil.getDays()+reqVO.getOrderNo().substring(reqVO.getOrderNo().length()-4, reqVO.getOrderNo().length());
		BigDecimal totalAmt = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		Long totalNum = 0l;
		try {
			String filePath = ""; 
			if(batchPayFilePath.endsWith(File.separator)) {
				filePath = batchPayFilePath + DateUtil.getDays() + File.separator;
			}else {
				filePath = batchPayFilePath + File.separator + DateUtil.getDays() + File.separator;
			}
		    FileUtils.createFile(fileName, filePath, FileSuffixEnums.REQ.getSuffix());
		    
		    SysConfig payerAccountConfig = sysConfigMapper.selectByPrimaryKey("BHPayerAccount");
			SysConfig payerNameConfig = sysConfigMapper.selectByPrimaryKey("BHPayerName");
			if(null == payerAccountConfig || null == payerNameConfig) {
				resVO = new BatchPayTradeResVO(ChannelErrorCode.ERROR_9000,"系统错误:渠道系统参数未配置");
				return resVO;
			}
			//文件内容
			StringBuffer buf = new StringBuffer();
			for(TradeDTO tradeDTO:reqVO.getTrades()) {
				String amountStr = tradeDTO.getTradeAmount().setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();
				//行与行之间得分隔符
        		buf.append("\n");
				buf.append(String.format("%s~%s~%s~%s~CNY~%s", tradeDTO.getSeqNo(),tradeDTO.getPayeeAccount(),
						tradeDTO.getPayeeName(),tradeDTO.getPayeeBankCode(),amountStr));
        		totalAmt = totalAmt.add(tradeDTO.getTradeAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        		totalNum ++;
			}
		    //文件头部
			StringBuffer bufHeader = new StringBuffer();
			bufHeader.append(String.format("<Header>~%s~%s~%d~%s~</Header>", payerAccountConfig.getSysValue(),payerNameConfig.getSysValue(),totalNum,totalAmt.toString()));
			bufHeader.append("\n");
			
		    FileUtils.appendWriteFile(bufHeader.toString(), fileName, filePath, FileSuffixEnums.REQ.getSuffix());
		    FileUtils.appendWriteFile(buf.toString(), fileName, filePath, FileSuffixEnums.REQ.getSuffix());
		    
		    BatchPayTradeInnerReqVO innerReqVO = new BatchPayTradeInnerReqVO();
			BeanUtils.copyProperties(reqVO, innerReqVO);
			innerReqVO.setTotalAmt(totalAmt);
			innerReqVO.setTotalNum(totalNum);
			innerReqVO.setFileName(fileName+FileSuffixEnums.REQ.getSuffix());
			resVO = (BatchPayTradeResVO) tradePayExecutor.execute(innerReqVO);
			resVO.setChannelId(ChannelType.BOHAI.getChannelId());
		}catch(IOException e) {
			resVO = new BatchPayTradeResVO(ChannelErrorCode.ERROR_1003,"生成批量文件失败");
			return resVO;
		}catch(Exception e) {
			log.error("系统异常：{}",e.getMessage());
			resVO = new BatchPayTradeResVO(ChannelErrorCode.ERROR_9000,"系统异常");
			return resVO;
		}
		log.info("渠道接口，批量代付，响应结果：{}",resVO);
		return resVO;
	}



	@Override
	public BatchPayTradeQueryResVO batchPayQuery(BatchPayTradeQueryReqVO reqVO) {
		log.info("渠道接口，批量代付结果查询，请求参数：{}",reqVO);
		BatchPayTradeQueryResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BatchPayTradeQueryResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchTradeQueryHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		//TODO 根据响应结果构建结果集
		resVO = (BatchPayTradeQueryResVO) tradePayExecutor.execute(reqVO);
		log.info("渠道接口，批量代付结果查询，响应结果:{}",resVO);
		return resVO;
	}



	@Override
	public BaseTradeResVO batchPaySingleQuery(BatchPaySingleQueryReqVO reqVO) {
		log.info("渠道接口，批量代付单笔结果查询，请求参数：{}",reqVO);
		BaseTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BaseTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchSingleQueryHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		resVO = tradePayExecutor.execute(reqVO);
		log.info("渠道接口，批量代付单笔结果查询，响应结果：{}",resVO);
		return resVO;
	}



	@Override
	public BaseTradeResVO batchPayRetry(BatchPayRetryReqVO reqVO) {
		log.info("渠道接口，批量代付触发查询，请求参数：{}",reqVO);
		BaseTradeResVO resVO = null;
		try {
			ValidationUtils.validate(reqVO);
		}catch(CloudApiException e) {
			log.error("参数校验失败:{}",e.getMessage());
			resVO = new BaseTradeResVO(e.getErrorCode(),e.getMessage());
			return resVO;
		}
		ITradePayExecutor tradePayExecutor = tradePayTypeHandlerFactory.getBatchPayRetryHandler(ChannelType.getChannelByChannelId(reqVO.getChannelId()));
		resVO = tradePayExecutor.execute(reqVO);
		log.info("渠道接口，批量代付触发查询，响应结果：{}",resVO);
		return resVO;
	}

	
	public static void main(String[] args) {
		String orderNo = "2018111423104500001";
		System.out.println(orderNo.substring(orderNo.length()-4, orderNo.length()));
	}
}
