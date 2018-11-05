package com.cloud.pay.merchant.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.merchant.dto.MerchantPrepayInfoDTO;
import com.cloud.pay.merchant.mapper.MerchantPrepayInfoMapper;

@Service
public class MerchantPrepayInfoService {

	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;

	/**
	 * 预缴户资金查询
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<MerchantPrepayInfoDTO> selectList(Integer merchantId, Date startTime, Date endTime, String type) {
		return merchantPrepayInfoMapper.selectList(merchantId, startTime, endTime, type);
	}

}
