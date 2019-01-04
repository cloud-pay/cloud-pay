package com.cloud.pay.trade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cloud.pay.channel.dto.TradeDTO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.trade.entity.BatchTrade;
import com.cloud.pay.trade.service.BatchTradeService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BatchTradeServiceTest {

	@Autowired
	private BatchTradeService tradeService;
	
//	@Test
	public void auditTest() throws Exception {
		BatchTrade batchTrade = new BatchTrade();
		batchTrade.setId(7);
		batchTrade.setAuditOptinion("通过");
		batchTrade.setAuditor("admin");
		batchTrade.setAuditTime(new Date());
		batchTrade.setBatchNo("2018112010343486200000001");
		batchTrade.setStatus(2);
		tradeService.audit(batchTrade, "108040");
	} 
	
	@Test
	public void dealBatchTradeTest() throws Exception {
		BatchPayTradeQueryResVO resVO = new BatchPayTradeQueryResVO(null, null);
		resVO.setStatus(0);
		resVO.setChannelId(1);
		List<TradeDTO> trades = new ArrayList<>();
		TradeDTO dto1 = new TradeDTO();
		dto1.setSeqNo("1");
		dto1.setStatus(1);
		trades.add(dto1);
		TradeDTO dto2 = new TradeDTO();
		dto2.setSeqNo("2");
		dto2.setStatus(2);
		trades.add(dto2);
		resVO.setTrades(trades);
		tradeService.dealBatchTrade("2018122803533828000000002", 1, resVO);
	} 
}
