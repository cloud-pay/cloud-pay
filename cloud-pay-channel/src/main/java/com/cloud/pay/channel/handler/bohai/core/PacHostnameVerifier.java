package com.cloud.pay.channel.handler.bohai.core;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class PacHostnameVerifier implements HostnameVerifier {

    public boolean verify(String urlHostName, SSLSession session) {
        System.out.println("Warning:URL Host:" + urlHostName + " vs. "
                + session.getPeerHost());
        return true;
    }

}
