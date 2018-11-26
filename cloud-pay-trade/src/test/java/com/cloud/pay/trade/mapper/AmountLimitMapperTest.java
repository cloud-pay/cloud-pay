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
public class AmountLimitMapperTest {

	@Autowired
	private AmountLimitMapper amountLimitMapper;
	
	@Test
	public void getAmountLimitListTest() {
		System.out.println(amountLimitMapper.getAmountLimitList(1, "mingc","mingc", new Date(), new Date(), 2));
	}
}
