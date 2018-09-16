package com.cloud.pay.channel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.common.contants.ChannelType;
import com.cloud.pay.common.exception.CloudApiException;
import com.cloud.pay.common.utils.ApplicationContextHolder;

/**
 * 渠道接口简单工厂
 * @author wangy
 */
@Component
public class TradePayTypeHandlerFactory {
   
	private final Map<String,String> tradePayMapper = new HashMap<>(); //代付通道执行类
	private final Map<String,String> tradePayQueryMapper = new HashMap<>(); //代付结果查询通道执行类
	private final Map<String,String> tradeUnionPayMapper = new HashMap<>(); //银行银联代付通道
	private final Map<String,String> tradeDownReconFileMapper = new HashMap<>();//下载对账文件 
	private final Map<String,String> batchTradePayMapper = new HashMap<>();//批量代付
	private final Map<String,String> batchTradePayQueryMapper = new HashMap<>(); //批量代付结果查询
	private final Map<String,String> batchSingleQueryMapper = new HashMap<>();
	private final Map<String,String> batchPayRetryMapper = new HashMap<>();
    
	/**
	 * 启动的时候将渠道接口信息直接加载到内存
	 * 如果多多个渠道则直接新增mapper的key-value即可
	 * 示例，加入新增民生代付通道，新增民生代付接口，则在tradePayMapper这个map里面新增key-value
	 * tradePayMapper.put("smms_trade_pay", "smmsTradePayExecutor");
	 */
	@PostConstruct
	public void init() {
		//渤海银行
		tradePayMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiTradePayExecutor");
		tradePayQueryMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiTradeQueryExecutor");
		tradeUnionPayMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiUnionTradePayExecutor");
		tradeDownReconFileMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiDownReconFileExecutor");
		batchTradePayMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiBatchTradePayExecutor");
		batchTradePayQueryMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiBatchTradePayQueryExecutor");
		batchSingleQueryMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiBatchSingleQueryExecutor");
		batchPayRetryMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiBatchPayRetryExecutor");
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
	
	/**
	 * 根据交易类型获取银联代付渠道执行类
	 * @param tradeType
	 * @return
	 */
	public ITradePayExecutor getTradeUnionPayHandler(String tradeType) {
		  return getHandler(tradeUnionPayMapper,tradeType);
	}
	
	/**
	 * 获取对账文件下载执行类
	 * @param tradeType
	 * @return
	 */
	public ITradePayExecutor getTradeDownReconFileHandler(String tradeType) {
		  return getHandler(tradeDownReconFileMapper,tradeType);
	}
	
	/**
	 * 批量代付
	 * @param tradeType
	 * @return
	 */
	public ITradePayExecutor getBatchTraeHandler(String tradeType) {
		 return getHandler(batchTradePayMapper,tradeType);
	}
	
	/**
	 * 批量代付结果查询
	 * @param tradeType
	 * @return
	 */
	public ITradePayExecutor getBatchTradeQueryHandler(String tradeType) {
		 return getHandler(batchTradePayQueryMapper,tradeType);
	}
	
	/**
	 * 批量代付单笔查询
	 * @param tradeType
	 * @return
	 */
    public ITradePayExecutor getBatchSingleQueryHandler(String tradeType) {
    	 return getHandler(batchSingleQueryMapper,tradeType);
    }
    
    /**
       *  批量代付重发
     * @param tradeType
     * @return
     */
    public ITradePayExecutor getBatchPayRetryHandler(String tradeType) {
    	 return getHandler(batchPayRetryMapper,tradeType);
    }
	
	private <T> T getHandler(Map<String, String> mapper, String tradeType) {
		String handlerBeanName = mapper.get(tradeType);
		if (StringUtils.isBlank(handlerBeanName)) {
			throw new CloudApiException("paramError", "参数无效(tradeType)");
		}
		// 获取api请求处理器
		return ApplicationContextHolder.getBean(handlerBeanName);
	}
}
