package com.cloud.pay.trade.mapper;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.trade.entity.PaySms;

public interface PaySmsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaySms record);

    int insertSelective(PaySms record);

    PaySms selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaySms record);

    int updateByPrimaryKey(PaySms record);
    
    PaySms selectByBatchNo(@Param("batchNo") String batchNo);
    
    int updateVerifyResult(PaySms record);
}