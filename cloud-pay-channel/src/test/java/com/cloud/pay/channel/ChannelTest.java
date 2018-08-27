package com.cloud.pay.channel;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ChannelTest {
    
	@Autowired
	private ICloudApiService cloudApiService;
	
	@Test
	public void tradePay() {
		PayTradeReqVO reqVO = new PayTradeReqVO();
		reqVO.setMerchantNo("2018082800000001");
		reqVO.setOrderNo("2018082800000001");
		reqVO.setPayerAccount("4333234234242342342");
		reqVO.setPayerName("老黑");
		reqVO.setPayeeAccount("4333234234242342342");
		reqVO.setPayeeName("李白");
		reqVO.setPayeeBankCode("3100015");
		reqVO.setAmt(new BigDecimal("10000"));
		cloudApiService.pay(reqVO);
	}
	
	@Test
	public void tradePayQuery() {
		PayTradeQueryReqVO tradeReq = new PayTradeQueryReqVO();
		tradeReq.setMerchantNo("2018082800000001");
		tradeReq.setDate("20180829 23:21:00");
		tradeReq.setOrderNo("2018082800000001");
		tradeReq.setChannelNo("1");
		cloudApiService.queryPay(tradeReq);
	}
}
