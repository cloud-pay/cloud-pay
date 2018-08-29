package com.cloud.pay.trade.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.trade.entity.AmountLimit;
import com.cloud.pay.trade.mapper.AmountLimitMapper;

@Service
public class AmountLimitService {

	private Logger log = LoggerFactory.getLogger(AmountLimitService.class);

	@Autowired
	private AmountLimitMapper amountLimitMapper;

	public int update(AmountLimit amountLimit) {
		log.info("修改AmountLimit信息：{}", amountLimit);
		return amountLimitMapper.updateByPrimaryKeySelective(amountLimit);
	}

	public int save(AmountLimit amountLimit) {
		log.info("修改AmountLimit信息：{}", amountLimit);
		return amountLimitMapper.insert(amountLimit);
	}

	public int del(Integer id) {
		log.info("根据AmountLimitID[{}]删除：{}", id);
		return amountLimitMapper.deleteByPrimaryKey(id);
	}

	public List<AmountLimit> getAmountLimitList(Integer type, String orgName, String merchantName, Date startTime,
			Date endTime) {
		return amountLimitMapper.getAmountLimitList(type, orgName, merchantName, startTime, endTime);
	}
}
