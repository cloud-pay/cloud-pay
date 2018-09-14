package com.cloud.pay.channel;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.PayTradeQueryReqVO;
import com.cloud.pay.channel.vo.PayTradeQueryResVO;
import com.cloud.pay.channel.vo.PayTradeReqVO;
import com.cloud.pay.channel.vo.PayTradeResVO;
import com.cloud.pay.channel.vo.PayUnionTradeReqVO;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ChannelTest {
    
	@Autowired
	private ICloudApiService cloudApiService;
	
	/**
	 * 单笔时代代付（大额代付）
	 */
	@Test
	public void tradePay() {
		PayTradeReqVO reqVO = new PayTradeReqVO();
		reqVO.setMerchantNo("2018082800000001");
		reqVO.setOrderNo("2018082800000001");  //2018开头返回成功，其他失败
		reqVO.setPayerAccount("4333234234242342342");
		reqVO.setPayerName("老黑");
		reqVO.setPayeeAccount("4333234234242342342");
		reqVO.setPayeeName("李白");
		reqVO.setPayeeBankCode("3100015");
		reqVO.setAmt(new BigDecimal("99.99"));
		BaseTradeResVO response = cloudApiService.pay(reqVO);
		System.out.println("===============================================");
		System.out.println(response);
	}
	
	/**
	 * 单笔代付查询
	 */
	@Test
	public void tradePayQuery() {
		PayTradeQueryReqVO tradeReq = new PayTradeQueryReqVO();
		tradeReq.setMerchantNo("2018082800000001");
		tradeReq.setTradeDate("20180829 23:21:00");
		tradeReq.setOrderNo("2018082800000001");
		tradeReq.setChannelCode("1");
		PayTradeQueryResVO response = cloudApiService.queryPay(tradeReq);
		System.out.println("===============================================");
		System.out.println(response);
	}
	
	/**
	 * 单笔银联代付查询
	 */
	@Test
	public void tradeUnionPay() {
		PayUnionTradeReqVO reqVO = new PayUnionTradeReqVO();
		reqVO.setMerchantNo("2018082800000001");
		reqVO.setOrderNo("2018082800000001");  //2018开头返回成功，其他失败
		reqVO.setPayerAccount("4333234234242342342");
		reqVO.setPayerName("老黑");
		reqVO.setPayeeAccount("4333234234242342342");
		reqVO.setPayeeName("李白");
		reqVO.setAmt(new BigDecimal("99"));
		PayTradeResVO response =cloudApiService.unionPay(reqVO);
		System.out.println("===============================================");
		System.out.println(response);
	}
	
	/**
	 * 下载对账文件
	 */
	@Test
	public void downReconFile() {
		ReconDownFileReqVO reqVO = new ReconDownFileReqVO();
		reqVO.setMerchantNo("");
		reqVO.setChannelCode("1");
		reqVO.setReconDate("20180904");
		ReconDownFileResVO response = cloudApiService.downReconFile(reqVO);
		System.out.println(response);
	}
}
