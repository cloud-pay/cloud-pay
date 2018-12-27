package com.cloud.pay.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	public static boolean createFile(String fileName,String filePath,String suffix)throws IOException{
		boolean flag = false;
		String fileFullPath = "";
		isExist(filePath);
		if(filePath.endsWith(File.separator)) {
			fileFullPath = filePath + fileName + suffix;
		}else {
			fileFullPath = filePath + File.separator + fileName + suffix;
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
	
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	public static boolean isExist(String fileName,String filePath,String suffix) throws IOException{
		boolean flag = false;
		String fileFullPath = "";
		if(filePath.endsWith(File.separator)) {
			fileFullPath = filePath + fileName + suffix;
		}else {
			fileFullPath = filePath + File.separator + fileName + suffix;
		}
		File file = new File(fileFullPath);
		if(file.exists()) {
			flag = true;
		}
		return flag;
	}
	
	private static void isExist(String path){
//		String paths[] = path.split(File.separator);
		String paths[] = path.split("\\\\");
		String dir = paths[0];
		for (int i = 0; i < paths.length - 1; i++) {
			 dir = dir + "/" + paths[i + 1];  
			   File dirFile = new File(dir);   
			   if (!dirFile.exists()) {  
			       dirFile.mkdir(); 
			   } 
		}
	}
	
	public static void main(String[] args) {
		System.out.println(File.separator);
		isExist("e:\\reconFile\\merchant\\20181009");
	}
	
	public static void writeFileFromList(List<String> list,String fileName,String filePath,String suffix) throws IOException{
        FileOutputStream fos = null;
        BufferedWriter out = null;
        try {
        	String fileFullPath = "";
        	if(filePath.endsWith(File.separator)) {
    			fileFullPath = filePath + fileName + suffix;
    		}else {
    			fileFullPath = filePath + File.separator + fileName + suffix;
    		}
        	logger.info("写入文件路径：{}",fileFullPath);
        	File file = new File(fileFullPath);
        	out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "gb2312"));
        	for (String info : list) {
                out.write(info);
                out.newLine();
            }
        }finally {
        	if(null != out) {
        		out.flush();
        		out.close();
        	}
        	if(null != fos) {
        		fos.close();
        	}
        }
	}
	
	/**
	 * 追加写文件
	 * @param content 追加内容
	 * @param fileName  文件名
	 * @param filePath  文件路径
	 * @return
	 * @throws IOException
	 */
	public static boolean appendWriteFile(String content,String fileName,String filePath,String suffix) throws IOException{
		boolean flag = false;
        String temp = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        FileOutputStream fos = null;
        BufferedWriter out = null;
        try {
        	//文件路径
        	String fileFullPath = "";
        	if(filePath.endsWith(File.separator)) {
    			fileFullPath = filePath + fileName + suffix;
    		}else {
    			fileFullPath = filePath + File.separator + fileName + suffix;
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
        		//buf = buf.append(System.getProperty("line.separator"));
        	}
        	buf.append(content);
        	fos = new FileOutputStream(file);
        	out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"gb2312"));
        	out.write(buf.toString().toCharArray());
        	flag = true;
        }finally {
        	if(null != out) {
        		out.flush();
        		out.close();
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
	
	/**
	 * 读取文件写入另一个文件
	 * @param fromFileFullPath
	 * @param toFileFullPath
	 * @param isAppend 是否追加
	 * @return
	 * @throws IOException
	 */
	public static boolean writeTxtFromOtherTxtFile(String fromFileFullPath,String toFileFullPath,boolean isAppend)throws IOException{
	 	 boolean flag = false;
		 FileInputStream fis = null;
	     InputStreamReader isr = null;
	     BufferedReader br = null;
	     
	     FileOutputStream fos = null;
	     PrintWriter pw = null;
	     try {
	    	 File fromFile = new File(fromFileFullPath);
	    	 File toFile = new File(toFileFullPath);
	    	 fis = new FileInputStream(fromFile);
	    	 fos = new FileOutputStream(toFile,isAppend);
	    	 isr = new InputStreamReader(fis);
	    	 String temp = "";
	    	 br = new BufferedReader(isr);
	    	 pw = new PrintWriter(fos);
	         StringBuffer buf = new StringBuffer();
	         while((temp=br.readLine())!=null){
	        	 buf.append(temp);//"\n"读取换行
	        	 //行与行之间得分隔符
	        	 buf = buf.append(System.getProperty("line.separator"));
	         }
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
	
	/**
	 * 写文件
	 * @param content 文件内容
	 * @param fileName 文件名
	 * @param filePath 文件路径
	 * @return
	 * @throws IOException
	 */
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
	
	public static String utf8Togb2312(String str){
	      StringBuffer sb = new StringBuffer();
	      for(int i=0; i<str.length(); i++) {
	          char c = str.charAt(i);
	          switch (c) {
	             case '+':
	                 sb.append(' ');
	             break;
	             case '%':
	                 try {
	                      sb.append((char)Integer.parseInt(
	                      str.substring(i+1,i+3),16));
	                 }
	                 catch (NumberFormatException e) {
	                     throw new IllegalArgumentException();
	                }
	                i += 2;
	                break;
	             default:
	                sb.append(c);
	                break;
	           }
	      }
	      String result = sb.toString();
	      String res=null;
	      try{
	          byte[] inputBytes = result.getBytes("8859_1");
	          res= new String(inputBytes,"gb2312");
	      }
	      catch(Exception e){}
	      return res;
	}
}
