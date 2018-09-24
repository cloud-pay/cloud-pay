package com.cloud.pay.channel.handler.bohai;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.DateUtil;
import com.cloud.pay.channel.utils.FtpUtil;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;
import com.cloud.pay.common.contants.ChannelErrorCode;

@Service("bohaiDownReconFileExecutor")
public class BohaiTradeDownReconFileExecutor 
       extends BohaiTradeExecutor<BohaiCloudTradeParam,BohaiCloudTradeResult> 
       implements ITradePayExecutor<ReconDownFileReqVO,ReconDownFileResVO>{
	
	private Logger log = LoggerFactory.getLogger(BohaiTradeDownReconFileExecutor.class);

	@Value("${cloud.bohai.pay.sftpHost}")
	private String sftpHost;
	
	@Value("${cloud.bohai.pay.sftUserName}")
	private String sftUserName;
	
	@Value("${cloud.bohai.pay.sftpPassword}")
	private String sftpPassword;
	
	@Value("${cloud.bohai.pay.sftPort}")
	private int sftpPort;
	
	@Value("${cloud.bohai.pay.sftpPath}")
	private String sftpPath;
	
	@Value("${cloud.bohai.pay.localPath}")
	private String localPath;
	
	
	@Override
	public ReconDownFileResVO execute(ReconDownFileReqVO reqVO) {
		ReconDownFileResVO resVO = null;
		try {
			String fileName = "DCHK" + "12345689"+ reqVO.getReconDate();
			String localFilePath = "";
			//构建文件本地存放目录
			if(localPath.endsWith(File.separator)) {
				localFilePath = localPath + "bohai" + File.separator + reqVO.getReconDate();
			}else {
				localFilePath = localPath + File.separator + "bohai" + File.separator + reqVO.getReconDate();
			}
			
			log.info("获取渤海对账文件,请求日期：{}，文件名：{}",reqVO.getReconDate(),fileName);
			FtpUtil.downloadSftpFile(sftpHost, sftUserName, sftpPassword, sftpPort, sftpPath, localFilePath, fileName);
			resVO = new ReconDownFileResVO();
			resVO.setFilePath(localFilePath + File.separator  +fileName);
			resVO.setChannelId(reqVO.getChannelId());
		} catch (Exception e) {
			log.error("获取对账文件失败");
			resVO = new ReconDownFileResVO(ChannelErrorCode.ERROR_2001,e.getMessage());
		}
		return resVO;
	}
  
}
