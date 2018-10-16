package com.cloud.pay.trade.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PrepayTrade {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_prepay_trade.id
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_prepay_trade.merchant_id
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    private Integer merchantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_prepay_trade.trade_type
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    private Integer tradeType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_prepay_trade.trade_time
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    private Date tradeTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_prepay_trade.amount
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    private BigDecimal amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_prepay_trade.remark
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    private String remark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_prepay_trade.id
     *
     * @return the value of t_prepay_trade.id
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_prepay_trade.id
     *
     * @param id the value for t_prepay_trade.id
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_prepay_trade.merchant_id
     *
     * @return the value of t_prepay_trade.merchant_id
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public Integer getMerchantId() {
        return merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_prepay_trade.merchant_id
     *
     * @param merchantId the value for t_prepay_trade.merchant_id
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_prepay_trade.trade_type
     *
     * @return the value of t_prepay_trade.trade_type
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public Integer getTradeType() {
        return tradeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_prepay_trade.trade_type
     *
     * @param tradeType the value for t_prepay_trade.trade_type
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_prepay_trade.trade_time
     *
     * @return the value of t_prepay_trade.trade_time
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public Date getTradeTime() {
        return tradeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_prepay_trade.trade_time
     *
     * @param tradeTime the value for t_prepay_trade.trade_time
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_prepay_trade.amount
     *
     * @return the value of t_prepay_trade.amount
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_prepay_trade.amount
     *
     * @param amount the value for t_prepay_trade.amount
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_prepay_trade.remark
     *
     * @return the value of t_prepay_trade.remark
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_prepay_trade.remark
     *
     * @param remark the value for t_prepay_trade.remark
     *
     * @mbggenerated Fri Oct 05 11:49:59 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    
    private Integer status;
    
    private String creator;

    private Date createTime;
    
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
    
}