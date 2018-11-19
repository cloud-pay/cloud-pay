package com.cloud.pay.trade;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cloud.pay.trade.entity.BatchTrade;
import com.cloud.pay.trade.service.BatchTradeService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BatchPayTest {
   
	@Autowired
	private BatchTradeService batchTradeService;
	
	@Test
	public void batchPay() {
		BatchTrade batchTrade = new BatchTrade();
		batchTrade.setPayerMerchantId(1);
		batchTrade.setBatchNo("2018100700001");
		batchTrade.setTotalAmount(new BigDecimal("10000"));
		batchTrade.setTotalCount(6);
		batchTrade.setTradeTime(new Date());
		batchTrade.setStatus(1);
		batchTradeService.batchPay(batchTrade, 
				"batchPayFile/20181007zhengyan01.txt", "zhengyan01",1);
	}
}
