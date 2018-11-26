package com.cloud.pay.trade.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.trade.dto.MerchantRouteDTO;
import com.cloud.pay.trade.entity.MerchantRouteConf;
import com.cloud.pay.trade.mapper.MerchantRouteConfMapper;

@Service
public class MerchantRouteConfService {

	private Logger log = LoggerFactory.getLogger(MerchantRouteConfService.class);

	@Autowired
	private MerchantRouteConfMapper merchantRouteConfMapper;

	public int update(MerchantRouteConf merchantRouteConf) {
		log.info("修改merchantRouteConf信息：{}", merchantRouteConf);
		return merchantRouteConfMapper.updateByPrimaryKeySelective(merchantRouteConf);
	}

	public int save(MerchantRouteConf merchantRouteConf) {
		log.info("保存merchantRouteConf信息：{}", merchantRouteConf);
		return merchantRouteConfMapper.insert(merchantRouteConf);
	}

	public int del(Integer id) {
		log.info("根据merchantRouteConfID[{}]删除：{}", id);
		return merchantRouteConfMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 查询限额列表
	 * @param type
	 * @param status
	 * @param merchantName
	 * @param startTime
	 * @param endTime
	 * @param userMerchantId
	 * @return
	 */
	public List<MerchantRouteDTO> getmerchantRouteConfList(Integer type, Integer status, String merchantName, Date startTime,
			Date endTime, Integer userMerchantId) {
		return merchantRouteConfMapper.getMerchantRouteConfList(type, status, merchantName, startTime, endTime, userMerchantId);
	}
	
}
