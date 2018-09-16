package com.cloud.pay.client.handler;

import com.cloud.pay.client.vo.base.CloudApiBaseResult;

/**
 * 对外业务接口接口类
 * @author 
 *
 * @param <T> 请求参数
 * @param <R> 响应结果
 */
public interface ICloudPayApiHandler <T,R extends CloudApiBaseResult>{

	/**
	 * 处理请求
	 * @param reqParam
	 * @return
	 */
	R handle(T reqParam);
	
	/**
	 * 获取请求参数实体类
	 * @return
	 */
	Class<T> getReqParamType();
}
