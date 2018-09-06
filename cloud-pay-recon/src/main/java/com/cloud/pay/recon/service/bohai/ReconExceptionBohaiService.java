package com.cloud.pay.recon.service.bohai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.recon.mapper.ReconExceptionBohaiMapper;

/**
 * 对账异常数据
 * @author wangy
 */
@Service
public class ReconExceptionBohaiService {

	 @Autowired
	 private ReconExceptionBohaiMapper reconExceptionBohaiMapper;
	 
	 /**
	  * 新增短款异常数据
	  * @param reconDate
	  * @return
	  */
	 public int insertShortPlat(String reconDate) {
		 reconExceptionBohaiMapper.insertShortPlat(reconDate);
	 }
}
