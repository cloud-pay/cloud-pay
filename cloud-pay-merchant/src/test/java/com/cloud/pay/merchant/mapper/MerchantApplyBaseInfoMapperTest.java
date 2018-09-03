package com.cloud.pay.merchant.mapper;

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
public class MerchantApplyBaseInfoMapperTest {

	@Autowired
	private MerchantApplyBaseInfoMapper merchantBaseInfoMapper;
	
	@Test
	public void selectByPrimaryKeyTest() {
		System.out.println(merchantBaseInfoMapper.selectByPrimaryKey(1));
	}
	
	@Test
	public void getMerchantApplyDTOsTest() {
		System.out.println(merchantBaseInfoMapper.getMerchantApplyDTOs(1, "1", "2", 1, new Date(), new Date()));
	}
}
