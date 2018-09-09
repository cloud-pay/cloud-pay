package com.cloud.pay.merchant.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.pay.merchant.constant.MerchantConstant;
import com.cloud.pay.merchant.dto.MerchantDTO;
import com.cloud.pay.merchant.entity.MerchantAttachementInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantFeeInfo;
import com.cloud.pay.merchant.mapper.MerchantAttachementInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantBankInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantFeeInfoMapper;

@Service
public class MerchantService {

	@Autowired
	private MerchantBaseInfoMapper baseInfoMapper;
	
	@Autowired
	private MerchantBankInfoMapper bankInfoMapper;
	
	@Autowired
	private MerchantFeeInfoMapper feeInfoMapper;
	
	@Autowired
	private MerchantAttachementInfoMapper attachementInfoMapper;

	public List<MerchantDTO> getMerchantDTOs(String type) {
		return baseInfoMapper.getMerchantDTOs(type);
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
		return baseInfoMapper.getMerchantList(orgId, code, name, startTime, endTime);
	}

	public int update(MerchantBaseInfo baseInfo) {
		return baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
	}
	
	@Transactional
	public Map<String, Object> select(Integer id) {
		Map<String, Object> merchantMap = new HashMap<>();
		merchantMap.put("baseInfo", baseInfoMapper.selectByPrimaryKey(id));
		merchantMap.put("bankInfo", bankInfoMapper.selectByMerchantId(id));
		merchantMap.put("feeInfo", feeInfoMapper.selectByMerchantId(id));
		List<MerchantAttachementInfo> infos = attachementInfoMapper.selectByMerchantId(id);
		if(infos != null) {
			for(MerchantAttachementInfo info : infos) {
				if(MerchantConstant.BUSINESS == info.getAttachementType()) {
					merchantMap.put("businessPath", info.getAttachementPath());
				} else if(MerchantConstant.BANK_CARD == info.getAttachementType()) {
					merchantMap.put("bankCardPath", info.getAttachementPath());
				} else if(MerchantConstant.CERT == info.getAttachementType()) {
					merchantMap.put("certPath", info.getAttachementPath());
				} else if(MerchantConstant.PROTOCOL == info.getAttachementType()) {
					merchantMap.put("protocolPath", info.getAttachementPath());
				}
				
			}
		}
		return merchantMap;
	}

}
