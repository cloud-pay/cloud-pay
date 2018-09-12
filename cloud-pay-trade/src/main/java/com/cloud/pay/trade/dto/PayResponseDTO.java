package com.cloud.pay.trade.dto;

import java.io.Serializable;

/**
 * 交易响应DTO
 * 
 * @author kftpay-core
 *
 */
public class PayResponseDTO implements Serializable {

	private static final long serialVersionUID = -8783802253267903564L;

	private String orderNo;

	private Integer status;

	private String returnCode;

	private String returnInfo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PayResponseDTO [orderNo=");
		builder.append(orderNo);
		builder.append(", status=");
		builder.append(status);
		builder.append(", returnCode=");
		builder.append(returnCode);
		builder.append(", returnInfo=");
		builder.append(returnInfo);
		builder.append("]");
		return builder.toString();
	}

}
