package com.cloud.pay.recon.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<FillRecordDTO> selectListByParam(){
		return fillRecordMapper.selectListByParam();
	}
	
	public int insert(FillRecord fillRecord) {
		return fillRecordMapper.insert(fillRecord);
	}
}
