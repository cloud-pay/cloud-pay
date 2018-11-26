package com.cloud.pay.merchant.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.merchant.dto.MerchantPrepayJournalDTO;
import com.cloud.pay.merchant.mapper.MerchantPrepayJournalMapper;

@Service
public class MerchantPrepayJournalService {

	@Autowired
	private MerchantPrepayJournalMapper merchantPrepayJournalMapper;

	/**
	 * 预缴户资金流水查询
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<MerchantPrepayJournalDTO> selectList(Integer merchantId, Date startTime, Date endTime
			, Integer userMerchantId, String userMerchantType) {
		return merchantPrepayJournalMapper.selectList(merchantId, startTime, endTime, userMerchantId, userMerchantType);
	}

}
