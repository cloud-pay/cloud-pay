package com.cloud.pay.merchant.mapper;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.merchant.entity.UserMerchant;

public interface UserMerchantMapper {

    int deleteByUserId(@Param("userId")Integer userId);

    int insert(UserMerchant record);
    
    UserMerchant selectByUserId(@Param("userId")Integer userId);

    
}