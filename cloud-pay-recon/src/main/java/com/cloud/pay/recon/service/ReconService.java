package com.cloud.pay.recon.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.common.entity.Channel;
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
			accountDate = new Date();
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
			recon.setFailReson("对账渠道不存在");
			reconMapper.updateByPrimaryKey(recon);
			return;
		}
		// step 1 调用渠道接口，下载对账文件
		// step 2 将渠道成功交易表成功以及其他要素相匹配的记录更新为对账成功
		// step 3 将渠道成功交易表失败以及其他要素相匹配的记录更新为对账成功
		// step 4 将渠道失败交易表成功以及其他要素相匹配的记录生成异常订单数据（记录异常原因为：渠道失败平台成功）
		// step 5 将渠道失败交易表失败以及其他要素相匹配的记录更新为对账失败
		// step 6 将渠道成功但是交易表除订单号以外其他要素不匹配的记录生成异常订单数据（记录异常原因为：和渠道相关信息不匹配）
		// step 7 将渠道失败但是交易表除订单号以外其他要素不匹配的记录生成异常订单数据(记录异常原因为：和渠道相关信息不匹配)
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
