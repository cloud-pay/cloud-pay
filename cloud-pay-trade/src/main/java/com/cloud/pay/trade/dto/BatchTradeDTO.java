package com.cloud.pay.trade.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BatchTradeDTO {
    private Integer id;

    private String batchNo;

    private Date tradeTime;

    private BigDecimal totalAmount;

    private Integer totalCount;

    private Integer payerMerchantId;

    private Integer status;

    private String creator;

    private Date createTime;

    private String auditor;

    private Date auditTime;

    private Integer type;

    private String name;
    
    private String platBatchNo;
    
    private Integer tradeStatus;
    
    private String fileName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
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

	public Integer getPayerMerchantId() {
		return payerMerchantId;
	}

	public void setPayerMerchantId(Integer payerMerchantId) {
		this.payerMerchantId = payerMerchantId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlatBatchNo() {
		return platBatchNo;
	}

	public void setPlatBatchNo(String platBatchNo) {
		this.platBatchNo = platBatchNo;
	}

	public Integer getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Integer tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BatchTradeDTO [id=");
		builder.append(id);
		builder.append(", batchNo=");
		builder.append(batchNo);
		builder.append(", tradeTime=");
		builder.append(tradeTime);
		builder.append(", totalAmount=");
		builder.append(totalAmount);
		builder.append(", totalCount=");
		builder.append(totalCount);
		builder.append(", payerMerchantId=");
		builder.append(payerMerchantId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", creator=");
		builder.append(creator);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", auditor=");
		builder.append(auditor);
		builder.append(", auditTime=");
		builder.append(auditTime);
		builder.append(", type=");
		builder.append(type);
		builder.append(", name=");
		builder.append(name);
		builder.append(", platBatchNo=");
		builder.append(platBatchNo);
		builder.append(", tradeStatus=");
		builder.append(tradeStatus);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append("]");
		return builder.toString();
	}
    
}