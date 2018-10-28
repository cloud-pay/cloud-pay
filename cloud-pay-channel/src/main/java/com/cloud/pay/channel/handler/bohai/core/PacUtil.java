/*
 *
 */
package com.cloud.pay.channel.handler.bohai.core;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  
 */
public class PacUtil {
	public static HostnameVerifier hv = new PacHostnameVerifier();

	public static String formatData(Map dataMap, String CHARSET) {
		log.info("Format data:" + dataMap);

		String xmlStr = null;

		String instSeq = (String) dataMap.get("serialNo");
		String instId = "";
		String certId = "";
		String instDate = null;
		String pyrAct = "";
		String pyrNam = "";

		if (null != dataMap.get("instId")) {
			instId = (String) dataMap.get("instId");
		}

		if (null != dataMap.get("certId")) {
			certId = (String) dataMap.get("certId");
		}

		if (null != dataMap.get("date")) {
			instDate = (String) dataMap.get("date");
		}

		if (null != dataMap.get("pyrAct")) {
			pyrAct = (String) dataMap.get("pyrAct");
		}

		if (null != dataMap.get("pyrNam")) {
			pyrNam = (String) dataMap.get("pyrNam");
		}

		String transType = (String) dataMap.get("transType");

		if (null == instDate || "".equals(instDate.trim())) {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			instDate = sdf.format(curDate);
		}

		StringBuffer buf = new StringBuffer("<?xml version='1.0' encoding='");
		buf.append(CHARSET);
		buf.append("'?><Cbhbpac><Message id='");
		buf.append(instSeq);
		buf.append("'><");
		buf.append(transType);
		buf.append("Req id='");
		buf.append(transType);
		buf.append("Req'><version>1.0.1</version>");

		buf.append("<instId>");
		buf.append(instId);
		buf.append("</instId>");

		buf.append("<certId>");
		buf.append(certId);
		buf.append("</certId>");

		buf.append("<serialNo>");
		buf.append(instSeq);
		buf.append("</serialNo>");

		buf.append("<date>");
		buf.append(instDate);
		buf.append("</date>");

		int threshold = 0;

		while (threshold < 3) {
			if ("SCSP".equals(transType) || "SCHP".equals(transType)) {
				buf.append("<pyrAct>");
				buf.append(pyrAct);
				buf.append("</pyrAct>");

				buf.append("<pyrNam>");
				buf.append(pyrNam);
				buf.append("</pyrNam>");

				buf.append("<pyeAct>");
				buf.append(dataMap.get("pyeAct"));
				buf.append("</pyeAct>");

				buf.append("<pyeNam>");
				buf.append( dataMap.get("pyeNam"));
				buf.append("</pyeNam>");

				buf.append("<pyeBnk>");
				buf.append(dataMap.get("pyeBnk"));
				buf.append("</pyeBnk>");

				buf.append("<amt>");
				buf.append(dataMap.get("amt"));
				buf.append("</amt>");

				buf.append("<postscript>");
				buf.append(dataMap.get("postscript"));
				buf.append("</postscript>");

				break;
			}

			if ("SCTQ".equals(transType)) {
				buf.append("<instSeq>");
				buf.append(instSeq);
				buf.append("</instSeq>");

				break;
			}

			if ("SCBP".equals(transType) || "SCBT".equals(transType)) {
				String totNum = (String) dataMap.get("totNum");
				String totAmt = (String) dataMap.get("totAmt");

				String fileNam = (String) dataMap.get("fileNam");
				String fileSha1 = (String) dataMap.get("fileSha1");

				buf.append("<pyrAct>");
				buf.append(pyrAct);
				buf.append("</pyrAct>");

				buf.append("<pyrNam>");
				buf.append(pyrNam);
				buf.append("</pyrNam>");

				buf.append("<totNum>");
				buf.append(totNum);
				buf.append("</totNum>");

				buf.append("<totAmt>");
				buf.append(totAmt);
				buf.append("</totAmt>");

				buf.append("<fileNam>");
				buf.append(fileNam);
				buf.append("</fileNam>");

				if (null != fileSha1 && !"".equals(fileSha1)) {
					buf.append("<fileSHA1>");
					buf.append(fileSha1);
					buf.append("</fileSHA1>");
				} else {

					try {
						FileInputStream fis = new FileInputStream(
								"/BHIBS/files/cbhbpac/btrn/upload/" + instId
										+ "/" + fileNam);
						byte[] fileContent = PacUtil.readAllByteFromStream(fis);
						String sha1 = DigestUtils.shaHex(fileContent);
						log.info("Cal SHA1:" + sha1);

						buf.append("<fileSHA1>");
						buf.append(sha1);
						buf.append("</fileSHA1>");

					} catch (Exception e) {
						log.error("Error while read file !", e);
					}
				}

				break;

			}

			if ("SCBR".equals(transType)) {
				String fileNam = (String) dataMap.get("fileNam");
				String termJnlno = (String) dataMap.get("termJnlno");

				buf.append("<fileNam>");
				buf.append(fileNam);
				buf.append("</fileNam>");

				buf.append("<termJnlno>");
				buf.append(termJnlno);
				buf.append("</termJnlno>");

				break;
			}

			if ("SCBS".equals(transType)) {
				String fileNam = (String) dataMap.get("fileNam");
				String seqNo = (String) dataMap.get("seqNo");
				String termJnlno = (String) dataMap.get("termJnlno");

				buf.append("<fileNam>");
				buf.append(fileNam);
				buf.append("</fileNam>");

				buf.append("<seqNo>");
				buf.append(seqNo);
				buf.append("</seqNo>");

				buf.append("<termJnlno>");
				buf.append(termJnlno);
				buf.append("</termJnlno>");

				break;

			}

			if ("SCRQ".equals(transType)) {
				String sysCd = (String) dataMap.get("sysCd");
				String begDat = (String) dataMap.get("begDat");
				String endDat = (String) dataMap.get("endDat");

				buf.append("<pyrAct>");
				buf.append(pyrAct);
				buf.append("</pyrAct>");

				buf.append("<sysCd>");
				buf.append(sysCd);
				buf.append("</sysCd>");

				buf.append("<begDat>");
				buf.append(begDat);
				buf.append("</begDat>");

				buf.append("<endDat>");
				buf.append(endDat);
				buf.append("</endDat>");

				break;
			}

			if ("SCBQ".equals(transType)) {

				buf.append("<pyrAct>");
				buf.append(pyrAct);
				buf.append("</pyrAct>");

				break;

			}

			if ("SCDQ".equals(transType)) {
				String qryDat = (String) dataMap.get("qryDat");

				buf.append("<pyrAct>");
				buf.append(pyrAct);
				buf.append("</pyrAct>");

				buf.append("<qryDat>");
				buf.append(qryDat);
				buf.append("</qryDat>");

				break;

			}

			threshold = threshold + 1;

		}

		buf.append("</");
		buf.append(transType);
		buf.append("Req></Message></Cbhbpac>");

		xmlStr = buf.toString();

		return xmlStr;
	}

