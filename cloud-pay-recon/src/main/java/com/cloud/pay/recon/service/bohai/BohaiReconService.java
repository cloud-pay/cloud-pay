package com.cloud.pay.recon.service.bohai;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.ReconDownFileReqVO;
import com.cloud.pay.channel.vo.ReconDownFileResVO;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.entity.Channel;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.service.ChannelService;
import com.cloud.pay.common.utils.DateUtil;
import com.cloud.pay.recon.constant.ReconExceptionTypeEnum;
import com.cloud.pay.recon.dto.ReconExceptionBohaiDTO;
import com.cloud.pay.recon.entity.Recon;
import com.cloud.pay.recon.entity.ReconExceptionBohai;
import com.cloud.pay.recon.mapper.ReconMapper;
import com.cloud.pay.recon.service.IReconServiceHandler;
import com.cloud.pay.recon.service.ReconExceptionService;
import com.cloud.pay.trade.dto.TradeDTO;
import com.cloud.pay.trade.mapper.TradeMapper;

/**
 * 渤海对账
 * @author wangy
 */
@Service("bohaiReconService")
public class BohaiReconService implements IReconServiceHandler {

	private Logger log = LoggerFactory.getLogger(BohaiReconService.class);
	@Autowired
	private ICloudApiService cloudApiService;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ReconMapper reconMapper;
	
	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private ReconChannelBohaiService reconChannelBohaiService;
	
	@Autowired
	private ReconExceptionService reconExceptionBohaiService;
	
	@Override
	public void handle(Recon recon) throws CloudPayException{
		log.info("对账-渤海银行-开始：{}",recon);
		Channel channel = channelService.selectById(recon.getChannelId());
		  //获取对账文件
		ReconDownFileReqVO downFileReqVO = new ReconDownFileReqVO();
		downFileReqVO.setChannelId(channel.getId());
		downFileReqVO.setReconDate(DateUtil.formatDate(recon.getAccountDate(), "yyyyMMdd"));
		log.info("对账-渤海银行-下载对账文件,请求渠道参数:{}",downFileReqVO);
		ReconDownFileResVO downFileResVo = cloudApiService.downReconFile(downFileReqVO);
		if(ChannelContants.CHANNEL_RESP_CODE_FAIL.equals(downFileResVo.getRespCode())) {
			throw new CloudPayException(downFileResVo.getErrorCode()+":"+downFileResVo.getErrorMessage());
		}
		log.info("对账-渤海银行-下载对账文件，响应参数：{}",downFileResVo);
		if(StringUtils.isBlank(downFileResVo.getFilePath())) {
			throw new CloudPayException("获取对账文件失败");
		}
		String reconDate = DateUtil.formatDate(recon.getAccountDate(), "yyyy-MM-dd");
		try {
			//删除异常数据明细
			reconExceptionBohaiService.deleteByReconId(recon.getId());
		    //解析对账文件，并将对账数据保存到数据库
			reconChannelBohaiService.saveChannelReconDate(downFileResVo.getFilePath());
			//更新交易订单表数据和渠道对账表数据元素一致的记录的状态，标识渠道比对账表记录为平账
			reconChannelBohaiService.updateReconStatusFlat(recon.getAccountDate());
			//检查是否存在延期的记录，先更新延期记录
			List<ReconExceptionBohai> postPoneRecord = reconExceptionBohaiService.selectListByExceptionType(ReconExceptionTypeEnum.EXCEPTION_TYPE_POSTPONE.getTypeCode());
		    if(null != postPoneRecord && postPoneRecord.size() > 0) {
		    	log.info("处理历史延期对账数据");
		    	reconExceptionBohaiService.updatePostPoneHis();
		    	reconExceptionBohaiService.deletePostPoneHis();
		    }
		    log.info("更新渠道存在但交易表中不存在的记录");
			int shortCount = reconChannelBohaiService.updateShortUnflat(reconDate);
			if(shortCount > 0) {
				log.info("更新渠道记录中存在但本地交易记录表中不存在的记录，生成对账异常记录，记录异常数据类型为：短款");
				reconExceptionBohaiService.insertShortPlat(reconDate,recon.getChannelId(),recon.getId(),ReconExceptionTypeEnum.EXCEPTION_TYPE_SHORT.getTypeCode());
			}
			log.info("更新交易订单中存在渠道中不存在的记录");
			List<TradeDTO> postPoneTrade = tradeMapper.selectLongRecord(reconDate);
			if(null != postPoneTrade && postPoneTrade.size() > 0) {
				log.info("更新交易订单中存在渠道中不存在的记录，记录数据异常类型为：延期，不影响对账结果");
				reconExceptionBohaiService.batchInsert(buildPostPoneExceptionDate(postPoneTrade, recon.getId(),ReconExceptionTypeEnum.EXCEPTION_TYPE_POSTPONE.getTypeCode(),"延期对账"));
			}
			log.info("更新交易订单表和渠道对账表订单号一致，但其他元素不一致的记录的对账状态为失败");
			//step4 更新交易订单表和渠道对账表订单号一致，但其他元素不一致的记录的对账状态为失败，并生成异常记录，标识渠道对账表记录为不平账(最后判断订单号一直但是未对账的记录则为不平帐记录)
	        List<TradeDTO> exceptionTrade = tradeMapper.selectExceptionRecord(reconDate);
	        if(null != exceptionTrade && exceptionTrade.size() > 0 ) {
	        	log.info("更新不平帐的记录");
	        	reconExceptionBohaiService.batchInsert(buildPostPoneExceptionDate(exceptionTrade, recon.getId(), ReconExceptionTypeEnum.EXCEPTION_TYPE_MISMATH.getTypeCode(),"数据不匹配"));
	        }
	        log.info("检查异常表中是否存在该渠道的对账日期的异常记录，并汇总");
		    int tradeCount = 100;
		    List<ReconExceptionBohaiDTO> exceptionCount = reconExceptionBohaiService.selectCountByChannelId(recon.getChannelId(),recon.getId());
		    int exceptionC = 0;
		    int postPoneC = 0;
		    if(null != exceptionCount && exceptionCount.size() > 0) {
		    	for(ReconExceptionBohaiDTO exception:exceptionCount) {
		    		if(exception.getExceptionType() == ReconExceptionTypeEnum.EXCEPTION_TYPE_POSTPONE.getTypeCode()) {
		    			postPoneC += exception.getExceptionCount();
		    		}else {
		    			exceptionC += exception.getExceptionCount();
		    		}
		    	}
		    	if(exceptionC > 0) {
		    		recon.setReconStatus(2);
		    		recon.setFailReson("存在异常数据");
		    	}
		    	recon.setExceptionTotal(exceptionC + postPoneC);
		    }else {
		    	recon.setExceptionTotal(0);
	    		recon.setReconStatus(1);
	    	}
		    reconMapper.updateByPrimaryKey(recon);
		}catch(Exception e) {
			log.error("对账异常：{}",e);
			throw new CloudPayException("对账异常：系统错误");
		}
	}
	
