package com.cloud.pay.channel.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.cloud.pay.common.utils.validation.DateValue;

/**
 * 批量代付结果查询
 * @author 
 */
public class BatchPayTradeQueryReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = 5530254610348810005L;
   
	@NotBlank(message = "交易日期不能为空")
	@Length(max = 17,message = "交易日期最长32位")
	@DateValue(format="yyyyMMdd HH:mm:ss")
	private String tradeDate;
	
	@NotBlank(message = "文件名不能为空")
	@Length(max = 32,message = "文件名最长32位")
	private String fileName; 
	
	@NotBlank(message = "批量交易流水不能为空")
	@Length(max = 32,message = "批量交易流水最长32位")
	private String batchOrderNo;
	
	@NotNull(message = "渠道ID不能为空")
	private Integer channelId;

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBatchOrderNo() {
		return batchOrderNo;
	}

	public void setBatchOrderNo(String batchOrderNo) {
		this.batchOrderNo = batchOrderNo;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	
	
}

