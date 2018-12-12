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
		String result = downloadFile(fileName, "BAT ", reconFilePath);
		if(!"SUCCESS".equals(result)) {
			resVO =  new ReconDownFileResVO(ChannelErrorCode.ERROR_1002,"获取对账文件失败");
			log.info("渤海批量代付-响应参数：{}",resVO);
			return resVO;
		}
		String fileFullPath = "";
		if(reconFilePath.startsWith(File.separator)) {
			fileFullPath = reconFilePath + fileName;
		}else {
			fileFullPath = reconFilePath + File.separator+ fileName;
		}
		resVO = new ReconDownFileResVO();
		resVO.setRespCode(ChannelContants.CHANNEL_RESP_CODE_SUCCESS);
		resVO.setFilePath(fileFullPath);
		return resVO;
	}
  
}
