package com.cloud.pay.trade.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PayHandler {

	/**
	 * 保存单笔代付
	 * @author 夏志强
	 * @date 2018年9月12日 下午5:23:06
	 */
	@Transactional
	public void singlePay() {
		
	}
	
	/**
	 * 调用渠道单笔扣款
	 * @author 夏志强
	 * @date 2018年9月12日 下午5:22:55
	 */
	public void invokePay() {
		
	}
	
	/**
	 * 调用渠道返回后修改交易状态
	 * @author 夏志强
	 * @date 2018年9月12日 下午5:22:38
	 */
	@Transactional
	public void updateTradeStatus() {
		
	}
}
