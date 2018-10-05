package com.cloud.pay.merchant.mapper;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.merchant.entity.MerchantPrepayInfo;

public interface MerchantPrepayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantPrepayInfo record);

    MerchantPrepayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantPrepayInfo record);

    int updateByPrimaryKey(MerchantPrepayInfo record);
    
    /**
     * 通过商户ID锁定商户预缴户
     * @param merchantId
     * @return
     */
    MerchantPrepayInfo lockByMerchantId(@Param("merchantId")Integer merchantId);
}