package com.cloud.pay.channel.handler.bohai;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.cloud.pay.channel.handler.bohai.core.ClientOverHTTP;
import com.cloud.pay.channel.handler.bohai.core.PacUtil;
import com.cloud.pay.channel.utils.ChannelBeanUtils;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeParam;
import com.cloud.pay.channel.vo.bohai.BohaiCloudTradeResult;


/**
 * 渤海银行模板类
 * @author wangy
 */
@Component
public class BohaiTradeExecutor<M extends BohaiCloudTradeParam,R extends BohaiCloudTradeResult> {
	 
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	private final String charset = "utf-8";
	
	@Value("${cloud.pay.bohai.hostUrl}")
	private String hostUrl;
	
	@Value("${cloud.bohai.pay.instId}")
	private String instId;
	
	@Value("${cloud.bohai.pay.certId}")
	private String certId;
	
	protected R request(M param,String reqName) {
		log.info("代付-渤海代付{}-请求参数：{}",reqName,param);
		//构建响应参数 
		try {
			Map<String, Object> map = ChannelBeanUtils.object2Map(param);
			map.put("transType", reqName);
			map.put("instId", instId);
			map.put("certId", certId);
			ClientOverHTTP client = new ClientOverHTTP();
			String sndMsg = toXml(map,param.getSerialNo(),reqName);
					//PacUtil.formatData(map, charset);
			log.info("代付-渤海代付{}-请求参数：{}",reqName,sndMsg);
			String response = client.issuePac(sndMsg, charset, 60000, hostUrl);
			log.info("代付-渤海代付{}-响应报文:{}",reqName,response);
		    return buildResult(response,param.getSerialNo());
		}catch(Exception e) {
			log.error("请求渤海代付异常",e);
		}
		return null;
	}
	
	protected Map<String,String> issuePacFile(String folder4Upload,String uploadFileName) {
		byte[] sndCont  = formatUpload(folder4Upload, uploadFileName, instId, certId);
		Map rcvMap = null;
		try {
			ClientOverHTTP client = new ClientOverHTTP();
			rcvMap = client.issuePacFile(sndCont, charset, 60 * 1000, hostUrl
					+ "uploadFile.do");
			if (null != rcvMap) {
				System.out.println("UploadFile result:" + rcvMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcvMap;
	}
	
	
	private byte[] formatUpload(String folder, String fileName,
			String instId, String certId) {
		byte[] sndCont = null;
		String fileSHA1 = null;
		String sigAlg = "SHA1WithRSA";
		byte[] fileCont = null;

		File uploadFile = null;
		FileInputStream fis = null;

		try {
			uploadFile = new File(folder, fileName);
			fis = new FileInputStream(uploadFile);

			fileCont = PacUtil.readAllByteFromStream(fis);

			if (null != fileCont) {
				fileSHA1 = DigestUtils.shaHex(fileCont);
			}
			StringBuffer bufHeader = new StringBuffer();
			bufHeader.append("instId=");
			bufHeader.append(instId);
			bufHeader.append("|");
			bufHeader.append("certId=");
			bufHeader.append(certId);
			bufHeader.append("|");
			bufHeader.append("fileName=");
			bufHeader.append(fileName);
			bufHeader.append("|");
			bufHeader.append("sigAlg=");
			bufHeader.append(sigAlg);
			bufHeader.append("|");
			bufHeader.append("fileSHA1=");
			bufHeader.append(fileSHA1);
			byte[] headerCont = null;
			try {
				headerCont = bufHeader.toString().getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String strHeaderLen = PacUtil.formatPckLen(headerCont.length);
			String strTotalLen = PacUtil.formatPckLen(headerCont.length + 8
					+ fileCont.length);
			sndCont = new byte[8 + headerCont.length + 8 + fileCont.length];
			System.arraycopy(strTotalLen.getBytes(), 0, sndCont, 0, 8);
			System.arraycopy(strHeaderLen.getBytes(), 0, sndCont, 8, 8);
			System.arraycopy(headerCont, 0, sndCont, 16, headerCont.length);
			System.arraycopy(fileCont, 0, sndCont, 16 + headerCont.length,
					fileCont.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != uploadFile) {
				uploadFile = null;
			}
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fis = null;
			}
		}
		return sndCont;
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
		String reqHeader = reqName+"Req";
		buf.append("<?xml version='1.0' encoding='utf-8'?><Cbhbpac>");
		buf.append("<Message id='"+orderNo+"'>");
		buf.append("<"+reqHeader+" id='"+reqHeader+"'>");
		for (String key : keys) {
			if("serialVersionUID".equals(key)) {
				continue;
			}
			if(null == params.get(key)) {
				continue;
			}
			buf.append("<").append(key).append(">");
			buf.append(params.get(key));
			buf.append("</").append(key).append(">");
		}
		buf.append("</"+reqHeader+">");
		buf.append("</Message>");
		buf.append("</Cbhbpac>");
		return buf.toString();

	}
	
	/**
	 * 构建响应结果，通过子类重写实现
	 * @param xmlRsp
	 * @return
	 * @throws DocumentException 
	 */
	protected R buildResult(String xmlRsp,String serialNo) {
		return null;
	}
	
	
}
