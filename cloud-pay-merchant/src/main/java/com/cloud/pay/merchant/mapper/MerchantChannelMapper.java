package com.cloud.pay.merchant.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.merchant.entity.MerchantChannel;
import com.cloud.pay.merchant.entity.MerchantChannelKey;

public interface MerchantChannelMapper {
	
    int deleteByPrimaryKey(MerchantChannelKey key);

    int insert(MerchantChannel record);

    MerchantChannel selectByPrimaryKey(MerchantChannelKey key);

    int updateByPrimaryKey(MerchantChannel record);
    
    List<MerchantChannel> selectByMerchantId(@Param("merchantId")Integer merchantId);
    
    
}