package com.cloud.pay.recon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloud.pay.recon.entity.Recon;

/**
 * 对账异步线程
 * @author wangy
 */
public class ReconThread extends Thread {
	
	private Logger log = LoggerFactory.getLogger(ReconThread.class);
    
	private Recon recon;
	
	private ReconService reconService;
	
	public ReconThread() {
		
	}
	
	public ReconThread(Recon recon,ReconService reconService) {
		this.recon = recon;
		this.reconService = reconService;
	}
	
	@Override
	public void run() {
		reconService.recon(recon);
	}

	public Recon getRecon() {
		return recon;
	}

	public void setRecon(Recon recon) {
		this.recon = recon;
	}
	
}
