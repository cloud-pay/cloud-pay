package com.cloud.pay.merchant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.merchant.dto.MerchantDTO;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;

@Service
public class MerchantService {

	@Autowired
	private MerchantBaseInfoMapper merchantBaseInfoMapper;
	
	public List<MerchantDTO> getMerchantDTOs(String type) {
		return merchantBaseInfoMapper.getMerchantDTOs(type);
	}
	
}
