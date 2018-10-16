package com.cloud.pay.merchant.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.merchant.entity.MerchantApplyAttachementInfo;

public interface MerchantApplyAttachementInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantApplyAttachementInfo record);

    int insertSelective(MerchantApplyAttachementInfo record);

    MerchantApplyAttachementInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantApplyAttachementInfo record);

    int updateByPrimaryKey(MerchantApplyAttachementInfo record);
    
    List<MerchantApplyAttachementInfo> selectByMerchantId(@Param("merchantId")Integer merchantId);
    
    int updateByMerchantIdAndType(@Param("merchantId")Integer merchantId, @Param("type")Integer type,
    		@Param("path")String path);
}