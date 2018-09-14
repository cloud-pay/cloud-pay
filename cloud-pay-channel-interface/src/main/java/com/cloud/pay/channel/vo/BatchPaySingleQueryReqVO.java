package com.cloud.pay.channel.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 批量代付单笔结果查询
 * @author 
 */
public class BatchPaySingleQueryReqVO extends BaseTradeReqVO {

	private static final long serialVersionUID = 3997694702340235174L;
   
	@NotBlank(message = "交易日期不能为空")
	@Length(max = 17,message = "交易日期最长17位")
	private String tradeDate; //交易时间
	
	@NotBlank(message = "批量请求文件名不能为空")
	@Length(max = 32,message = "批量请求文件名最长32位")
	private String fileName; //批量请求文件名
	
	@NotBlank(message = "批量流水号不能为空")
	@Length(max = 32,message = "批量流水号最长32位")
	private String batchOrderNo; //批量流水号
	
	@NotBlank(message = "单笔交易序号不能为空")
	@Length(max = 32,message = "单笔交易序号最长32位")
	private String seqNo;//单笔交易在批量代付文件里得序号

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

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	
}
