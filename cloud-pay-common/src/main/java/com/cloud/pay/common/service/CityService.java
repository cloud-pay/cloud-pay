package com.cloud.pay.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cloud.pay.common.entity.Provincial;
import com.cloud.pay.common.mapper.ProvincialMapper;

@Service
public class CityService {

	@Autowired
	private ProvincialMapper provincialMapper;
	
	@Cacheable(value = "cloud-pay", key="'provincial'")
	public List<Provincial> selectProvincialList() {
		return provincialMapper.selectList();
	}
}
