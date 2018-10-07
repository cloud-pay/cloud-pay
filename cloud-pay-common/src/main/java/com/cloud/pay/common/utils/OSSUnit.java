package com.cloud.pay.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

/**
 * 阿里云OSS
 * @author wangy
 */
@Service
public class OSSUnit {
  
	private static final Logger logger = LoggerFactory.getLogger(OSSUnit.class);

	
	public static String endpoint;
	
	
	public static String accessKeyId;
	
	
	public static String secretAccessKey;
	
    /** 
     * 获取阿里云OSS客户端对象 
     * */  
    public static OSSClient getOSSClient(){  
        return new OSSClient(endpoint,accessKeyId, secretAccessKey); 
    }  
    
    /** 
     * 新建Bucket  --Bucket权限:私有 
     * @param bucketName bucket名称 
     * @return true 新建Bucket成功 
     * */  
    public static final boolean createBucket(OSSClient client, String bucketName){  
        Bucket bucket = client.createBucket(bucketName);   
        return bucketName.equals(bucket.getName());  
    } 
    
    
    /** 
     * 删除Bucket  
     * @param bucketName bucket名称 
     * */  
    public static final void deleteBucket(OSSClient client, String bucketName){  
        client.deleteBucket(bucketName);   
        logger.info("删除" + bucketName + "Bucket成功");  
    }  
    
    
    
