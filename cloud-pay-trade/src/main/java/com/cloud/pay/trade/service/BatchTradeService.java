package com.cloud.pay.trade.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.trade.dto.BatchTradeDTO;
import com.cloud.pay.trade.mapper.BatchTradeMapper;

@Service
public class BatchTradeService {

	@Autowired
	private BatchTradeMapper batchTradeMapper;
	
	public List<BatchTradeDTO> getBatchTradeList(Integer status, Date startTime, Date endTime) {
		return batchTradeMapper.getBatchTradeList(status, startTime, endTime);
	}
}
