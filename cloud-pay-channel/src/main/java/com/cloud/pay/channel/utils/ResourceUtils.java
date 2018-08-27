package com.cloud.pay.channel.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {
   
	public static byte[] getInputStreamByte(String resourcePath) {
		try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
			InputStream is = new FileInputStream(new File(resourcePath));
			byte[] buff = new byte[1024];  
			int rc = 0; 
			while ((rc = is.read(buff, 0, 100)) > 0) { 
				swapStream.write(buff, 0, rc);  
			}
			byte[] in2b = swapStream.toByteArray();  
			return in2b;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
