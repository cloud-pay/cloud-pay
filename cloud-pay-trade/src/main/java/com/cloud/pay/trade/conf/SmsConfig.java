package com.cloud.pay.trade.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath*:sms.properties")
public class SmsConfig {

	@Value("${accessKeyId}")
	private String accessKeyId;

	@Value("${accessKeySecret}")
	private String accessKeySecret;

	@Value("${signName}")
	private String signName;

	@Value("${verifyMaxTimes}")
	private int verifyMaxTimes;

	@Value("${expiryTime}")
	private int expiryTime;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public int getVerifyMaxTimes() {
		return verifyMaxTimes;
	}

	public void setVerifyMaxTimes(int verifyMaxTimes) {
		this.verifyMaxTimes = verifyMaxTimes;
	}

	public int getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(int expiryTime) {
		this.expiryTime = expiryTime;
	}

}
