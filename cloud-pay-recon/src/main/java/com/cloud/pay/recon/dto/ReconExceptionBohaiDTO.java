package com.cloud.pay.recon.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReconExceptionBohaiDTO {

    private Integer id;

    private String orderNo;

    private String exceptionReason;

    private Date createTime;

    
    public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getExceptionReason() {
		return exceptionReason;
	}


	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
