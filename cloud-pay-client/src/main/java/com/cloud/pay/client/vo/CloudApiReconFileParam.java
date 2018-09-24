package com.cloud.pay.client.vo;

import com.cloud.pay.client.vo.base.CloudApiBaseParam;

/**
 * 获取对账文件请求参数
 * @author THINKPAD
 *
 */
public class CloudApiReconFileParam extends CloudApiBaseParam {

	private static final long serialVersionUID = -2082463124223757170L;

	private String reconDate;

	public String getReconDate() {
		return reconDate;
	}

	public void setReconDate(String reconDate) {
		this.reconDate = reconDate;
	}
	
}
