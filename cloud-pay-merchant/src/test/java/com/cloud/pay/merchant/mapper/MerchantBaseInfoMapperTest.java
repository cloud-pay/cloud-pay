package com.cloud.pay.merchant.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MerchantBaseInfoMapperTest {

	@Autowired
	private MerchantBaseInfoMapper merchantBaseInfoMapper;
	
	@Test
	public void selectByPrimaryKeyTest() {
		System.out.println(merchantBaseInfoMapper.selectByPrimaryKey(1));
	}
	
//	@Test
//	public void getBankListTest() {
//		System.out.println(merchantBaseInfoMapper.getMerchantDTOs("1"));
//	}
}
