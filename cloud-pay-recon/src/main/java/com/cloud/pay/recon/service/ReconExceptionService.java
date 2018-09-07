package com.cloud.pay.recon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.recon.dto.ReconExceptionBohaiDTO;
import com.cloud.pay.recon.mapper.ReconExceptionBohaiMapper;

/**
 * 对账异常数据
 * @author wangy
 */
@Service
public class ReconExceptionService {

	 @Autowired
	 private ReconExceptionBohaiMapper reconExceptionBohaiMapper;
	 
	 /**
	  * 新增短款异常数据
	  * @param reconDate
	  * @return
	  */
	 public int insertShortPlat(String reconDate,Integer channelId) {
		 return reconExceptionBohaiMapper.insertShortPlat(reconDate,channelId);
	 }
	 
	 /**
	  * 根据渠道编号获取异常数据数量
	  * @param channelId
	  * @return
	  */
	 public Long selectCountByChannelId(Integer channelId) {
		 return reconExceptionBohaiMapper.selectCountByChannelId(channelId);
	 }
	 
	 public List<ReconExceptionBohaiDTO> selectListByParam(Integer channelId,Integer reconId){
		  return reconExceptionBohaiMapper.selectListByParam(channelId, reconId);
	 }
}