	public static String issuePac(String sndContent, String CHARSET,
			int timeout, String hostUrl) throws Exception {
		final String contentType = "application/xml;charset=utf-8";

		byte[] content = null;
		try {
			content = sndContent.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			log.error("Error occur !", e);
		}

		System.getProperties().put("java.protocol.handler.pkgs",
				"com.ibm.net.ssl.www2.protocol");

		URL myUrl = new URL(hostUrl);

		if (log.isInfoEnabled()) {
			log.info(new StringBuffer("Send to ").append(hostUrl)
					.append(", content :").append(sndContent).toString());
		}

		HttpURLConnection httpConn = (HttpURLConnection) myUrl.openConnection();

		httpConn.setRequestMethod("POST");

		if (httpConn instanceof HttpsURLConnection) {
			HttpsURLConnection httpsConn = (HttpsURLConnection) httpConn;
			httpsConn.setHostnameVerifier(hv);

			X509TrustManager xtm = new PacTrustManager();
			TrustManager mytm[] = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, new SecureRandom());
			SSLSocketFactory factory = ctx.getSocketFactory();

			httpsConn.setSSLSocketFactory(factory);

		}
		httpConn.addRequestProperty("Content-Type", contentType);

		httpConn.setDoInput(true);
		httpConn.setDoOutput(true);

