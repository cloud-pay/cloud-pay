package com.cloud.pay.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作工具类
 */
public class FileUtils {
 
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	
	/**
	 * 生成txt文件
	 * @param fileName 文件名
	 * @param filePath 文件路径
	 * @return false-生成文件失败 true-生成文件成功
	 * @throws IOException
	 */
	public static boolean createTxtFile(String fileName,String filePath) throws IOException{
		boolean flag = false;
		String fileFullPath = "";
		isExist(filePath);
		if(filePath.endsWith(File.separator)) {
			fileFullPath = filePath + fileName + ".txt";
		}else {
			fileFullPath = filePath + File.separator + fileName + ".txt";
		}
		logger.info("生成文件路径：{}",fileFullPath);
		File file = new File(fileFullPath);
		if(!file.exists()) {
			 file.createNewFile();
			 flag = true;
		}else {
			 file.delete();
			 file.createNewFile();
			 flag = true;
		}
		return flag;
	} 
	
	private static void isExist(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public static void main(String[] args) {
		isExist("e:\\reconFile\\merchant\\20181009");
	}
	
	/**
	 * 追加写文件
	 * @param newRow
	 * @return
	 * @throws IOException
	 */
	public static boolean appendWriteTxtFile(String newRow,String fileName,String filePath) throws IOException{
		boolean flag = false;
		String filein = newRow + "\r\n";
        String temp = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
        	//文件路径
        	String fileFullPath = "";
        	if(filePath.endsWith(File.separator)) {
    			fileFullPath = filePath + fileName + ".txt";
    		}else {
    			fileFullPath = filePath + File.separator + fileName + ".txt";
    		}
        	logger.info("写入文件路径：{}",fileFullPath);
        	File file = new File(fileFullPath);
        	fis = new FileInputStream(file);
        	isr = new InputStreamReader(fis);
        	br = new BufferedReader(isr);
        	StringBuffer buf = new StringBuffer();
        	//保存文件原有
        	for(int j = 1;(temp = br.readLine())!=null;j++) {
        		buf = buf.append(temp);
        		//行与行之间得分隔符
        		buf = buf.append(System.getProperty("line.separator"));
        	}
        	buf.append(filein);
        	fos = new FileOutputStream(file);
        	pw = new PrintWriter(fos);
        	pw.write(buf.toString().toCharArray());
        	pw.flush();
        	flag = true;
        }finally {
        	if(null != pw) {
        		pw.close();
        	}
        	if(null != fos) {
        		fos.close();
        	}
        	if(null != br) {
        		br.close();
        	}
        	if(null != isr) {
        		isr.close();
        	}
        	if(null != fis) {
        		fis.close();
        	}
        }
		return flag;
	}
	
	public static boolean writeTxtFile(String content,String fileName,String filePath) throws IOException{
		boolean flag = false;
		FileOutputStream fos = null;
	    PrintWriter pw = null;
	    try {
	    	//文件路径
        	String fileFullPath = "";
        	if(filePath.endsWith(File.separator)) {
    			fileFullPath = filePath + fileName + ".txt";
    		}else {
    			fileFullPath = filePath + File.separator + fileName + ".txt";
    		}
        	logger.info("写入文件路径：{}",fileFullPath);
        	File file = new File(fileFullPath);
        	fos = new FileOutputStream(file);
        	pw = new PrintWriter(fos);
        	pw.write(content.toCharArray());
        	pw.flush();
        	flag = true;
	    }finally {
	    	if(null != pw) {
	    		pw.close();
	    	}
	    	if(null != fos) {
	    		fos.close();
	    	}
	    }
		return flag;
	}
}