	/**
	 * 构建延期数据
	 * @param trades
	 * @param reconId
	 * @return
	 */
	private List<ReconExceptionBohai> buildPostPoneExceptionDate(List<TradeDTO> trades,Integer reconId,Integer typeCode,String reason){
		List<ReconExceptionBohai> reconExceptionBohais = new ArrayList<>();
		for(TradeDTO trade:trades) {
			ReconExceptionBohai reconExceptionBohai = new ReconExceptionBohai();
			reconExceptionBohai.setChannelId(trade.getChannelId());
			reconExceptionBohai.setReconId(reconId);
			reconExceptionBohai.setExceptionType(typeCode);
			reconExceptionBohai.setOrderNo(trade.getPlatOrderNo());
			reconExceptionBohai.setPayerAccount(trade.getPayerBankCard());
			reconExceptionBohai.setPayerName(trade.getPayerName());
			reconExceptionBohai.setPayeeAccount(trade.getPayeeBankCard());
			reconExceptionBohai.setPayeeName(trade.getPayeeName());
			reconExceptionBohai.setBankCode(trade.getPayeeBankCode());
			reconExceptionBohai.setTradeAmount(trade.getTradeAmount());
			String tradeStatus = "2";
			if(null != trade.getStatus() && trade.getStatus() == 2) {
				tradeStatus = "1";
			}
			reconExceptionBohai.setTradeStatus(tradeStatus);
			reconExceptionBohai.setExceptionReason(reason);
			reconExceptionBohai.setCreateTime(new Date());
			reconExceptionBohais.add(reconExceptionBohai);
		}
		return reconExceptionBohais;
	}
}
