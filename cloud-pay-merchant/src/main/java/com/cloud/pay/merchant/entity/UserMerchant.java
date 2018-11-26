package com.cloud.pay.merchant.entity;

public class UserMerchant {

	private int userId;
	
	private int merchantId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserMerchant [userId=");
		builder.append(userId);
		builder.append(", merchantId=");
		builder.append(merchantId);
		builder.append("]");
		return builder.toString();
	}
	
}
