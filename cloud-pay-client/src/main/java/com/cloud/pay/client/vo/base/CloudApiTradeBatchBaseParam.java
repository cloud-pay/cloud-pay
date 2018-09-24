package com.cloud.pay.client.vo.base;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 批量交易请求基类
 * @author wangy
 */
public class CloudApiTradeBatchBaseParam extends CloudApiBaseParam {

	private static final long serialVersionUID = -3088007340417432842L;

	@NotEmpty(message = "批次号不能为空")
	@Length(max = 32,message = "批次号最长32位")
	private String batchNo;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	
}
