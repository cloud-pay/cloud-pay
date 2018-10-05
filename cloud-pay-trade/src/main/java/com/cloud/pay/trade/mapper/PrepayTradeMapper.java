package com.cloud.pay.trade.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.trade.dto.PrepayTradeDTO;
import com.cloud.pay.trade.entity.PrepayTrade;

public interface PrepayTradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PrepayTrade record);

    int insertSelective(PrepayTrade record);

    PrepayTrade selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PrepayTrade record);

    int updateByPrimaryKey(PrepayTrade record);
    
    /**
     * 預繳戶交易列表查询
     * @param merchantId
     * @param startTime
     * @param endTime
     * @return
     */
	List<PrepayTradeDTO> selectTradeList(@Param("merchantId") Integer merchantId, 
			@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}