package com.cloud.pay.merchant.mapper;

import com.cloud.pay.merchant.entity.MerchantApplyAttachementInfo;

public interface MerchantApplyAttachementInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantApplyAttachementInfo record);

    int insertSelective(MerchantApplyAttachementInfo record);

    MerchantApplyAttachementInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantApplyAttachementInfo record);

    int updateByPrimaryKey(MerchantApplyAttachementInfo record);
}