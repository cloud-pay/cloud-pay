package com.cloud.pay.recon.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.recon.dto.FillRecordDTO;
import com.cloud.pay.recon.entity.FillRecord;
import com.cloud.pay.recon.mapper.FillRecordMapper;

@Service
public class FillRecordService {
   
	private Logger log = LoggerFactory.getLogger(FillRecordService.class);
	
	@Autowired
	private FillRecordMapper fillRecordMapper;
	
	/**
	 * 资金补登列表
	 * @return
	 */
	public List<FillRecordDTO> selectListByParam(String orgCode,String orgName,Date startTime,Date endTime){
		return fillRecordMapper.selectListByParam(orgCode,orgName,startTime,endTime);
	}
	
	public int insert(FillRecord fillRecord) {
		return fillRecordMapper.insert(fillRecord);
	}
	
	public void reversal(Integer id,Integer userId) throws CloudPayException{
		try {
			FillRecord fillRecord = fillRecordMapper.selectByPrimaryKey(id);
			//TODO ....冲正
			
			//修改补登记录的状态
			fillRecord.setStatus(2);
			fillRecord.setUpdateTime(new Date());
			fillRecord.setUpdatorId(userId);
			fillRecordMapper.updateByPrimaryKeySelective(fillRecord);
		}catch(Exception e) {
		    throw new CloudPayException("系统异常"); 
		}
	}
}
