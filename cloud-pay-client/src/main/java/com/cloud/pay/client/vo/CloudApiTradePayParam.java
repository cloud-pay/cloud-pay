package com.cloud.pay.client.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cloud.pay.client.vo.base.CloudApiTradeBaseParam;
import com.cloud.pay.common.utils.validation.DateValue;
import com.cloud.pay.common.utils.validation.IntegerValue;

/**
 * 单笔代付请求参数
 * @author THINKPAD
 *
 */
public class CloudApiTradePayParam extends CloudApiTradeBaseParam {

	private static final long serialVersionUID = 497845062222920887L;
  
	@NotEmpty(message = "交易时间不能为空")
	@Length(max = 21,message = "交易时间最长21位")
	@DateValue(format="yyyyMMdd HH:mm:ss")
	private String tradeTime;
	
	@NotNull(message = "交易金额不能为空")
	private BigDecimal tradeAmount;
	
	@NotEmpty(message = "收款人姓名不能为空")
	private String payeeName;
	
	@NotEmpty(message = "收款人银行账号不能为空")
	private String payeeBankCard;
	
	@NotEmpty(message = "收款人联行号不能为空")
	private String payeeBankCode;
	
	@IntegerValue(value= {0,1},message="是否垫资只能为是或者否")
	private Integer loaning;
	
	private String remark;

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeBankCard() {
		return payeeBankCard;
	}

	public void setPayeeBankCard(String payeeBankCard) {
		this.payeeBankCard = payeeBankCard;
	}

	public String getPayeeBankCode() {
		return payeeBankCode;
	}

	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
	}

	public Integer getLoaning() {
		return loaning;
	}

	public void setLoaning(Integer loaning) {
		this.loaning = loaning;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
