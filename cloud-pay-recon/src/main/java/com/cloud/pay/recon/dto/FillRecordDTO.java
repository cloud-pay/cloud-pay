package com.cloud.pay.recon.dto;

import java.math.BigDecimal;
import java.util.Date;

public class FillRecordDTO {

    private Integer id;

    private Integer orgId;

    private BigDecimal fillAmount;

    private String rmk;

    private Integer status;

    private Integer updatorId;
    
    private String orgCode;
    
    private String orgName;

    private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public BigDecimal getFillAmount() {
		return fillAmount;
	}

	public void setFillAmount(BigDecimal fillAmount) {
		this.fillAmount = fillAmount;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUpdatorId() {
		return updatorId;
	}

	public void setUpdatorId(Integer updatorId) {
		this.updatorId = updatorId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
