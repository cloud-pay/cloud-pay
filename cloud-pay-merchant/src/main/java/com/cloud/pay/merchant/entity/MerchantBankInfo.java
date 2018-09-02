package com.cloud.pay.merchant.entity;

public class MerchantBankInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.merchant_id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private Integer merchantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.bank_name
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private String bankName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.bank_id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private Integer bankId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.bank_card_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private String bankCardNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.bank_account_type
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private Integer bankAccountType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.bank_account_name
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private String bankAccountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.cert_type
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private Integer certType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.cert_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private String certNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_bank_info.mobile_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    private String mobileNo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.id
     *
     * @return the value of t_merchant_bank_info.id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.id
     *
     * @param id the value for t_merchant_bank_info.id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.merchant_id
     *
     * @return the value of t_merchant_bank_info.merchant_id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public Integer getMerchantId() {
        return merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.merchant_id
     *
     * @param merchantId the value for t_merchant_bank_info.merchant_id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.bank_name
     *
     * @return the value of t_merchant_bank_info.bank_name
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.bank_name
     *
     * @param bankName the value for t_merchant_bank_info.bank_name
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.bank_id
     *
     * @return the value of t_merchant_bank_info.bank_id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public Integer getBankId() {
        return bankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.bank_id
     *
     * @param bankId the value for t_merchant_bank_info.bank_id
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.bank_card_no
     *
     * @return the value of t_merchant_bank_info.bank_card_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public String getBankCardNo() {
        return bankCardNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.bank_card_no
     *
     * @param bankCardNo the value for t_merchant_bank_info.bank_card_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.bank_account_type
     *
     * @return the value of t_merchant_bank_info.bank_account_type
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public Integer getBankAccountType() {
        return bankAccountType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.bank_account_type
     *
     * @param bankAccountType the value for t_merchant_bank_info.bank_account_type
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setBankAccountType(Integer bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.bank_account_name
     *
     * @return the value of t_merchant_bank_info.bank_account_name
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public String getBankAccountName() {
        return bankAccountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.bank_account_name
     *
     * @param bankAccountName the value for t_merchant_bank_info.bank_account_name
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName == null ? null : bankAccountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.cert_type
     *
     * @return the value of t_merchant_bank_info.cert_type
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public Integer getCertType() {
        return certType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.cert_type
     *
     * @param certType the value for t_merchant_bank_info.cert_type
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setCertType(Integer certType) {
        this.certType = certType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.cert_no
     *
     * @return the value of t_merchant_bank_info.cert_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public String getCertNo() {
        return certNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.cert_no
     *
     * @param certNo the value for t_merchant_bank_info.cert_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setCertNo(String certNo) {
        this.certNo = certNo == null ? null : certNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_bank_info.mobile_no
     *
     * @return the value of t_merchant_bank_info.mobile_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_bank_info.mobile_no
     *
     * @param mobileNo the value for t_merchant_bank_info.mobile_no
     *
     * @mbggenerated Sun Sep 02 09:06:42 CST 2018
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }
}