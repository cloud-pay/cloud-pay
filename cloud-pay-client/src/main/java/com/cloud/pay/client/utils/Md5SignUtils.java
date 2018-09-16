package com.cloud.pay.client.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 接口验签
 * @author wangy
 */
public class Md5SignUtils {


    private static Logger logger = LoggerFactory.getLogger(Md5SignUtils.class);

    /**
     * 根据json数据生成签名信息
     *
     * @param signKey
     * @param signJsonPlainText
     * @return
     */
    public static String createSignInfo(String signKey, String signJsonPlainText) {
        signJsonPlainText += "&key=" + signKey;
        logger.info("签名-明文:{}", signJsonPlainText);
        String signInfo = SecurityUtils.md5(signJsonPlainText).toUpperCase();
        logger.info("签名-密文:{}", signInfo);
        return signInfo;
    }

    /**
     * 生成签名信息
     *
     * @param signKey
     * @param dataInfo
     * @return
     */
    public static String createSign4Object(String signKey, Object dataInfo, String... filters) {
        String signJsonPlainText = createSignPlainText(dataInfo, filters);
        signJsonPlainText += "&key=" + signKey;
        logger.info("签名-明文:{}", signJsonPlainText);
        String signInfo = SecurityUtils.md5(signJsonPlainText).toUpperCase();
        logger.info("签名-密文:{}", signInfo);
        return signInfo;
    }

    /**
     * 生成签名明文信息
     *
     * @param dataInfo
     * @return
     */
    public static String createSignPlainText(Object dataInfo, String... filters) {
        return createSignPlainText(dataInfo, false, filters);
    }

    /**
     * 生成签名明文信息
     *
     * @param dataInfo
     * @param isWithEmpty 是否包含空
     * @return
     */
    public static String createSignPlainText(final Object dataInfo, final boolean isWithEmpty, final String... filters) {
        if (dataInfo instanceof String) {
            return (String) dataInfo;
        }
        if (dataInfo instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) dataInfo;
            return createMapSignPlainText(dataMap, isWithEmpty, filters);
        }
       
        String signJsonPlainText = JSON.toJSONString(dataInfo);
        JSONObject jsonObject = JSON.parseObject(signJsonPlainText);
        return createMapSignPlainText(jsonObject, isWithEmpty, filters);
    }

    private static String createMapSignPlainText(Map<String, Object> dataMap, final boolean isWithEmpty, final String... filters) {
        final StringBuilder sb = new StringBuilder();
        final Set<String> filterSet = Sets.newHashSet(filters);
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            Object value = entry.getValue();
            if (!isWithEmpty) {
                if (value == null) {
                    continue;
                }
                if (value instanceof String && StringUtils.isBlank((String) value)) {
                    continue;
                }
            }
            if(value instanceof JSONObject){
            	 value = createChildMapSignPlainText((JSONObject)value, isWithEmpty, filters);
            }
            String keyName = entry.getKey();
            if (filterSet.contains(keyName)) {
                continue;
            }
            if ("sign".equalsIgnoreCase(keyName.toLowerCase())) {
                continue;
            }
            list.add(keyName + "=" + entry.getValue());
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
            if (i < size - 1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
   
    /**
     * 对时间的特殊处理
     * @param dataMap
     * @param isWithEmpty
     * @param filters
     * @return
     */
    private static Object createChildMapSignPlainText(Map<String, Object> dataMap, final boolean isWithEmpty, final String... filters){
    	for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
    		Object value = entry.getValue();
    		if(value instanceof Long){
    			entry.setValue(DateUtil.longToDate((Long)value));
    		}
    	}
    	return dataMap;
    }
    
    
}
