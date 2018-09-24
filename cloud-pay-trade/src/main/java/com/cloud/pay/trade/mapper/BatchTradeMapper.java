package com.cloud.pay.trade.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.trade.dto.BatchTradeDTO;
import com.cloud.pay.trade.entity.BatchTrade;

public interface BatchTradeMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(BatchTrade record);

    int insertSelective(BatchTrade record);

    BatchTrade selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BatchTrade record);
    
    int audit(BatchTrade record);
    
    List<BatchTradeDTO> getBatchTradeList(@Param("status") Integer status,
			@Param("startTime") Date startTime,
			@Param("endTime") Date endTime);
    
    /**
      * 根据批次号查询批次信息
     * @param batchNo
     * @param merchantId
     * @return
     */
    BatchTradeDTO queryBatchByBatchNo(@Param("BatchNo")String batchNo,@Param("merchantId")Integer merchantId);
}