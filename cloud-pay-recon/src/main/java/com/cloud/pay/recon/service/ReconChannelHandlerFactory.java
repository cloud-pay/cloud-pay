package com.cloud.pay.recon.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.cloud.pay.common.contants.ChannelType;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.common.utils.ApplicationContextHolder;

/**
 *  对账渠道工厂类
 * @author wangy
 */
@Component
public class ReconChannelHandlerFactory {
   
	private final Map<String,String> reconMapper = new HashMap<>();
    
	@PostConstruct
	public void init() {
		//渤海银行
		reconMapper.put(ChannelType.BOHAI.getChannelENName(), "bohaiReconService");
	}
	
	
	public IReconServiceHandler getHandler(String channelName) {
		String handlerBeanName = reconMapper.get(channelName);
		if (StringUtils.isBlank(handlerBeanName)) {
			throw new CloudPayException("渠道不存在");
		}
		// 获取api请求处理器
		return ApplicationContextHolder.getBean(handlerBeanName);
	}
}
