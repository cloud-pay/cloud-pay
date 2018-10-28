package com.cloud.pay.channel.handler.bohai.core;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class PacTrustManager  implements X509TrustManager{

    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        System.out.println("1");
        
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        System.out.println("2");
        
    }

    public X509Certificate[] getAcceptedIssuers() {
        System.out.println("3");
        return null;
    }

}
