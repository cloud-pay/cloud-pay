package com.cloud.pay.client.vo.base;

/**
 * 交易响应参数基类
 * @author THINKPAD
 *
 */
public class CloudApiTradeBaseResult extends CloudApiBaseResult {

	private static final long serialVersionUID = -2481701953297258185L;
	
	
	//平台订单号
	private String platOrderNo;

    //交易流水
    private String orderNo;
    
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPlatOrderNo() {
		return platOrderNo;
	}

	public void setPlatOrderNo(String platOrderNo) {
		this.platOrderNo = platOrderNo;
	}
	
	
}