		httpConn.setConnectTimeout(timeout);
		httpConn.setReadTimeout(timeout);

		httpConn.connect();
		OutputStream out = httpConn.getOutputStream();
		out.write(content);
		out.flush();
		out.close();

		String rspContent = null;

		byte[] rspByte = readAllByteFromStream(httpConn.getInputStream());
		if (null != rspByte) {
			rspContent = new String(rspByte, CHARSET);
		} else {
			log.debug("can not read any content from server!");
		}

		httpConn.disconnect();
		httpConn = null;
		myUrl = null;

		if (log.isInfoEnabled()) {
			log.info(new StringBuffer("Receive response:").append(rspContent)
					.toString());
		}

		return rspContent;
	}

	public static byte[] formatPacFile(Map dataMap, String charset) {
		byte[] sndCont = null;

		String transType = (String) dataMap.get("transType");
		String instId = "100099";// default for inner test
		String certId = "1000992014081101";
		String fileName = null;
		String fileType = null;
		String fileSHA1 = null;
		String sig = null;
		byte[] fileCont = null;

		if (null != dataMap.get("instId")) {
			instId = (String) dataMap.get("instId");
		}

		if (null != dataMap.get("certId")) {
			certId = (String) dataMap.get("certId");
		}

		fileName = (String) dataMap.get("fileName");
		fileType = (String) dataMap.get("fileType");
		fileSHA1 = (String) dataMap.get("fileSHA1");
		sig = (String) dataMap.get("sig");

		if ("UploadFile".equals(transType)) {
			fileCont = (byte[]) dataMap.get("fileCont");

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

			bufHeader.append("fileType=");
			bufHeader.append(fileType);
			bufHeader.append("|");

			bufHeader.append("fileSHA1=");
			bufHeader.append(fileSHA1);
			bufHeader.append("|");

			bufHeader.append("sig=");
			bufHeader.append(sig);

			byte[] headerCont = null;
			try {
				headerCont = bufHeader.toString().getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				log.error("Error occur!", e);
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

		} else {
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

			bufHeader.append("fileType=");
			bufHeader.append(fileType);
			bufHeader.append("|");

			bufHeader.append("sig=");
			bufHeader.append(sig);

			byte[] headerCont = null;
			try {
				headerCont = bufHeader.toString().getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				log.error("Error occur!", e);
			}

			String strHeaderLen = PacUtil.formatPckLen(headerCont.length);
			String strTotalLen = PacUtil.formatPckLen(headerCont.length + 8);

			sndCont = new byte[8 + headerCont.length + 8];

			System.arraycopy(strTotalLen.getBytes(), 0, sndCont, 0, 8);

			System.arraycopy(strHeaderLen.getBytes(), 0, sndCont, 8, 8);

			System.arraycopy(headerCont, 0, sndCont, 16, headerCont.length);

		}

		return sndCont;
	}

	public static Map issuePacFile(byte[] sndCont, String charset, int timeout,
			String hostUrl) throws Exception {
		Map rspMap = new HashMap();

		final String contentType = "application/octet-stream";

		System.getProperties().put("java.protocol.handler.pkgs",
				"com.ibm.net.ssl.www2.protocol");

		URL myUrl = new URL(hostUrl);

		HttpURLConnection httpConn = (HttpURLConnection) myUrl.openConnection();

		httpConn.setRequestMethod("POST");

		if (httpConn instanceof HttpsURLConnection) {
			HttpsURLConnection httpsConn = (HttpsURLConnection) httpConn;
			httpsConn.setHostnameVerifier(hv);

			X509TrustManager xtm = new PacTrustManager();
			TrustManager mytm[] = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, new SecureRandom());
			SSLSocketFactory factory = ctx.getSocketFactory();

			httpsConn.setSSLSocketFactory(factory);

		}
		httpConn.addRequestProperty("Content-Type", contentType);

		httpConn.setDoInput(true);
		httpConn.setDoOutput(true);

		httpConn.setConnectTimeout(timeout);
		httpConn.setReadTimeout(timeout);

		httpConn.connect();
		OutputStream out = httpConn.getOutputStream();
		out.write(sndCont);
		out.flush();
		out.close();

		String rspContent = null;

		byte[] rspByte = readAllByteFromStream(httpConn.getInputStream());
		if (null != rspByte) {

			int totalLen = 0;
			int headerLen = 0;
			int bodyLen = 0;
			int readLen = 0;

			totalLen = Integer.parseInt(new String(rspByte, 0, 8).trim());

			headerLen = Integer.parseInt(new String(rspByte, 8, 8).trim());

			bodyLen = totalLen - headerLen - 8;

			rspContent = new String(rspByte, 16, headerLen, charset);
			String[] params = PacUtil.split(rspContent, '|');

			if (null != params) {
				String itm = null;
				int pos = -1;
				for (int i = 0; i < params.length; i++) {
					itm = params[i];
					pos = itm.indexOf('=');
					if (pos != -1) {

						rspMap.put(itm.substring(0, pos).trim(),
								itm.substring(pos + 1).trim());
					}
				}
			}

			if (bodyLen > 0) {
				byte[] fileCont = new byte[bodyLen];
				System.arraycopy(rspByte, 16 + headerLen, fileCont, 0,
						bodyLen);
				rspMap.put("fileCont", fileCont);
			}

		} else {
			log.debug("can not read any content from server!");
		}

		httpConn.disconnect();
		httpConn = null;
		myUrl = null;

		if (log.isInfoEnabled()) {
			log.info(new StringBuffer("Receive response:").append(rspContent)
					.toString());
		}

		return rspMap;
	}

	public static byte[] readAllByteFromStream(InputStream is) throws Exception {
		byte[] buf = new byte[8072];

		ByteArrayOutputStream baos = new ByteArrayOutputStream(8072);
		int readCount = is.read(buf);
		while (readCount != -1) {
			baos.write(buf, 0, readCount);
			readCount = is.read(buf);
		}

		byte[] rsByte = baos.toByteArray();

		baos.close();
		baos = null;

		return rsByte;

	}

	public static int readInLength(byte[] buf, InputStream is, int length)
			throws IOException {
		int readBytes = -1;
		int readCount = 0;

		while (readCount < length) {
			readBytes = is.read(buf, readCount, length - readCount);
			if (readBytes != -1) {
				readCount += readBytes;
			} else {
				break;
			}
		}

		return readCount;
	}

	public static String formatPckLen(int len) {
		DecimalFormat df = new DecimalFormat("00000000");
		return df.format(len);
	}

	/**
	 * ��ָ���ָ������������ַ��������ݴ���������־�ж��Ƿ�����ո�
	 * 
	 * @param toSplit
	 *            :����ֵ��ַ���
	 * @param delimiter
	 *            :�ָ���
	 * @param trim
	 *            :�Ƿ���Ҫ����ո�ı�־
	 * 
	 */
	public static String[] split(String toSplit, char delimiter, boolean trim) {

		if (toSplit == null) {
			return null;
		}
		int len = toSplit.length();
		if (len == 0) {
			return null;
		}
		List list = new ArrayList();
		String itm = null;
		int i = 0, start = 0;
		while (i < len) {

			if (toSplit.charAt(i) == delimiter) {
				itm = toSplit.substring(start, i);
				if (trim) {
					list.add(toStringAndTrim(itm));
				} else {
					list.add(itm);
				}

				start = ++i;
				continue;
			}
			i++;
		}

		itm = toSplit.substring(start, i);
		if (trim) {
			list.add(toStringAndTrim(itm));
		} else {
			list.add(itm);
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * ��ȡһ�������string��ʽ��ȥ���ո�
	 * 
	 * @param object
	 * 
	 * @return
	 * 
	 */
	public static String toStringAndTrim(Object object) {

		if (object == null) {
			return null;
		} else {
			return object.toString().trim();
		}
	}

	/**
	 * ��ָ���ָ������������ַ���������ո�
	 * 
	 * @param toSplit
	 *            :����ֵ��ַ���
	 * @param delimiter
	 *            :�ָ���
	 * 
	 */
	public static String[] split(String toSplit, char delimiter) {

		return split(toSplit, delimiter, true);
	}

	protected static Log log = LogFactory.getLog(PacUtil.class);
}