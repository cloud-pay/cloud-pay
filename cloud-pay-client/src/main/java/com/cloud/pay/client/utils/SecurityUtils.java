package com.cloud.pay.client.utils;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang3.StringUtils;


/**
 * 加签工具类
 * @author wangy
 */
public class SecurityUtils {

    /**
     * 对明文进行md5
     *
     * @param plainText
     * @return
     */
    public static String md5(String plainText) {
        return md5(plainText, "utf-8");
    }

    /**
     * 对明文进行md5
     *
     * @param plainText
     * @param charset
     * @return
     */
    public static String md5(String plainText, String charset) {
        charset = StringUtils.defaultString(charset , "utf-8");
        try {
            return Encodes.encodeHex(Digests.md5(new ByteArrayInputStream(plainText.getBytes(charset))));
        } catch (Exception ex) {
            throw new RuntimeException("md5错误", ex);
        }
    }

}
