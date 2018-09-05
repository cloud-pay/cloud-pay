package com.cloud.pay.recon.service.bohai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.entity.Channel;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.service.ChannelService;
import com.cloud.pay.common.utils.DateUtil;
import com.cloud.pay.recon.entity.Recon;
import com.cloud.pay.recon.service.IReconServiceHandler;

/**
 * 渤海对账
 * @author wangy
 */
@Service("bohaiReconService")
public class BohaiReconService implements IReconServiceHandler {

	private Logger log = LoggerFactory.getLogger(BohaiReconService.class);
	@Autowired
	private ICloudApiService cloudApiService;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ReconChannelBohaiService reconChannelBohaiService;
	
	@Override
	public void handle(Recon recon) throws CloudPayException{
		log.info("对账-渤海银行-开始：{}",recon);
		Channel channel = channelService.selectById(recon.getChannelId());
		  //获取对账文件
		ReconDownFileReqVO downFileReqVO = new ReconDownFileReqVO();
		downFileReqVO.setChannelCode(channel.getId()+"");
		downFileReqVO.setReconDate(DateUtil.formatDate(recon.getAccountDate(), "yyyyMMdd"));
		log.info("对账-渤海银行-下载对账文件,请求渠道参数:{}",downFileReqVO);
		ReconDownFileResVO downFileResVo = cloudApiService.downReconFile(downFileReqVO);
		if(ChannelContants.CHANNEL_RESP_CODE_FAIL.equals(downFileResVo.getRespCode())) {
			throw new CloudPayException(downFileResVo.getErrorCode()+":"+downFileResVo.getErrorMessage());
		}
		log.info("对账-渤海银行-下载对账文件，响应参数：{}",downFileResVo);
		if(StringUtils.isBlank(downFileResVo.getFilePath())) {
			throw new CloudPayException("获取对账文件失败");
		}
	    //解析对账文件，并讲对账数据保存到数据库
		reconChannelBohaiService.saveChannelReconDate(downFileResVo.getFilePath());
		//开始对账
		//step1 TODO.... 更新交易订单表数据和渠道对账表数据元素一致的记录的状态（更新对账状态为银行状态），标识渠道比对账表记录为平账
		reconChannelBohaiService.updateReconStatusFlat();
		//step2 TODO.... 更新交易订单表和渠道对账表订单号一致，但其他元素不一致的记录的对账状态为失败，并生成异常记录，标识渠道对账表记录为不平账
		//step3 TODO.... 更新交易订单中存在但渠道不存在的记录的对账状态为延期对账
		//step4 TODO.... 渠道记录中存在但本地交易记录表中不存在的记录，生成对账异常记录
		//step5 TODO.... 检查异常订单记录表里面是否存在延期的记录，并判断延期记录是否对账状态，如果对账状态认为失败，则讲记录标识为异常记录并设置错误为“延期后对账失败”
		//step6 TODO.... 检查是否交易表中该渠道是否还有未对账未延期的记录
		//step7 TODO.... 检查异常表中是否存在该渠道的对账日期的异常记录，并汇总
		
	}
}