    /**
     * 存储文件到OSS服务器
     * @param is 输入文件流
     * @param fileName 文件名称
     * @param bucketName bucket名称
     * @param fileFullPath bucket下的文件全路径
     * @return String 唯一MD5数字签名 
     */
    public static final String uploadObject2OSS(InputStream is,String fileName,Long fileSize,String bucketName,String fileFullPath) {
    	String resultStr = null;  
    	try {
    		OSSClient client = getOSSClient();
    		 //创建上传Object的Metadata  
            ObjectMetadata metadata = new ObjectMetadata();  
            metadata.setContentLength(is.available());  
            metadata.setCacheControl("no-cache");  
            metadata.setHeader("Pragma", "no-cache");  
            metadata.setContentEncoding("utf-8");  
            metadata.setContentType(getContentType(fileName));  
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte."); 
            PutObjectResult putResult = client.putObject(new PutObjectRequest(bucketName, fileFullPath, is, metadata));  
            //解析结果  
            resultStr = putResult.getETag();  
    	}catch (Exception e) {  
        	logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);  
        }  
    	return resultStr;
    }
    
    /** 
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代 (不会异步通知服务器)
     * @param client OSS客户端 
     * @param file 上传文件 
     * @param bucketName bucket名称 
     * @param diskName 上传文件的目录  --bucket下文件的路径 
     * @return String 唯一MD5数字签名 
     * */  
    public static final String uploadFile2OSS(OSSClient client, File file, String bucketName, String diskName,String appId) {  
        String resultStr = null;  
        try {  
            InputStream is = new FileInputStream(file);  
            String fileName = file.getName();  
            Long fileSize = file.length();  
            //创建上传Object的Metadata  
            ObjectMetadata metadata = new ObjectMetadata();  
            metadata.setContentLength(is.available());  
            metadata.setCacheControl("no-cache");  
            metadata.setHeader("Pragma", "no-cache");  
            metadata.setContentEncoding("utf-8");  
            metadata.setContentType(getContentType(fileName));  
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            PutObjectRequest object = new PutObjectRequest(bucketName, diskName + fileName, is, metadata);
            //上传文件   
            PutObjectResult putResult = client.putObject(object);
            //解析结果  
            resultStr = putResult.getETag();  
        } catch (Exception e) {  
        	logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);  
        }  
        return resultStr;  
    }  
    
    
    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public static final String getUrl(OSSClient client,File file,String diskName,String bucketName) {
      // 设置URL过期时间为10年  3600l* 1000*24*365*10
      Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
      // 生成URL
      URL url = client.generatePresignedUrl(bucketName,  diskName + file.getName(), expiration);
      if (url != null) {
        return url.toString();
      }
      return null;
    }
      
    /**  
     * 根据key获取OSS服务器上的文件输入流 
     * @param client OSS客户端 
     * @param bucketName bucket名称 
     * @param diskName 文件路径 
     * @param key Bucket下的文件的路径名+文件名 
     */    
     public static final InputStream getOSS2InputStream(OSSClient client, String bucketName, String diskName, String key){   
        OSSObject ossObj = client.getObject(bucketName, diskName + key);  
        return ossObj.getObjectContent();     
     }   
     
     /**
      * 根据文件全路径获取OSS服务器的文件流
      * @param client
      * @param bucketName
      * @param fileFullName
      * @return
      */
     public static final InputStream getOSS2InputStream(OSSClient client, String bucketName, String fileFullName){   
         OSSObject ossObj = client.getObject(bucketName, fileFullName);  
         return ossObj.getObjectContent();     
      }   
     
     /**  
      * 根据key删除OSS服务器上的文件  
      * @param client OSS客户端 
      * @param bucketName bucket名称 
      * @param diskName 文件路径 
      * @param key Bucket下的文件的路径名+文件名 
      */    
     public static void deleteFile(OSSClient client, String bucketName, String diskName, String key){    
          client.deleteObject(bucketName, diskName + key);   
          logger.info("删除" + bucketName + "下的文件" + diskName + key + "成功");  
     }  
     
     /**  
      * 通过文件名判断并获取OSS服务文件上传时文件的contentType  
      * @param fileName 文件名 
      * @return 文件的contentType    
      */    
      public static final String getContentType(String fileName){    
         String fileExtension = fileName.substring(fileName.lastIndexOf("."));  
         if("bmp".equalsIgnoreCase(fileExtension)) return "image/bmp";  
         if("gif".equalsIgnoreCase(fileExtension)) return "image/gif";  
         if("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)  || "png".equalsIgnoreCase(fileExtension) ) return "image/jpeg";  
         if("html".equalsIgnoreCase(fileExtension)) return "text/html";  
         if("txt".equalsIgnoreCase(fileExtension)) return "text/plain";  
         if("vsd".equalsIgnoreCase(fileExtension)) return "application/vnd.visio";  
         if("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) return "application/vnd.ms-powerpoint";  
         if("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) return "application/msword";  
         if("xml".equalsIgnoreCase(fileExtension)) return "text/xml";  
         return "text/html";    
      }    
         
      public static void main(String[] args) throws FileNotFoundException, IOException, ParserConfigurationException, JDOMException {
//    	  XMLOutputter XMLOut = new XMLOutputter(FormatXML());  //生成xml文件 
//    	  String filePath = "D:\\file\\upload\\c3\\zxywUserApiSzTest\\180103_193300_201801030000000094\\cmd.xml";
//    	  //XMLOut.output(creatXml(), new FileOutputStream(filePath));  
//    	  String message = uploadObject2OSS(getOSSClient(), new File(filePath), "rcmd", "c3/zxywUserApiSzTest/180103_193300_201801030000000094/","zxywUserApiSzTest");
//    	  if(!"".equals(message)){
//    		  InputStream inputStream = getOSS2InputStream(getOSSClient(), "rcmd", "c3/zxywUserApiSzTest/180103_193300_201801030000000094/", "cmd.result.xml");
//    		  SAXBuilder builder = new SAXBuilder();
//    		  Document doc = builder.build(inputStream);
//    		  XMLOut.output(doc, new FileOutputStream("D:\\file\\upload\\c3\\zxywUserApiSzTest\\180103_193300_201801030000000094\\cmd.result.xml"));  
//    	  }
//    	  logger.info(message);
	  }
      
      private static Format FormatXML(){    
          //格式化生成的xml文件   
          Format format = Format.getCompactFormat();    
          format.setEncoding("utf-8");    
          format.setIndent(" ");    
          return format;    
      }   
      
      private static  Document creatXml(){
       	  Element cmdData=new Element("cmdData");
    	  Element appIdElement = new Element("AppID");  
    	  appIdElement.setText("zxywUserApiSzTest");
    	  cmdData.addContent(appIdElement);
    	  Element appSecretElement = new Element("AppSecret");
    	  appSecretElement.setText("113355678");
    	  cmdData.addContent(appSecretElement);
    	  Element cmd = new Element("cmd");
    	  cmd.setText("test");
    	  cmdData.addContent(cmd);
    	  Element opretor = new Element("操作员");
    	  opretor.setText("王超");
    	  cmdData.addContent(opretor);
    	  Element cmdguid = new Element("cmdguid");
    	  cmdguid.setText("2a59f9e6-5705-461a-af83-5548636651846834");
    	  cmdData.addContent(cmdguid);
    	  Element cmdtime = new Element("cmdtime");
    	  cmdtime.setText("2017-12-13 17:59:26");
    	  cmdData.addContent(cmdtime);
    	  
    	  Element root = new Element("root");
    	  root.addContent(cmdData);
    	  Document document=new Document(root);
    	  return document;
      }

	public String getEndpoint() {
		return endpoint;
	}

	@Value("${cloud.alipay.oss.endpoint}")
	public void setEndpoint(String endpoint) {
		OSSUnit.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	@Value("${cloud.alipay.oss.accessKeyId}")
	public void setAccessKeyId(String accessKeyId) {
		OSSUnit.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	@Value("${cloud.alipay.oss.secretAccessKey}")
	public void setSecretAccessKey(String secretAccessKey) {
		OSSUnit.secretAccessKey = secretAccessKey;
	}
      
      
}
