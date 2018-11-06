package com.cloud.pay.trade.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloud.pay.merchant.entity.MerchantPrepayInfo;

public class ConvertUtil {

	public static Map<Integer, MerchantPrepayInfo> convertMap(List<MerchantPrepayInfo> merchantPrepayInfos) {
		Map<Integer, MerchantPrepayInfo> maps = new HashMap<Integer, MerchantPrepayInfo>();
		for(MerchantPrepayInfo info : merchantPrepayInfos) {
			maps.put(info.getMerchantId(), info);
		}
		return maps;
	}
}
