package com.cloud.pay.client.vo;

import com.cloud.pay.client.vo.base.CloudApiBaseResult;

/**
 * 获取对账文件响应结果
 * @author THINKPAD
 *
 */
public class CloudApiReconFileResult extends CloudApiBaseResult {

	private static final long serialVersionUID = -3226838865265578066L;

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
