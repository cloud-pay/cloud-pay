package com.cloud.pay.channel.handler.bohai;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.utils.DateUtil;
import com.cloud.pay.common.utils.FileUtils;

@Service("bohaiDownReconFileExecutor")
public class BohaiTradeDownReconFileExecutor 
       extends BohaiTradeExecutor<BohaiCloudTradeParam,BohaiCloudTradeResult> 
       implements ITradePayExecutor<ReconDownFileReqVO,ReconDownFileResVO>{
	
	private Logger log = LoggerFactory.getLogger(BohaiTradeDownReconFileExecutor.class);
	
	@Value("${cloud.bohai.pay.instId}")
	private String instId;
	
	@Value("${cloud.bohai.recon.filePath}")
	private String reconFilePath;
	
	@Override
	public ReconDownFileResVO execute(ReconDownFileReqVO reqVO) {
		ReconDownFileResVO resVO = null;
		String fileName = "DCHK" + instId+ reqVO.getReconDate();
		String filePath = "";
		if(reconFilePath.startsWith(File.separator)) {
			filePath = reconFilePath + DateUtil.getDays() + File.separator;
		}else {
			filePath = reconFilePath + File.separator + DateUtil.getDays() + File.separator;
		}
		//创建文件目录
		FileUtils.isExist(filePath);
		String result = downloadFile(fileName, "BAT", filePath);
		if(!"SUCCESS".equals(result)) {
			resVO =  new ReconDownFileResVO(ChannelErrorCode.ERROR_1002,"获取对账文件失败");
			log.info("渤海批量代付-响应参数：{}",resVO);
			return resVO;
		}
		String fileFullPath = filePath + fileName;
		resVO = new ReconDownFileResVO();
		resVO.setRespCode(ChannelContants.CHANNEL_RESP_CODE_SUCCESS);
		resVO.setFilePath(fileFullPath);
		return resVO;
	}
  
}
