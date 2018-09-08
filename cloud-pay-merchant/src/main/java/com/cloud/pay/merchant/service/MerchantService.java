package com.cloud.pay.merchant.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.merchant.dto.MerchantDTO;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;

@Service
public class MerchantService {

	@Autowired
	private MerchantBaseInfoMapper merchantBaseInfoMapper;

	public List<MerchantDTO> getMerchantDTOs(String type) {
		return merchantBaseInfoMapper.getMerchantDTOs(type);
	}

	/**
	 * 列表查询list
	 * 
	 * @param orgId
	 * @param code
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<MerchantDTO> getMerchantList(Integer orgId, String code, String name, Date startTime, Date endTime) {
		return merchantBaseInfoMapper.getMerchantList(orgId, code, name, startTime, endTime);
	}

	public int update(MerchantBaseInfo baseInfo) {
		return merchantBaseInfoMapper.updateByPrimaryKeySelective(baseInfo);
	}

}
