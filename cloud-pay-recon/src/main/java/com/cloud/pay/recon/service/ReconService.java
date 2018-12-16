package com.cloud.pay.recon.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSException;
import com.cloud.pay.common.contants.ChannelType;
import com.cloud.pay.common.contants.FileSuffixEnums;
import com.cloud.pay.common.entity.Channel;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.mapper.SysConfigMapper;
import com.cloud.pay.common.service.ChannelService;
import com.cloud.pay.common.utils.DateUtil;
import com.cloud.pay.common.utils.FileUtils;
import com.cloud.pay.common.utils.OSSUnit;
import com.cloud.pay.merchant.dto.MerchantDTO;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.recon.dto.ReconDTO;
import com.cloud.pay.recon.entity.Recon;
import com.cloud.pay.recon.mapper.ReconMapper;
import com.cloud.pay.trade.dto.TradeDTO;
import com.cloud.pay.trade.mapper.TradeMapper;

@Service
public class ReconService {
	private Logger log = LoggerFactory.getLogger(ReconService.class);
	
	
	
	@Value("${cloud.merchant.recon.path}")
	private String merchantReconFilePath;
	
	@Value("${cloud.agent.recon.path}")
	private String agentReconFilePath;
	
	@Value("${cloud.oss.recon.file.path}")
	private String ossReconFilePath;
	
