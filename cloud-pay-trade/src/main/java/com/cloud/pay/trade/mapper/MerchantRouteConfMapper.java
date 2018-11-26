package com.cloud.pay.trade.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.trade.dto.MerchantRouteDTO;
import com.cloud.pay.trade.entity.MerchantRouteConf;

public interface MerchantRouteConfMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(MerchantRouteConf record);

	int insertSelective(MerchantRouteConf record);

	MerchantRouteConf selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(MerchantRouteConf record);

	int updateByPrimaryKey(MerchantRouteConf record);

	public List<MerchantRouteDTO> getMerchantRouteConfList(@Param("type") Integer type, @Param("status") Integer status,
			@Param("merchantName") String merchantName, @Param("startTime") Date startTime,
			@Param("endTime") Date endTime, @Param("userMerchantId")Integer userMerchantId, 
			@Param("userMerchantType")String userMerchantType);
	
	/**
	 * 通过商户ID。渠道ID查询路由信息
	 * @author dbnaxlc
	 * @date 2018年11月6日 下午2:54:29
	 * @param merchantId
	 * @param channelId
	 * @return
	 */
	MerchantRouteConf selectByMerchantIdAndChannelId(@Param("merchantId") Integer merchantId,
			@Param("channelId") Integer channelId);
}