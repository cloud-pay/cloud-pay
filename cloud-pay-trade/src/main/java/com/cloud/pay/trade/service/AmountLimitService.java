package com.cloud.pay.trade.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.pay.trade.constant.AmountLimitConstant;
import com.cloud.pay.trade.dto.AmountLimitDTO;
import com.cloud.pay.trade.entity.AmountLimit;
import com.cloud.pay.trade.exception.AmountLimitException;
import com.cloud.pay.trade.mapper.AmountLimitMapper;

@Service
public class AmountLimitService {

	private Logger log = LoggerFactory.getLogger(AmountLimitService.class);

	@Autowired
	private AmountLimitMapper amountLimitMapper;

	@Transactional
	public int update(AmountLimit amountLimit) {
		checkExist(amountLimit.getType(), amountLimit.getMerchantId());
		log.info("修改AmountLimit信息：{}", amountLimit);
		return amountLimitMapper.updateByPrimaryKeySelective(amountLimit);
	}

	@Transactional
	public int save(AmountLimit amountLimit) {
		checkExist(amountLimit.getType(), amountLimit.getMerchantId());
		log.info("新增AmountLimit信息：{}", amountLimit);
		return amountLimitMapper.insert(amountLimit);
	}

	private void checkExist(Integer type, Integer merchantId) {
		log.info("查询已存在的限额信息，类型：{}，商户ID：{}", type, merchantId);
		List<AmountLimit> limits = amountLimitMapper.selectExist(type, merchantId);
		if(limits != null && limits.size() > 0) {
			if(AmountLimitConstant.PER_LIMIT == type) {
				throw new AmountLimitException("单笔限额已存在", null);
			} else if(AmountLimitConstant.MERCHNAT_LIMIT == type) {
				throw new AmountLimitException("商户限额已存在", null);
			} else if(AmountLimitConstant.ORG_LIMIT == type) {
				throw new AmountLimitException("机构限额已存在", null);
			} 
		}
		
	}
	
	@Transactional
	public int del(Integer id) {
		log.info("根据AmountLimitID[{}]删除：{}", id);
		return amountLimitMapper.deleteByPrimaryKey(id);
	}

	public List<AmountLimitDTO> getAmountLimitList(Integer type, String orgName, String merchantName, Date startTime,
			Date endTime) {
		List<AmountLimitDTO> dtos =  amountLimitMapper.getAmountLimitList(type, orgName, merchantName, startTime, endTime);
		for(AmountLimitDTO dto : dtos) {
			if(dto.getType() == 3) {
				dto.setOrgName(dto.getMerchantName());
				dto.setOrgName("");
			}
		}
		return dtos;
	}
}
