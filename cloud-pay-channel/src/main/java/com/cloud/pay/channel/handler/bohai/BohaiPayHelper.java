package com.cloud.pay.channel.handler.bohai;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.cloud.pay.channel.utils.BeanUtils;
import com.cloud.pay.channel.utils.DocumentUtils;
import com.cloud.pay.channel.utils.XmlSignature;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;

/**
 * 渤海银行模板类
 * @author wangy
 */
@Component
public class BohaiPayHelper<M extends BohaiCloudTradeParam>{
	 
	protected Logger log = LoggerFactory.getLogger(getClass());
     
	@Value("${cloud.bohai.pay.private.key.path}")
	private String priKeyPath;
	
	@Value("${cloud.bohai.pay.public.key.path}")
	private String pubKeyPath;
	
	@Value("${cloud.bohai.pay.private.key.password}")
	private String priKeyPwd;
	
	private final String charset = "utf-8";
	
	
	
	public BohaiCloudTradeResult request(M param,String reqName) {
		log.info("代付-渤海代付-请求参数：{}",param);
		try {
			Map<String, Object> map = BeanUtils.object2Map(param);
			String xml = toXml(map,param.getSerialNo(),reqName);
			log.info("代付-渤海代付-请求参数转为xml:{}",xml);
			Document doc = DocumentUtils.stringToDoc(xml);
			PrivateKey privateKey = XmlSignature.getPrivateKey(priKeyPath, priKeyPwd, "root");
			String xmlStr = XmlSignature.generateXMLDigitalSignature1(doc, "", privateKey, priKeyPwd);
			
			
		    log.info("代付-渤海代付-请求参数加密后:{}",xmlStr);
		    boolean verify = XmlSignature.validateXMLDigitalSignature(xmlStr, pubKeyPath);
		    log.info("代付-渤海代付-响应结果验证：{}",verify);
		}catch(Exception e) {
			log.error("请求渤海代付异常",e);
		}
		return null;
	}
	
	/**
	 * 将请求参数转换为xml数据
	 * @param params
	 * @param orderNo
	 * @return
	 */
	public static String toXml(Map<String, Object> params,String orderNo,String reqName) {
		StringBuilder buf = new StringBuilder();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		buf.append("<Message id='"+orderNo+"'>");
		buf.append("<"+reqName+">");
		for (String key : keys) {
			if("serialVersionUID".equals(key)) {
				continue;
			}
			if(null == params.get(key)) {
				continue;
			}
			buf.append("<").append(key).append(">");
			buf.append(params.get(key));
			buf.append("</").append(key).append(">\n");
		}
		buf.append("</"+reqName+">");
		buf.append("</Message>");
		return buf.toString();
	}

	
}
