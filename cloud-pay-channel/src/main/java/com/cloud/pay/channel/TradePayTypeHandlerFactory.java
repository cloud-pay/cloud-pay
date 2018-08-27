package com.cloud.pay.channel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.cloud.pay.channel.contants.ChannelType;
import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.ApplicationContextHolder;
import com.cloud.pay.channel.vo.BaseTradeReqVO;
import com.cloud.pay.common.exception.CloudApiExcetion;

/**
 * 渠道接口简单工厂
 * @author wangy
 */
@Component
public class TradePayTypeHandlerFactory {
   
	private final Map<String,String> tradePayMapper = new HashMap<>(); //代付通道执行类
	private final Map<String,String> tradePayQueryMapper = new HashMap<>(); //代付结果查询通道执行类
    
	/**
	 * 启动的时候将渠道接口信息直接加载到内存
	 * 如果多多个渠道则直接新增mapper的key-value即可
	 * 示例，加入新增民生代付通道，新增民生代付接口，则在tradePayMapper这个map里面新增key-value
	 * tradePayMapper.put("smms_trade_pay", "smmsTradePayExecutor");
	 */
	@PostConstruct
	public void init() {
		//渤海银行
		tradePayMapper.put(ChannelType.BOHAI.getChannelCode(), "bohaiTradePayExecutor");
		tradePayQueryMapper.put(ChannelType.BOHAI.getChannelCode(), "bohaiTradeQueryExecutor");
	}
	
	/**
	 * 根据交易类型获取代付渠道执行类
	 * @param tradeType
	 * @return
	 */
	public ITradePayExecutor getTradePayHandler(String tradeType) {
		 return getHandler(tradePayMapper, tradeType);
	}
	
	/**
	 * 根据交易类型获取代付结果渠道执行类
	 * @param tradeType
	 * @return
	 */
	public ITradePayExecutor getTradePayQueryHandler(String tradeType) {
		  return getHandler(tradePayQueryMapper, tradeType);
	}
	
	private <T> T getHandler(Map<String, String> mapper, String tradeType) {
		String handlerBeanName = mapper.get(tradeType);
		if (StringUtils.isBlank(handlerBeanName)) {
			throw new CloudApiExcetion("paramError", "参数无效(tradeType)");
		}
		// 获取api请求处理器
		return ApplicationContextHolder.getBean(handlerBeanName);
	}
}
