package com.cloud.pay.merchant.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.merchant.dto.MerchantPrepayJournalDTO;
import com.cloud.pay.merchant.entity.MerchantPrepayJournal;

public interface MerchantPrepayJournalMapper {
	int insert(MerchantPrepayJournal record);

	MerchantPrepayJournal selectByPrimaryKey(Integer id);

	public List<MerchantPrepayJournalDTO> selectList(@Param("merchantId") Integer merchantId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime, 
			@Param("userMerchantId")Integer userMerchantId, 
			@Param("userMerchantType")String userMerchantType);

}