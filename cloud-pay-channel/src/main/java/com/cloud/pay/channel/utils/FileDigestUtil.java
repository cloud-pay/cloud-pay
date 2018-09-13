package com.cloud.pay.channel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloud.pay.common.exception.CloudPayException;

public class FileDigestUtil {
	
	private static Logger log = LoggerFactory.getLogger(FileDigestUtil.class);
   
	public static String getFileSHA1(File file) throws CloudPayException{
		MessageDigest md = null; 
		FileInputStream fis = null; 
		StringBuilder sha1Str = new StringBuilder(); 
		try { 
			fis = new FileInputStream(file); 
			MappedByteBuffer mbb = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			md = MessageDigest.getInstance("SHA-1"); 
			md.update(mbb); 
			byte[] digest = md.digest(); 
			String shaHex = ""; 
			for (int i = 0; i < digest.length; i++) { 
				shaHex = Integer.toHexString(digest[i] & 0xFF); 
				if (shaHex.length() < 2) { 
					sha1Str.append(0); 
				}
				sha1Str.append(shaHex); 
			}
			
		}catch (NoSuchAlgorithmException e) { 
			log.error("生成文件SHA-1摘要异常：{}",e);
			throw new CloudPayException("生成文件SHA-1摘要异常");
		}catch (FileNotFoundException e) { 
			log.error("读取文件错误:{}",e);
			throw new CloudPayException("读取文件错误");
		}catch (IOException e) { 
			log.error("读取文件异常:{}",e);
			throw new CloudPayException("读取文件异常");
		}finally { 
			if (fis != null) { 
				try {
				 fis.close(); 
				}catch(IOException e) {
					
				}
			}
		}
		return sha1Str.toString(); 
	}
}
