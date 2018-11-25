package com.cloud.pay.client.vo;

import com.cloud.pay.client.vo.base.CloudApiTradeBaseResult;

/**
 * 代笔代付响应结果
 * @author THINKPAD
 *
 */
public class CloudApiTradePayResult extends CloudApiTradeBaseResult {
	
	private static final long serialVersionUID = 6122098494486986L;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
