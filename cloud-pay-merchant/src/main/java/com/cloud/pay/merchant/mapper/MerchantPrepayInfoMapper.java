package com.cloud.pay.merchant.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.merchant.dto.MerchantPrepayInfoDTO;
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
    
    /**
     * 资金账户列表查询
     * @param merchantId
     * @param startTime
     * @param endTime
     * @param type org/merchant，商户类型
     * @return
     */
    List<MerchantPrepayInfoDTO> selectList(@Param("merchantId") Integer merchantId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") String type, @Param("userMerchantId")Integer userMerchantId, 
			@Param("userMerchantType")String userMerchantType);
    
    /**
     * 通过商户ID列表锁定商户预缴户
     * @param merchantId
     * @return
     */
    List<MerchantPrepayInfo> lockByMerchantIds(@Param("merchantIds")List<Integer> merchantIds);
}