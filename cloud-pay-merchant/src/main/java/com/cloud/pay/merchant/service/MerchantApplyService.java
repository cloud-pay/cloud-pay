package com.cloud.pay.merchant.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.merchant.dto.MerchantApplyDTO;
import com.cloud.pay.merchant.mapper.MerchantApplyBaseInfoMapper;

@Service
public class MerchantApplyService {

	@Autowired
	private MerchantApplyBaseInfoMapper merchantApplyBaseInfoMapper;
	
	public List<MerchantApplyDTO> getMerchantDTOs(Integer orgId, String code,
			String name, Integer status,  Date startTime,
			Date endTime) {
		return merchantApplyBaseInfoMapper.getMerchantApplyDTOs(orgId, code, name, status, startTime, endTime);
	}
	
}
