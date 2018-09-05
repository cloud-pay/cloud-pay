package com.cloud.pay.recon.service;

import com.cloud.pay.common.entity.Channel;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.recon.entity.Recon;

/**
 * 对账接口
 * @author wangy
 */
public interface IReconServiceHandler {
   
	void handle(Recon recon)  throws CloudPayException;
}