	@Autowired
	private ReconMapper reconMapper;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ReconChannelHandlerFactory reconChannelHandlerFactory;
	
	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private MerchantBaseInfoMapper merchantBaseInfoMapper;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;

	
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
		}catch(CloudPayException e) {
			recon.setReconStatus(2);
			log.error("对账出现异常：{}",e);
			recon.setFailReson(e.getMessage());
			reconMapper.updateByPrimaryKey(recon);
			return;
		}
		String reconDate = DateUtil.formatDate(recon.getAccountDate(), DateUtil.DATE_DAY_FORMAT);
		//生成商户对账文件
		createMerchantReconFile(reconDate);
		//生成代理商对账文件
		createAgentReconFile(reconDate);
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
	 * @return 1-生成对账文件成功 ; 0-生成对账文件失败
	 */
	public int createMerchantReconFile(String reconDate) {
		log.info("生成商户对账文件开始，生成日期:{}",reconDate);
		//step 1 判断是否所有的渠道都已完成对账
	    List<Recon> recons =  reconMapper.selectListByReconDate(DateUtil.fomatDate(reconDate));
	    for(Recon recon:recons) {
	    	if(1 != recon.getReconStatus() || 2 != recon.getReconStatus()) {
	    		log.info("渠道：{}未完成对账，无法生成对账文件",recon.getChannelId());
	    		return 0;
	    	}
	    }
		//step 2 获取所有的有效商户
	    Integer[] types = {4,5};
	    List<MerchantDTO>  merchants = merchantBaseInfoMapper.getAllNormalMerchant(types,null);
	    if(null == merchants) {
	    	log.info("暂无需要生成对账文件的有效商户");
	    	return 0;
	    }
	    //step 3 获取商户的交易信息
	    for(MerchantDTO merchant:merchants) {
	    	log.info("为商户{}生成对账文件",merchant.getName());
	    	List<TradeDTO> trades = tradeMapper.selectListByMerIdAndReconDate(merchant.getId(), DateUtil.fomatDate(reconDate));	    
	    	//step 4 根据交易表中的对账生成对账文件		
	        createMerchantReconFile(trades,merchant,reconDate);
	    }
		log.info("生成商户对账文件结束，生成日期:{}",reconDate);
		return 1;
	}
	
	/**
	 * 生成商户的对账文件
	 * @param trades
	 * @param merchant
	 * @param reconDate
	 */
	private void createMerchantReconFile(List<TradeDTO> trades,MerchantDTO merchant,String reconDate) {
		if(null == trades || trades.size() == 0) {
			log.info("商户{}对账日期{}不存在交易记录",merchant.getName(),reconDate);
			return;
		}
		try {
			//生成对账文件(生成的时候不上传OSS服务器，下游商户发起请求后再上传)
			String filePath = "";
			String reconDays = DateUtil.formatDate(DateUtil.fomatDate(reconDate),DateUtil.DATE_DAYS_FORMART);
			if(merchantReconFilePath.endsWith(File.separator)) {
				filePath = merchantReconFilePath +  reconDays;
			}else {
				filePath =  merchantReconFilePath + File.separator + reconDays;
			}
			String fileName =  merchant.getCode() + reconDays;
			if(FileUtils.createFile(fileName, filePath,FileSuffixEnums.TXT.getSuffix())) {
				StringBuffer buf = new StringBuffer();
				for(TradeDTO trade:trades) {
					//对账文件内容：商户号~代付交易流水~交易时间~交易金额~收款人姓名~收款人银行账号~收款人联行号~交易状态~交易状态描述
					buf.append(merchant.getCode()).append("~").append(trade.getOrderNo()).append("~").append(DateUtil.formatDate(trade.getTradeTime(), DateUtil.DATE_TIME_FORMAT)).append("~");
					buf.append(trade.getTradeAmount()).append("~").append(trade.getPayeeName()).append("~").append(trade.getPayeeBankCard()).append("~");
					buf.append(trade.getPayeeBankCode()).append("~").append(trade.getReconStatus());
					buf.append(System.getProperty("line.separator"));
				}
				FileUtils.writeTxtFile(buf.toString(), fileName, filePath);
			}
		}catch(IOException e) {
			log.error("生成商户{}，对账日期：{}对账文件失败:{}",merchant.getName(),reconDate,e);
		}
	}
	
	/**
	 * 判断商户文件是否存在
	 * @param mchCode
	 * @param reconDate
	 * @return
	 * @throws IOException
	 */
	private boolean isExistMchReconFile(String mchCode,String reconDate) throws IOException{
		String filePath = "";
		String reconDays = DateUtil.formatDate(DateUtil.fomatDate(reconDate),DateUtil.DATE_DAYS_FORMART);
		if(merchantReconFilePath.endsWith(File.separator)) {
			filePath = merchantReconFilePath +  reconDays;
		}else {
			filePath =  merchantReconFilePath + File.separator + reconDays;
		}
		String fileName =  mchCode + reconDays;
		return FileUtils.isExist(fileName, filePath,FileSuffixEnums.TXT.getSuffix());
	}
	
	
	/**
	 * 生成代理商对账文件
	 * 生成商户对账文件之前，需检查当前所有通道是不是都已对账完成，
	 * 如果对账尚有对账中或者对账失败的通道，等待其他通道完成对账后，再开始生成对账文件
	 * 对账文件命名：代理商（机构）号+对账日期.txt
	 * 对账文件内容：商户号~代付交易流水~交易时间~交易金额~收款人姓名~收款人银行账号~收款人联行号~交易状态~交易状态描述
	 * 对账文件存储路径：/reconFile/agent/{reconDate}/{文件名}
	 * @param reconDate 格式：yyyy-MM-dd
	 * @return 1-生成对账文件成功 ; 0-生成对账文件失败
	 */
	public int createAgentReconFile(String reconDate) {
		log.info("生成代理商对账文件开始,生成日期：{}",reconDate);
		//step 1 判断是否所有的渠道都已完成对账
	    List<Recon> recons =  reconMapper.selectListByReconDate(DateUtil.fomatDate(reconDate));
	    for(Recon recon:recons) {
	    	if(1 != recon.getReconStatus() || 2 != recon.getReconStatus()) {
	    		log.info("渠道：{}未完成对账，无法生成对账文件",recon.getChannelId());
	    		return 0;
	    	}
	    }
		//step 2 获取所有的有效机构代理商
	    Integer[] types = {1};
	    List<MerchantDTO>  merchants = merchantBaseInfoMapper.getAllNormalMerchant(types,null);
	    if(null == merchants) {
	    	log.info("暂无需要生成对账文件的有效代理商");
	    	return 0;
	    }
		//step 3 获取机构和下属商户的所有交易并根据交易表中的对账结果生成对账文件
	    for(MerchantDTO merchant:merchants) {
	    	log.info("为商户{}生成对账文件",merchant.getName());
	    	List<TradeDTO> trades = tradeMapper.selectListByMerIdAndReconDate(merchant.getId(), DateUtil.fomatDate(reconDate));	    
	    	//step 4 根据交易表中的对账生成对账文件		
	    	createAgentReconFile(trades,merchant,reconDate);
	    }
		log.info("生成代理商对账文件结束,生成日期：{}",reconDate);
		return 1;
	}
	
	
	/**
	 * 生成代理商的对账文件
	 * @param trades
	 * @param merchant
	 * @param reconDate
	 */
	private void createAgentReconFile(List<TradeDTO> trades,MerchantDTO merchant,String reconDate) {
		if(null == trades || trades.size() == 0) {
			log.info("代理商{}对账日期{}不存在交易记录",merchant.getName(),reconDate);
			//生成代理商下级商户的对账文件
			createChildMchRecondFile(null, merchant.getId(), reconDate,merchant.getCode());
			return;
		}
		Map<String,String> fileMap = createAgentReconFile(merchant.getCode(), reconDate);
		try {
			if(null != fileMap && fileMap.size() > 0) {
				String filePath = fileMap.get("filePath");
				String fileName = fileMap.get("fileName");
				StringBuffer buf = new StringBuffer();
				for(TradeDTO trade:trades) {
					//对账文件内容：商户号~代付交易流水~交易时间~交易金额~收款人姓名~收款人银行账号~收款人联行号~交易状态~交易状态描述
					buf.append(merchant.getCode()).append("~").append(trade.getOrderNo()).append("~").append(DateUtil.formatDate(trade.getTradeTime(), DateUtil.DATE_TIME_FORMAT)).append("~");
					buf.append(trade.getTradeAmount()).append("~").append(trade.getPayeeName()).append("~").append(trade.getPayeeBankCard()).append("~");
					buf.append(trade.getPayeeBankCode()).append("~").append(trade.getReconStatus());
					buf.append(System.getProperty("line.separator"));
				}
				FileUtils.writeTxtFile(buf.toString(), fileName, filePath);
			}else {
				log.info("生成代理商：{}，对账文件失败",merchant.getCode());
			}
		}catch(IOException e){
			log.error("生成代理商:{}，对账日期：{}对账文件失败:{}",merchant.getName(),reconDate,e);
		}
		//生成代理商下级商户的对账文件
		createChildMchRecondFile(fileMap, merchant.getId(), reconDate,merchant.getCode());
	}
	
	
	private Map<String,String> createAgentReconFile(String code,String reconDate) {
		Map<String,String> map = null;
		try {
			String filePath = "";
			String reconDays = DateUtil.formatDate(DateUtil.fomatDate(reconDate),DateUtil.DATE_DAYS_FORMART);
			if(agentReconFilePath.endsWith(File.separator)) {
				filePath = agentReconFilePath +  reconDays;
			}else {
				filePath =  agentReconFilePath + File.separator + reconDays;
			}
			map =  new HashMap<>();
			map.put("filePath", filePath);
			String fileName =  code + reconDays;
			map.put("fileName", fileName);
			FileUtils.createFile(fileName, filePath,FileSuffixEnums.TXT.getSuffix());
		}catch(IOException e) {
			log.error("生成代理商{},对账日期：{},对账文件失败：{}",code,reconDate,e);
		}
		return map;
	}
	
	
	/**
	 *  将代理商下级商户的对账文件信息同步到代理商的对账文件中
	 * @param fileMap
	 * @param agentId
	 * @param reconDate
	 */
	private void createChildMchRecondFile(Map<String,String> fileMap,Integer agentId,String reconDate,String agentCode) {
		log.info("追加代理商：{}下游商户文件开始",agentCode);
		try {
			String filePath = "";
			String fileName = "";
			String toFileFullPath = "";
			if(null != fileMap && fileMap.size() > 0) {
				filePath = fileMap.get("filePath");
				fileName = fileMap.get("fileName");
				
			}
			//获取代理商下级商户
			Integer[] types = {4,5};
			List<MerchantDTO> merchants = merchantBaseInfoMapper.getAllNormalMerchant(types, agentId);
			for(MerchantDTO merchant:merchants) {
				//判断商户是否已经生成对账文件
				if(!isExistMchReconFile(merchant.getCode(), reconDate)) {
					log.info("代理商{}下级商户{}暂生成对账文件",agentId,merchant.getCode());
					continue;
				}
				//判断代理商是否已经生成对账文件
				if(StringUtils.isBlank(filePath)) {
					fileMap = createAgentReconFile(agentCode, reconDate);
					if(null != fileMap && fileMap.size() > 0) {
						filePath = fileMap.get("filePath");
						fileName = fileMap.get("fileName");
					}
				}
				//代理商文件路径
				toFileFullPath = filePath + File.separator + fileName + ".txt";
	        	log.info("写入文件路径：{}",toFileFullPath);
				//商户文件路径
	        	String reconDays = DateUtil.formatDate(DateUtil.fomatDate(reconDate),DateUtil.DATE_DAYS_FORMART);
	        	String mchFilePath = "";
				if(merchantReconFilePath.endsWith(File.separator)) {
					mchFilePath = merchantReconFilePath +  reconDays;
				}else {
					mchFilePath =  merchantReconFilePath + File.separator + reconDays;
				}
				String mchFileName =  merchant.getCode() + reconDays;
	        	String fromFileFullPath = mchFilePath + File.separator +  mchFileName + ".txt";
				FileUtils.writeTxtFromOtherTxtFile(fromFileFullPath, toFileFullPath, true);	
			}
		}catch(IOException e) {
			log.error("生成代理商{}，对账日期：{}，商户数据对账文件失败：{}",agentId,reconDate,e);
		}
		log.info("追加代理商：{}下游商户文件结束",agentCode);
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
	 * @param isUploadOss   是否上传到oss服务器(通过页面下载时，不需要上传)
	 * @return filePath 对账文件路径
	 */
    public String queryReconFile(Integer merchantId,String mchCode,String reconDate,Integer mchType,boolean isUploadOss) {
    	//查询商户信息
    	MerchantBaseInfo baseInfo = merchantBaseInfoMapper.selectByPrimaryKey(merchantId);
    	String reconDays = reconDate;//DateUtil.formatDate(DateUtil.formatDate(reconDate, "yyyyMMdd"),DateUtil.DATE_DAYS_FORMART);
    	String reconFilePath = "";
    	String reconFileName =  baseInfo.getCode() + reconDays + ".txt";;
        //构建对账文件路径
    	if(1 == mchType) {
    		 //商户文件路径
    		if(merchantReconFilePath.endsWith(File.separator)) {
    			reconFilePath = merchantReconFilePath +  reconDays;
			}else {
				reconFilePath =  merchantReconFilePath + File.separator + reconDays;
			}
    	}else {
    		//代理商文件路径
    		if(agentReconFilePath.endsWith(File.separator)) {
    			reconFilePath = agentReconFilePath +  reconDays;
			}else {
				reconFilePath =  agentReconFilePath + File.separator + reconDays;
			}
    	}
    	String reconFileFullPath = reconFilePath + File.separator +  reconFileName ;
    	File file = new File(reconFileFullPath);
    	if(!file.exists()) {
    		return null;
    	}
    	//判断是否需要上传到OSS
    	if(!isUploadOss) {
    		return reconFileFullPath;
    	}
    	
    	SysConfig accessKeyIdConfig = null;
    	SysConfig secretAccessKeyConfig = null;
    	try {
    		accessKeyIdConfig = sysConfigMapper.selectByPrimaryKey("ossAccessKeyId");
    		secretAccessKeyConfig = sysConfigMapper.selectByPrimaryKey("ossSecretAccessKey");
    	}catch(Exception e) {
    		log.error("读取OSS服务器配置错误：{}",e);
    		return null;
    	}
    	InputStream is = null;
    	String ossReconFileFullPath = "";
    	if(ossReconFilePath.endsWith(File.separator)) {
    		ossReconFileFullPath = ossReconFilePath + reconDays + File.separator + reconFileName;
    	}else {
    		ossReconFileFullPath = ossReconFilePath + File.separator + reconDays + File.separator + reconFileName;
    	}
    	ossReconFileFullPath = ossReconFileFullPath.replaceAll("\\\\", "/");
    	try {
	    	is = new FileInputStream(file);  
	    	OSSUnit.uploadObject2OSS(OSSUnit.getOSSClient(accessKeyIdConfig.getSysValue(),secretAccessKeyConfig.getSysValue()), 
	    			is, reconFileName, file.length(), mchCode, ossReconFileFullPath);
//	    	OSSUnit.uploadObject2OSS(OSSUnit.getOSSClient(accessKeyIdConfig.getSysValue(),secretAccessKeyConfig.getSysValue()), 
//	    			is, reconFileName, file.length(), "zhengyan01", ossReconFileFullPath);
    	}catch(OSSException e) {
    		log.error("获取对账文件失败，{}",e);
    		return null;
    	}catch(Exception e) {
    		log.error("获取对账文件失败，{}",e);
    		return null;
    	}
    	return ossReconFileFullPath;
    }
}
