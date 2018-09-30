package com.cloud.pay.merchant.mapper;

import com.cloud.pay.merchant.entity.MerchantPrepayInfo;

public interface MerchantPrepayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantPrepayInfo record);

    MerchantPrepayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantPrepayInfo record);

    int updateByPrimaryKey(MerchantPrepayInfo record);
}