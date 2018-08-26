package com.cloud.pay.channel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.cloud.pay.channel.service.ITradePayExecutor;
import com.cloud.pay.common.exception.CloudApiExcetion;
import com.cloud.pay.common.utils.ApplicationContextHolder;

/**
 * 渠道接口简单工厂
 * @author wangy
 */
@Component
public class TradePayTypeHandlerFactory {
   
	private final Map<String,String> tradePayMapper = new HashMap<>();
	private final Map<String,String> tradePayQueryMapper = new HashMap<>();
    
	/**
	 * 启动的时候将渠道接口信息直接加载到内存
	 */
	@PostConstruct
	public void init() {
		tradePayMapper.put("bohai_trade_pay", "bohaiTradePayExecutor");
	}
	
	public ITradePayExecutor getTradePayHandler(String tradeType) {
		 return getHandler(tradePayMapper, tradeType);
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
