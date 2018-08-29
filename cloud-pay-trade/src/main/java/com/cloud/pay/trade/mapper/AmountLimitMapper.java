package com.cloud.pay.trade.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.trade.entity.AmountLimit;

public interface AmountLimitMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(AmountLimit record);

	int insertSelective(AmountLimit record);

	AmountLimit selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(AmountLimit record);

	int updateByPrimaryKey(AmountLimit record);

	public List<AmountLimit> getAmountLimitList(@Param("type") Integer type, @Param("orgName") String orgName,
			@Param("merchantName") String merchantName, @Param("startTime") Date startTime,
			@Param("endTime") Date endTime);
}