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
	
//	@Test
	public void getBatchTradeListTest() {
		System.out.println(tradeMapper.tradeStat(1, 1, null, new Date(), 3, "org"));
		System.out.println(tradeMapper.loanTradeStat(1, 1, null, new Date(), 3, "merchant"));
	}
	
//	@Test
	public void selectTradeListTest() {
		System.out.println(tradeMapper.selectTradeList(3, null, null, null, null, null, new Date(), 3, "merchant"));
	}
	
//	@Test
	public void selectByBatchNoOrderBySeqTest() {
		System.out.println(tradeMapper.selectByBatchNoOrderBySeq("2018121408241652800000003"));
	}
	
	@Test
	public void selectMerchantFeeStatsTest() {
		System.out.println(tradeMapper.selectMerchantFeeStats(3, 1, new Date(), new Date(), 3, "merchant"));
		System.out.println(tradeMapper.selectMerchantFeeStats(3, 1, new Date(), new Date(), 3, "org"));
	}
	
	@Test
	public void selectOrgTradeFeeStatsTest() {
		System.out.println(tradeMapper.selectOrgTradeFeeStats(1, new Date(), new Date(), 3, "merchant"));
		System.out.println(tradeMapper.selectOrgTradeFeeStats(1, new Date(), new Date(), 3, "org"));
	}
}
