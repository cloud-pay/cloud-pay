package com.cloud.pay.trade.mapper;

import com.cloud.pay.trade.entity.BatchTradeDetail;

public interface BatchTradeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BatchTradeDetail record);

    int insertSelective(BatchTradeDetail record);

    BatchTradeDetail selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(BatchTradeDetail record);

    int updateByPrimaryKey(BatchTradeDetail record);
}