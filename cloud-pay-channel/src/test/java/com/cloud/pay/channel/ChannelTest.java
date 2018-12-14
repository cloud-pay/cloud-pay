package com.cloud.pay.channel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cloud.pay.channel.dto.TradeDTO;
import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.BaseTradeResVO;
import com.cloud.pay.channel.vo.BatchPayRetryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
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
		reqVO.setMerchantId(1);
		reqVO.setOrderNo("2018111500000014");  //2018开头返回成功，其他失败
		reqVO.setPayeeAccount("1231312313131312");
		reqVO.setPayeeName("老黑");
		reqVO.setPayeeBankCode("308584000013");
		reqVO.setAmt(new BigDecimal("1"));
		reqVO.setTradeDate("20181115 22:17:15");
		PayTradeResVO response = cloudApiService.pay(reqVO);
		System.out.println("===============================================");
		System.out.println(response);
	}
	
	/**
	 * 单笔代付查询
	 */
	@Test
	public void tradePayQuery() {
		PayTradeQueryReqVO tradeReq = new PayTradeQueryReqVO();
		tradeReq.setMerchantId(1);
		tradeReq.setTradeDate("20181118 11:12:15");
		tradeReq.setOrderNo("2018111800000002");
		tradeReq.setChannelId(1);
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
		reqVO.setMerchantId(1);
		reqVO.setOrderNo("2018111800000003");  //2018开头返回成功，其他失败
		reqVO.setPayeeAccount("1231312313131312");
		reqVO.setPayeeName("老黑");
		reqVO.setAmt(new BigDecimal("1"));
		reqVO.setTradeDate("20181118 11:12:15");
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
		reqVO.setMerchantId(1);
		reqVO.setChannelId(1);
		reqVO.setReconDate("20180904");
		ReconDownFileResVO response = cloudApiService.downReconFile(reqVO);
		System.out.println("========================================================");
		System.out.println(response);
	}
	
	/**
	 * 批量代付重新触发
	 */
	@Test
	public void RetryBatchPay() {
		BatchPayRetryReqVO reqVO = new BatchPayRetryReqVO();
		reqVO.setTradeDate("20181028 21:31:15");
		reqVO.setTotalNum(2l);
		reqVO.setTotalAmt(new BigDecimal("20"));
		reqVO.setChannelId(1);
		reqVO.setMerchantId(1);
		reqVO.setOrderNo("2018110300000001");
		reqVO.setFileName("BD220015201702210001.req");
		BaseTradeResVO response = cloudApiService.batchPayRetry(reqVO);
		System.out.println(response);
	}
	
	/**
	 * 批量代付
	 */
	@Test
	public void batchPay() {
		BatchPayTradeReqVO reqVO = new BatchPayTradeReqVO();
		reqVO.setTradeDate("20181214 21:13:15");
		reqVO.setMerchantId(1);
		reqVO.setOrderNo("2018121400000001");
		List<TradeDTO> list = new ArrayList<TradeDTO>();
		TradeDTO tradeDTO1 = new TradeDTO();
		tradeDTO1.setSeqNo("1");
		tradeDTO1.setTradeAmount(new BigDecimal(23.56));
		tradeDTO1.setPayeeAccount("23424234242342432");
		tradeDTO1.setPayeeName("老黑");
		tradeDTO1.setPayeeBankCode("308584000013");
		list.add(tradeDTO1);
		
		TradeDTO tradeDTO2 = new TradeDTO();
		tradeDTO2.setSeqNo("2");
		tradeDTO2.setTradeAmount(new BigDecimal(23.56));
		tradeDTO2.setPayeeAccount("23423424232342342");
		tradeDTO2.setPayeeName("老黑");
		tradeDTO2.setPayeeBankCode("308584000013");
		list.add(tradeDTO2);
		reqVO.setTrades(list);
		BatchPayTradeResVO response = cloudApiService.batchPay(reqVO);
	    System.out.println("===================================================");
	    System.out.println(response);
	}
	
	/**
	 * 批量结果查询
	 */
	@Test
	public void batchPayQuery() {
		BatchPayTradeQueryReqVO reqVO = new BatchPayTradeQueryReqVO();
		reqVO.setTradeDate("20181214 21:13:15");
		reqVO.setFileName("YD220015201812140001.req");
		reqVO.setBatchOrderNo("2018121400000001");
		reqVO.setOrderNo("2018121400000001");
		reqVO.setChannelId(1);
		reqVO.setMerchantId(1);
		BatchPayTradeQueryResVO response = cloudApiService.batchPayQuery(reqVO);
		System.out.println("===================================================");
		System.out.println(response);
	}
	
}
