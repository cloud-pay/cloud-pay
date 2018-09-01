package com.cloud.pay.channel.handler.bohai;

import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.cloud.pay.channel.handler.ITradePayExecutor;
import com.cloud.pay.channel.utils.ChannelBeanUtils;
import com.cloud.pay.channel.utils.DocumentUtils;
import com.cloud.pay.channel.utils.XmlSignature;
import com.cloud.pay.channel.vo.CloudTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeQueryResult;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;
import com.cloud.pay.common.contants.BohaiMessageEleEnumTmp;
import com.cloud.pay.common.contants.ChannelContants;
import com.cloud.pay.common.contants.ChannelErrorCode;

/**
 * 渤海银行模板类
 * @author wangy
 */
@Component
public class BohaiTradeExecutor<M extends BohaiCloudTradeParam,R extends BohaiCloudTradeResult> {
	 
	protected Logger log = LoggerFactory.getLogger(getClass());
     
	@Value("${cloud.bohai.pay.private.key.path}")
	private String priKeyPath;
	
	@Value("${cloud.bohai.pay.public.key.path}")
	private String pubKeyPath;
	
	@Value("${cloud.bohai.pay.private.key.password}")
	private String priKeyPwd;
	
	@Value("${cloud.bohai.pay.private.key.alias}")
	private String aliasName;
	
	private final String charset = "utf-8";
	
	
	protected R request(M param,String reqName) {
		//log.info("代付-渤海代付{}-请求参数：{}",reqName,param);
		//构建响应参数 
		try {
			Map<String, Object> map = ChannelBeanUtils.object2Map(param);
			String xml = toXml(map,param.getSerialNo(),reqName);
			log.info("代付-渤海代付{}-请求参数转为xml:{}",reqName,xml);
			Document doc = DocumentUtils.stringToDoc(xml);
			PrivateKey privateKey = XmlSignature.getPrivateKey(priKeyPath, priKeyPwd, aliasName);
			String xmlStr = XmlSignature.generateXMLDigitalSignature1(doc, "", privateKey, priKeyPwd);
			log.info("代付-渤海代付{}-请求参数加密后:{}",reqName,xmlStr);
			
			String rspXml = buildResponseTest(param.getSerialNo(), param.getInstId(), param.getCertId(), reqName);
			
			log.info("代付-渤海代付{}-响应报文:{}",reqName,rspXml);
		    boolean verify = XmlSignature.validateXMLDigitalSignature(rspXml, pubKeyPath);
		    log.info("代付-渤海代付{}-响应结果验证：{}",reqName,verify);
		    return buildResult(rspXml,param.getSerialNo());
		}catch(Exception e) {
			log.error("请求渤海代付异常",e);
		}
		return null;
	}
	
	/**
	 * 构建响应结果，通过子类重写实现
	 * @param xmlRsp
	 * @return
	 */
	protected R buildResult(String xmlRsp,String serialNo) {
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
	
	/**
	 * 测试构建响应结果
	 * @param orderNo
	 * @param instId
	 * @param certId
	 * @param reqName
	 * @return
	 * @throws Exception
	 */
	private String buildResponseTest(String orderNo,String instId,String certId,String reqName) throws Exception{
		if(!orderNo.startsWith("2018")) {
			return buildErrorResponseTest(orderNo, instId, certId, ChannelErrorCode.ERROR_1000, "余额不足");
		}else {
		     return buildSuccessResponseTest(orderNo, instId,  certId, reqName);
		}
	}
	
	
	/**
	 * 错误代码，测试用
	 * @return
	 */
    private String buildErrorResponseTest(String orderNo,String instId,String certId,String errorCode,String errorMessage) throws Exception{
    	StringBuilder buf = new StringBuilder();
    	buf.append("<Message id='"+orderNo+"'>");
    	buf.append("<Error>");
    	buf.append("<version>1.0.0</version>");
    	buf.append("<instId>"+instId+"</instId>");
    	buf.append("<certId>"+certId+"</certId>");
    	buf.append("<errorCode>"+errorCode+"</errorCode>");
    	buf.append("<errorMessage>"+errorMessage+"</errorMessage>");
    	buf.append("</Error>");
    	buf.append("</Message>");
    	Document doc = DocumentUtils.stringToDoc(buf.toString());
    	PrivateKey privateKey = XmlSignature.getPrivateKey(priKeyPath, priKeyPwd, aliasName);
		String xmlStr = XmlSignature.generateXMLDigitalSignature1(doc, "", privateKey, priKeyPwd);
    	return xmlStr;
    }  
    
    private String buildSuccessResponseTest(String orderNo,String instId,String certId,String reqName)throws Exception{
    	StringBuilder buf = new StringBuilder();
    	buf.append("<Message id='"+orderNo+"'>");
    	buf.append("<"+BohaiMessageEleEnumTmp.getRsp(reqName)+">");
    	buf.append("<version>1.0.0</version>");
    	buf.append("<instId>"+instId+"</instId>");
    	buf.append("<certId>"+certId+"</certId>");
    	buf.append("<serialNo>"+orderNo+"</serialNo>");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	buf.append("<actDat>"+sdf.format(new Date())+"</actDat>");
    	buf.append("<rspCode>0</rspCode>");
    	if(reqName.equals(ChannelContants.CHANNEL_BOHAI_REQ_HEADER_SCTQ)) {
    		buf.append(buildQuerySuccResTest(orderNo, instId, certId, reqName));
    	}
    	buf.append("</"+BohaiMessageEleEnumTmp.getRsp(reqName)+">");
    	buf.append("</Message>");
    	Document doc = DocumentUtils.stringToDoc(buf.toString());
    	PrivateKey privateKey = XmlSignature.getPrivateKey(priKeyPath, priKeyPwd, aliasName);
		String xmlStr = XmlSignature.generateXMLDigitalSignature1(doc, "", privateKey, priKeyPwd);
		return xmlStr;
    }

    private String buildQuerySuccResTest(String orderNo,String instId,String certId,String reqName) throws Exception{
    	StringBuilder buf = new StringBuilder();
    	buf.append("<pyrAct>111111111111</pyrAct>");
    	buf.append("<pyrNam>老黑</pyrNam>");
    	buf.append("<pyeAct>2222222222222</pyeAct>");
    	buf.append("<pyeNam>孙悟空</pyeNam>");
    	buf.append("<pyeBnk>3150000</pyeBnk>");
    	buf.append("<amt>1000.00</amt>");
    	buf.append("<postscript>代付1000.00给孙悟空</postscript>");
		return buf.toString();
    }	
}
