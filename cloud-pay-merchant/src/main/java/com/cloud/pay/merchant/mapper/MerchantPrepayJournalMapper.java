package com.cloud.pay.merchant.mapper;

import com.cloud.pay.merchant.entity.MerchantPrepayJournal;

public interface MerchantPrepayJournalMapper {
    int insert(MerchantPrepayJournal record);

    MerchantPrepayJournal selectByPrimaryKey(Integer id);

}