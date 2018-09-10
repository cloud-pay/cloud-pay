package com.cloud.pay.trade.mapper;

import com.cloud.pay.trade.entity.BatchTrade;

public interface BatchTradeMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(BatchTrade record);

    int insertSelective(BatchTrade record);

    BatchTrade selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BatchTrade record);
}