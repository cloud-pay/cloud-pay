package com.cloud.pay.recon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.recon.dto.ReconExceptionBohaiDTO;
import com.cloud.pay.recon.entity.ReconExceptionBohai;
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
	 public int insertShortPlat(String reconDate,Integer channelId,Integer reconId,Integer exceptionType) {
		 return reconExceptionBohaiMapper.insertShortPlat(reconDate,channelId,reconId, exceptionType);
	 }
	 
	 /**
	  * 根据渠道编号获取异常数据数量
	  * @param channelId
	  * @return
	  */
	 public List<ReconExceptionBohaiDTO> selectCountByChannelId(Integer channelId,Integer reconId) {
		 return reconExceptionBohaiMapper.selectCountByChannelId(channelId,reconId);
	 }
	 
	 public List<ReconExceptionBohaiDTO> selectListByParam(Integer channelId,Integer reconId,String orderNo,Integer exceptionType){
		  return reconExceptionBohaiMapper.selectListByParam(channelId, reconId,orderNo,exceptionType);
	 }
	 
	 /**
	  *  根据对账ID删除异常数据
	  * @param reconId
	  * @return
	  */
	 public int deleteByReconId(Integer reconId) {
		 return reconExceptionBohaiMapper.deleteByReconId(reconId);
	 }
	 
	 public List<ReconExceptionBohai> selectListByExceptionType(Integer exceptionType){
		 return reconExceptionBohaiMapper.selectListByExceptionType(exceptionType);
	 }
	 
	   
	 public int updatePostPoneHis() {
		  return reconExceptionBohaiMapper.updatePostPoneHis();
	 }
	    
	    /**
	     * 删除历史延期对账成功的数据
	     * @return
	     */
	 public int deletePostPoneHis() {
		 return reconExceptionBohaiMapper.updatePostPoneHis();
	 }
	 
	 public int batchInsert(List<ReconExceptionBohai> list) {
		 return reconExceptionBohaiMapper.batchInsert(list);
	 }
}
