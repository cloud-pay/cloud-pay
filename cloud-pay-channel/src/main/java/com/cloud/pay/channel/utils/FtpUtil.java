package com.cloud.pay.channel.utils;

import java.io.File;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


/**
 * ftp工具
 * @author admin
 */
public class FtpUtil {
  
	private final static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
  
	public static void downloadSftpFile(String ftpHost, String ftpUserName,
			String ftpPassword, int ftpPort, String ftpPath, String localPath,
			String fileName) throws Exception {
		Session session = null;
		Channel channel = null;
		
		JSch jsch = new JSch();
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
		session.setPassword(ftpPassword);
		session.setTimeout(100000);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
 
		channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp chSftp = (ChannelSftp) channel;
 
		String ftpFilePath = ftpPath + "/" + fileName;
		String localFilePath = localPath + File.separatorChar + fileName;
		//判断本地文件夹是否存在，不存在则创建
		File file = new File(localPath);
		if(!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		try {
			chSftp.get(ftpFilePath, localPath);
		} catch (Exception e) {
			logger.error("读取文件错误：{}",e);
            throw new Exception("对账文件不存在");
		} finally {
			chSftp.quit();
			channel.disconnect();
			session.disconnect();
		}
 
	}

}
