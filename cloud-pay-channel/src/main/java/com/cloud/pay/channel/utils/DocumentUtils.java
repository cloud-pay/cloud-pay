package com.cloud.pay.channel.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 基于w3c的document转换
 * @author wangy
 */
public class DocumentUtils {

	 public static Document stringToDoc(String xmlStr) {    
	        //字符串转XML     
	        Document doc = null;    
	        try {    
	            xmlStr = new String(xmlStr.getBytes(),"UTF-8");    
	            StringReader sr = new StringReader(xmlStr);    
	            InputSource is = new InputSource(sr);    
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();    
	            DocumentBuilder builder;    
	            builder = factory.newDocumentBuilder();    
	            doc = builder.parse(is);    
	                
	        } catch (ParserConfigurationException e) {    
	            System.err.println(xmlStr);      
	            e.printStackTrace();    
	        } catch (SAXException e) {    
	            System.err.println(xmlStr);        
	            e.printStackTrace();    
	        } catch (IOException e) {    
	            System.err.println(xmlStr);        
	            e.printStackTrace();    
	        }    
	        return doc;    
	    }  
	 
	 public static String docToString(Document doc) {    
	        // XML转字符串     
	        String xmlStr = "";    
	        try {    
	            TransformerFactory tf = TransformerFactory.newInstance();    
	            Transformer t = tf.newTransformer();    
	            t.setOutputProperty("encoding", "UTF-8");// 解决中文问题，试过用GBK不行     
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();    
	            t.transform(new DOMSource(doc), new StreamResult(bos));    
	            xmlStr = bos.toString();    
	        } catch (TransformerConfigurationException e) {    
	            e.printStackTrace();    
	        } catch (TransformerException e) {    
	            e.printStackTrace();    
	        }    
	        return xmlStr;    
	    }   
}
