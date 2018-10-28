package com.cloud.pay.channel.handler.bohai.core;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientOverHTTP {
	protected static Log log = LogFactory.getLog(ClientOverHTTP.class);

	private HostnameVerifier hv = new PacHostnameVerifier();

	private SSLSocketFactory sslSocketFactory = null;

	private String sessionMessage = null;

	public ClientOverHTTP() {
		super();
	}

	public SSLSocketFactory getSslSocketFactory() {
		return sslSocketFactory;
	}

	public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}

	public String issuePac(String sndContent, String CHARSET, int timeout,
			String hostUrl) throws Exception {
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

		if (httpConn instanceof HttpsURLConnection) {
			HttpsURLConnection httpsConn = (HttpsURLConnection) httpConn;
			httpsConn.setHostnameVerifier(hv);
			httpsConn.setSSLSocketFactory(sslSocketFactory);
		}

		httpConn.setRequestMethod("POST");

		if (null != sessionMessage) {
			httpConn.setRequestProperty("Cookie", sessionMessage);
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

		String setCookie = httpConn.getHeaderField("Set-Cookie");

		if (null != setCookie) {
			int index = setCookie.indexOf(";");
			if (index > 0) {
				sessionMessage = setCookie.substring(0, index);
			} else {
				sessionMessage = setCookie;
			}
		}

		String rspContent = null;

		byte[] rspByte = PacUtil.readAllByteFromStream(httpConn
				.getInputStream());
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

	public Map issuePacFile(byte[] sndCont, String charset, int timeout,
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
			httpsConn.setSSLSocketFactory(sslSocketFactory);
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

		byte[] rspByte = PacUtil.readAllByteFromStream(httpConn.getInputStream());
		if (null != rspByte) {
			System.out.println("IssueFile rsp:"+new String(rspByte));

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
				System.arraycopy(rspByte, 16 + headerLen, fileCont, 0, bodyLen);
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

}
