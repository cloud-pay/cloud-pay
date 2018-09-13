package com.cloud.pay.trade.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TradeMapperTest {

	@Autowired
	private TradeMapper tradeMapper;
	
	@Test
	public void getBatchTradeListTest() {
		System.out.println(tradeMapper.tradeStat(1, 1, new Date(), new Date()));
		System.out.println(tradeMapper.loanTradeStat(1, 1, new Date(), new Date()));
	}
}
