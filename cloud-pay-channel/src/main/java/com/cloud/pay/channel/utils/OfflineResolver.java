package com.cloud.pay.channel.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.xerces.util.URI;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.w3c.dom.Attr;

public class OfflineResolver extends ResourceResolverSpi {
	
	static Map<String, String> _uriMap = null;
    static Map<String, String> _mimeMap = null;
 
	public OfflineResolver() {}
	
	 static 
	    {
	        //注意此处的静态初始化！必须，否则第一次不加载会出现//加签问题
	        Init.init();
	        _uriMap = new HashMap<String, String>();
	        _mimeMap = new HashMap<String, String>();
	        register("http://www.w3.org/TR/xml-stylesheet", "data/org/w3c/www/TR/xml-stylesheet.html", "text/html");
	        register("http://www.w3.org/TR/2000/REC-xml-20001006", "data/org/w3c/www/TR/2000/REC-xml-20001006", "text/xml");
	        register("http://www.nue.et-inf.uni-siegen.de/index.html", "data/org/apache/xml/security/temp/nuehomepage", "text/html");
	        register("http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/id2.xml", "data/org/apache/xml/security/temp/id2.xml", "text/xml");
	        register("http://xmldsig.pothole.com/xml-stylesheet.txt", "data/com/pothole/xmldsig/xml-stylesheet.txt", "text/xml");
	        register("http://www.w3.org/Signature/2002/04/xml-stylesheet.b64", "data/ie/baltimore/merlin-examples/merlin-xmldsig-twenty-three/xml-stylesheet.b64", "text/plain");
	    }
	
	private static void register(String URI, String filename, String MIME)
    {
        _uriMap.put(URI, filename);
        _mimeMap.put(URI, MIME);
    }
	
	@Override
	public boolean engineCanResolve(Attr uri, String BaseURI) {
		String uriNodeValue = uri.getNodeValue();
        if(uriNodeValue.equals("") || uriNodeValue.startsWith("#"))
            return false;
        try
        {
            URI uriNew = new URI(new URI(BaseURI), uri.getNodeValue());
            if(uriNew.getScheme().equals("http"))
            {
                return true;
            }
        }
        catch(URI.MalformedURIException malformeduriexception) { }
        return false;
	}

	@Override
	public XMLSignatureInput engineResolve(Attr uri, String BaseURI) throws ResourceResolverException {
		try
        {
            String URI = uri.getNodeValue();
            if(_uriMap.containsKey(URI))
            {
                String newURI = (String)_uriMap.get(URI);
                InputStream is = new FileInputStream(newURI);
                XMLSignatureInput result = new XMLSignatureInput(is);
                result.setSourceURI(URI);
                result.setMIMEType((String)_mimeMap.get(URI));
                return result;
            } else
            {
                Object exArgs[] = {
                    "The URI " + URI + " is not configured for offline work"
                };
                throw new ResourceResolverException("generic.EmptyMessage", exArgs, uri, BaseURI);
            }
        }
        catch(IOException ex)
        {
            throw new ResourceResolverException("generic.EmptyMessage", ex, uri, BaseURI);
        }
	}
	
	

}
