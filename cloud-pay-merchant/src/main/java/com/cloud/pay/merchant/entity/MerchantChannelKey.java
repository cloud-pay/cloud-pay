package com.cloud.pay.merchant.entity;

public class MerchantChannelKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_channel.merchant_id
     *
     * @mbggenerated Mon Sep 10 14:21:18 CST 2018
     */
    private Integer merchantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_merchant_channel.channel_id
     *
     * @mbggenerated Mon Sep 10 14:21:18 CST 2018
     */
    private Integer channelId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_channel.merchant_id
     *
     * @return the value of t_merchant_channel.merchant_id
     *
     * @mbggenerated Mon Sep 10 14:21:18 CST 2018
     */
    public Integer getMerchantId() {
        return merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_channel.merchant_id
     *
     * @param merchantId the value for t_merchant_channel.merchant_id
     *
     * @mbggenerated Mon Sep 10 14:21:18 CST 2018
     */
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_merchant_channel.channel_id
     *
     * @return the value of t_merchant_channel.channel_id
     *
     * @mbggenerated Mon Sep 10 14:21:18 CST 2018
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_merchant_channel.channel_id
     *
     * @param channelId the value for t_merchant_channel.channel_id
     *
     * @mbggenerated Mon Sep 10 14:21:18 CST 2018
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
}