package com.cloud.pay.trade.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.trade.dto.BatchTradeDTO;
import com.cloud.pay.trade.entity.BatchTrade;

public interface BatchTradeMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(BatchTrade record);

    BatchTrade selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BatchTrade record);
    
    int updateByPrimaryKeySelective(BatchTrade record);
    
    int audit(BatchTrade record);
    
    List<BatchTradeDTO> getBatchTradeList(@Param("status") Integer status,
			@Param("startTime") Date startTime,
			@Param("endTime") Date endTime,
			@Param("userMerchantId")Integer userMerchantId,
			@Param("userMerchantType")String userMerchantType);
    
    /**
      * 根据批次号查询批次信息
     * @param batchNo
     * @param merchantId
     * @return
     */
    BatchTradeDTO queryBatchByBatchNo(@Param("batchNo")String batchNo,@Param("merchantId")Integer merchantId);
    
    /**
     * 通过批次号修改交易状态
     * @param status
     * @param batchNo
     * @return
     */
    int updateTradeStatus(@Param("tradeStatus") Integer tradeStatus, @Param("batchNo")String batchNo);
}