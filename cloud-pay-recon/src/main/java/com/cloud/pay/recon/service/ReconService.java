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
import com.cloud.pay.common.exception.CloudApiException;
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
			IReconServiceHandler reconServiceHandler = reconChannelHandlerFactory.getHandler(ChannelType.getChannelByChannelId(channel.getId()));
			reconServiceHandler.handle(recon);
			
			String reconDate = DateUtil.formatDate(recon.getAccountDate(), DateUtil.DATE_DAY_FORMAT);
			//生成商户对账文件
			createMerchantReconFile(reconDate);
			//生成代理商对账文件
			createAgentReconFile(reconDate);
		}catch(CloudPayException e) {
			recon.setReconStatus(2);
			log.error("对账出现异常：{}",e);
			recon.setFailReson(e.getMessage());
			reconMapper.updateByPrimaryKey(recon);
			return;
		}
		log.info("对账结束");
	}
	
	/**
	 * 生成商户对账文件
	 * 生成商户对账文件之前，需检查当前所有通道是不是都已对账完成，
	 * 如果对账尚有对账中或者对账失败的通道，等待其他通道完成对账后，再开始生成对账文件
	 * 对账文件命名：商户号+对账日期.txt
	 * 对账文件内容：商户号~代付交易流水~交易时间~交易金额~收款人姓名~收款人银行账号~收款人联行号~交易状态~交易状态描述
	 * 对账文件存储路径：/reconFile/merchant/{reconDate}/{文件名}
	 * @param reconDate 格式：yyyy-MM-dd
	 */
	public void createMerchantReconFile(String reconDate) {
		log.info("生成商户对账文件开始，生成日期:{}",reconDate);
		//step 1 根据日期获取交易表中所有的商户信息
		//step 2 根据交易表中的对账生成对账文件		
		log.info("生成商户对账文件结束，生成日期:{}",reconDate);
	}
	
	/**
	 * 生成代理商对账文件
	 * 生成商户对账文件之前，需检查当前所有通道是不是都已对账完成，
	 * 如果对账尚有对账中或者对账失败的通道，等待其他通道完成对账后，再开始生成对账文件
	 * 对账文件命名：代理商（机构）号+对账日期.txt
	 * 对账文件内容：商户号~代付交易流水~交易时间~交易金额~收款人姓名~收款人银行账号~收款人联行号~交易状态~交易状态描述
	 * 对账文件存储路径：/reconFile/agent/{reconDate}/{文件名}
	 * @param reconDate 格式：yyyy-MM-dd
	 */
	public void createAgentReconFile(String reconDate) {
		log.info("生成代理商对账文件开始,生成日期：{}",reconDate);
		//step 1 获取所有的机构代理商
		//step 2 根据机构获取下属商户
		//step 3 获取机构和下属商户的所有交易并根据交易表中的对账结果生成对账文件
		log.info("生成代理商对账文件结束,生成日期：{}",reconDate);
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
	
	/**
	 * 获取对账文件(如果返回空，则表示对账文件未生成)
	 * @param merchantId
	 * @param recondDate
	 * @param mchType  1-商户，2-机构
	 * @param isUploadOss   是否上传到oss服务器
	 * @return filePath 对账文件路径
	 */
    public String queryReconFile(Integer merchantId,String recondDate,Integer mchType,boolean isUploadOss) {
        //TODO ...
        //对账文件生成规则
    	return null;
    }
}
