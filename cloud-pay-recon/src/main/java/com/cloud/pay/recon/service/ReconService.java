package com.cloud.pay.recon.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.common.contants.ChannelType;
import com.cloud.pay.common.entity.Channel;
import com.cloud.pay.common.exception.CloudApiExcetion;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.service.ChannelService;
import com.cloud.pay.common.utils.DateUtil;
import com.cloud.pay.recon.dto.ReconDTO;
import com.cloud.pay.recon.entity.Recon;
import com.cloud.pay.recon.mapper.ReconMapper;

@Service
public class ReconService {
	private Logger log = LoggerFactory.getLogger(ReconService.class);
	
	@Autowired
	private ReconMapper reconMapper;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ReconChannelHandlerFactory reconChannelHandlerFactory;
	
	/**
	 * 初始化对账数据
	 * @param date
	 * @throws CloudPayException
	 */
	public void initRecon(String date) throws CloudPayException{
		log.info("初始化对账数据,初始化日期：{}",date);
		Date accountDate = null;
		if(StringUtils.isNotBlank(date)) {
			DateUtil.fomatDate(date);
		}else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE,  -1);
			accountDate = calendar.getTime();
		}
		List<Channel> channels = channelService.getUnInitChannelList(accountDate);
		if(null == channels || channels.size() <= 0) {
			throw new CloudPayException("所有渠道数据都已初始化");
		}
		//初始化渠道对账数据
		List<Recon> recons = new ArrayList<Recon>();
		for(Channel channel:channels) {
			Recon recon = new Recon();
			recon.setAccountDate(accountDate);
			recon.setChannelId(channel.getId());
			recon.setCreateTime(new Date());
			recon.setReconStatus(0);
			recons.add(recon);
		}
		reconMapper.batchInsert(recons);
		log.info("初始化对账数据，初始化日期:{}完成",accountDate);
	}
	
	/**
	 * 接收对账
	 * @param id
	 * @throws CloudPayException
	 */
	public void recon(Integer id) throws CloudPayException{
		log.info("对账开始");
		Recon recon = reconMapper.selectByPrimaryKey(id);
		if( 0 != recon.getReconStatus() && 2 != recon.getReconStatus()) {
			throw new CloudPayException("该记录已完成对账或正在对账中");
		}
		//修改对账记录状态为对账中
		recon.setReconStatus(3);
		recon.setFailReson("");
		int i = reconMapper.updateByPrimaryKeySelective(recon);
		log.info("渠道：{},对帐日期：{}对账开始",recon.getChannelId(),recon.getAccountDate());
		//step 3 开启一个新的线程，后台对账
		ReconThread reconThread = new ReconThread(recon,this);
		reconThread.start();
	}
	
	/**
	 * 对账
	 * @param recon
	 */
	public void recon(Recon recon) {
		log.info("对账开始");
		//根据对账记录，读取对应的渠道信息
		Channel channel = channelService.selectById(recon.getChannelId());
		if(null == channel) {
			recon.setReconStatus(2);
			recon.setFailReson("对账渠道不存在");
			reconMapper.updateByPrimaryKey(recon);
			return;
		}
		try {
			IReconServiceHandler reconServiceHandler = reconChannelHandlerFactory.getHandler(ChannelType.getChannelByChannelCode(channel.getChannelCode()));
			reconServiceHandler.handle(recon);
		}catch(CloudPayException e) {
			recon.setReconStatus(2);
			log.error("对账出现异常：{}",e);
			recon.setFailReson(e.getMessage());
			reconMapper.updateByPrimaryKey(recon);
			return;
		}
		
		log.info("对账结束");
	}
	
	public int save(Recon recon) {
		log.info("初始化对账数据");
		return reconMapper.insert(recon);
	}
	
	/**
	 *  查询对账列表
	 * @param reconStatus
	 * @param channelName
	 * @param tradeDate
	 * @return
	 */
	public List<ReconDTO> queryReconList(Integer reconStatus,String channelName,Date tradeDate){
		return reconMapper.queryReconList(reconStatus, channelName, tradeDate);
	} 
	
}
