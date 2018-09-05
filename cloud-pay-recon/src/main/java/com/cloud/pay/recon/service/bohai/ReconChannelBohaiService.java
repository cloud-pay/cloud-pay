package com.cloud.pay.recon.service.bohai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.recon.constant.ReconTypeEnum;
import com.cloud.pay.recon.constant.bohai.ReconReturnCodeEnum;
import com.cloud.pay.recon.entity.ReconChannelBohai;
import com.cloud.pay.recon.mapper.ReconChannelBohaiMapper;

@Service
public class ReconChannelBohaiService {
	private Logger log = LoggerFactory.getLogger(ReconChannelBohaiService.class);
	
	@Autowired
	private ReconChannelBohaiMapper reconChannelBohaiMapper;
	
    public void saveChannelReconDate(String localPath) throws CloudPayException{
    	List<ReconChannelBohai> lists = createChannelReconDate(localPath);
    	if(null == lists || lists.size() <= 0) {
            log.info("读取对账文件失败");
    		throw new CloudPayException("读取对账文件失败");
    	}
    	//删除渠道记录表中得历史数据
    	reconChannelBohaiMapper.deleleAll();
    	//保存数据到数据库中
    	reconChannelBohaiMapper.batchInsert(lists);
	}
    
	
	private List<ReconChannelBohai> createChannelReconDate(String localPath) throws CloudPayException{
		List<ReconChannelBohai> lists = new ArrayList<ReconChannelBohai>();
		// step 1 TODO 解析对账文件
		 BufferedReader buf = null;
		 try {
			 String line = null;
			 buf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(localPath))));
			 while((line = buf.readLine()) != null){
				 ReconChannelBohai bohai = new ReconChannelBohai();
				 String[] str = line.trim().split("~");
				 bohai.setSingleOrBatch(StringUtils.isNotBlank(str[0])?ReconTypeEnum.getByChannelCode(str[0]):ReconTypeEnum.SINGLE.getLocalCode());
			     bohai.setInstid(StringUtils.isNotBlank(str[1])?str[1]:"");
			     bohai.setTradeOrderNo(StringUtils.isNotBlank(str[2])?str[2]:"");
			     bohai.setPayerAccount(StringUtils.isNotBlank(str[3])?str[3]:"");
			     bohai.setPayerName(StringUtils.isNotBlank(str[4])?str[4]:"");
			     bohai.setPayeeAccount(StringUtils.isNotBlank(str[5])?str[5]:"");
			     bohai.setPayeeName(StringUtils.isNotBlank(str[6])?str[6]:"");
			     bohai.setBankCode(StringUtils.isNotBlank(str[7])?str[7]:"");
			     bohai.setTradeAmount(new BigDecimal(StringUtils.isNotBlank(str[8])?str[8]:"0"));
			     bohai.setTradeStatus(StringUtils.isNotBlank(str[9])? ReconReturnCodeEnum.getStatus(str[9]):ReconReturnCodeEnum.RETURN_UNKNOWN.getStatus());
			     bohai.setTradeStatusDesc(StringUtils.isNotBlank(str[10])?str[10]:"");
			     bohai.setReconStatus(0);
			     bohai.setCreateTime(new Date());
			     lists.add(bohai);
			 }
			 
		} catch (FileNotFoundException e) {
			log.error("解析对账文件出错：{}",e);
			throw new CloudPayException("解析对账文件出错");
		} catch (IOException e) {
			log.error("解析对账文件出错：{}",e);
			throw new CloudPayException("解析对账文件出错");
		}finally {
			if(buf != null) {
				try {
					buf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lists;
	}
	
	/**
	 * 更新平帐记录
	 */
	public void updateReconStatusFlat() throws CloudPayException{
		log.info("对账-渤海银行-更新平帐记录");
		try {
		   reconChannelBohaiMapper.updateReconStatusFlat();
		}catch(Exception e) {
			throw new CloudPayException("更新平帐记录失败");
		}
		log.info("对账-渤海银行-更新平帐记录完成");
	}
}
