package com.cloud.pay.trade;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.service.TradeService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TradeServiceTest {

	@Autowired
	private TradeService tradeService;
	
	@Test
	public void tradeTest() {
		Trade trade = new Trade();
		trade.setMerchantId(1);
		trade.setOrderNo("20181119091");
		trade.setPayeeBankAcctType(1);
		trade.setPayeeBankCard("32165478963258712");
		trade.setPayeeBankCode("332211452");
		trade.setPayeeBankName("中国银行");
		trade.setPayeeName("测试01");
		trade.setTradeTime(new Date());
		trade.setRemark("测试");
		trade.setLoaning(1);
		trade.setTradeAmount(new BigDecimal(30));
		tradeService.pay(trade);
	} 
}
