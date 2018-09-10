package com.cloud.pay.merchant.mapper;

import com.cloud.pay.merchant.entity.MerchantSecret;

public interface MerchantSecretMapper {

    int deleteByPrimaryKey(Integer merchantId);

    int insert(MerchantSecret record);
    
    MerchantSecret selectByPrimaryKey(Integer merchantId);

    int updateByPrimaryKeyWithBLOBs(MerchantSecret record);
}