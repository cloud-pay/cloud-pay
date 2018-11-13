package com.cloud.pay.trade.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloud.pay.channel.dto.TradeDTO;
import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.util.MD5;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;

public class ConvertUtil {
	
	private static Logger log = LoggerFactory.getLogger(ConvertUtil.class);

	public static Map<Integer, MerchantPrepayInfo> convertMap(List<MerchantPrepayInfo> merchantPrepayInfos) throws Exception {
		Map<Integer, MerchantPrepayInfo> maps = new HashMap<Integer, MerchantPrepayInfo>();
		for(MerchantPrepayInfo prepayInfo : merchantPrepayInfos) {
			log.info("垫资机构预缴户信息:{}", prepayInfo);
			String orgDigest = MD5.md5(String.valueOf(prepayInfo.getBalance()) + "|" + prepayInfo.getFreezeAmount(),
					String.valueOf(prepayInfo.getMerchantId()));
			if (!orgDigest.equals(prepayInfo.getDigest())) {
				log.info("{}预缴户被篡改", prepayInfo.getMerchantId());
				throw new TradeException("预缴户被篡改", TradeConstant.PREPAY_CHANGE);
			}
			maps.put(prepayInfo.getMerchantId(), prepayInfo);
		}
		return maps;
	}
	
	/**
	 * list转map
	 * @param trades
	 * @return
	 */
	public static Map<String, Trade> convertTradeMap(List<Trade> trades) {
		Map<String, Trade> maps = new HashMap<String, Trade>();
		for(Trade trade : trades) {
			maps.put(trade.getOrderNo(), trade);
		}
		return maps;
	}
	
	public static Map<String, TradeDTO> convertTradeDTOMap(List<TradeDTO> trades) {
		Map<String, TradeDTO> maps = new HashMap<String, TradeDTO>();
		for(TradeDTO trade : trades) {
			maps.put(trade.getOrderNo(), trade);
		}
		return maps;
	}
	
}
