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
public class MerchantRouteConfMapperTest {

	@Autowired
	private MerchantRouteConfMapper merchantRouteConfMapper;
	
	@Test
	public void getMerchantRouteConfListTest() {
		System.out.println(merchantRouteConfMapper.getMerchantRouteConfList(1, 1,"mingc", new Date(), new Date(), 2));
	}
}
