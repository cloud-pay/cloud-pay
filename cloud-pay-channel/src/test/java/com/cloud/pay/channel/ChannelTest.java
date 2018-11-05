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
		reqVO.setOrderNo("2018110500000002");  //2018开头返回成功，其他失败
		reqVO.setPayeeAccount("1231312313131312");
		reqVO.setPayeeName("老黑");
		reqVO.setPayeeBankCode("308584000013");
		reqVO.setAmt(new BigDecimal("1"));
		reqVO.setTradeDate("20181105 21:53:15");
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
		tradeReq.setMerchantId(1);
		tradeReq.setTradeDate("20181031 22:29:15");
		tradeReq.setOrderNo("2018103100000007");
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
		reqVO.setOrderNo("2018103100000008");  //2018开头返回成功，其他失败
		reqVO.setPayeeAccount("1231312313131312");
		reqVO.setPayeeName("老黑");
		reqVO.setAmt(new BigDecimal("1"));
		reqVO.setTradeDate("20181031 22:52:15");
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
		reqVO.setTradeDate("20181103 23:00:15");
		reqVO.setTotalNum(2l);
		reqVO.setTotalAmt(new BigDecimal("20"));
		reqVO.setMerchantId(1);
		reqVO.setOrderNo("2018110300000005");
		reqVO.setFileName("BD220015201811030002.req");
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
		reqVO.setTradeDate("20181103 23:00:15");
		reqVO.setFileName("BD220015201811030002.req");
		reqVO.setBatchOrderNo("2018110300000005");
		reqVO.setOrderNo("2018110300000006");
		reqVO.setChannelId(1);
		reqVO.setMerchantId(1);
		BatchPayTradeQueryResVO response = cloudApiService.batchPayQuery(reqVO);
		System.out.println("===================================================");
		System.out.println(response);
	}
}
