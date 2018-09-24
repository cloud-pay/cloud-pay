package com.cloud.pay.client.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cloud.pay.client.vo.base.CloudApiTradeBatchBaseParam;

/**
 * 批量代付请求参数
 * @author THINKPAD
 *
 */
public class CloudApiTradeBatchPayParam extends CloudApiTradeBatchBaseParam {

	private static final long serialVersionUID = 4937253442660380787L;
	
	@NotEmpty(message = "交易时间不能为空")
	@Length(max = 21,message = "交易时间最长21位")
	private String tradeTime;
	
	@NotNull(message = "总交易金额不能为空")
	private BigDecimal totalAmount;
	
	@NotNull(message = "总交易条数不能为空")
	private Integer totalCount;
	
	@NotEmpty(message = "文件名不能为空")
	private String  fileName;

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
