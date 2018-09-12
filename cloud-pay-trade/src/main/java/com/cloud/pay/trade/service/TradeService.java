package com.cloud.pay.trade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.mapper.TradeMapper;

@Service
public class TradeService {

	@Autowired
	private TradeMapper tradeMapper;


	public List<Trade> selectByBatchNo(String batchNo) {
		return tradeMapper.selectByBatchNo(batchNo);
	}
	
}
