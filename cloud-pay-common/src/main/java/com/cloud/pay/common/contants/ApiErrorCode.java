package com.cloud.pay.common.contants;

/**
 * 下游接口错误码
 * @author
 */
public interface ApiErrorCode {
   
	//系统错误
	String SYSTEM_ERROR = "systemError";
	
	//参数错误
	String PARAM_ERROR = "paramError";
	
	//post数据为空
    String POST_DATA_EMPTY = "postDataEmpty";
    
    //非法商户
    String MCH_INVALID = "mchInvalid";
    
    // 签名错误
    String SIGN_ERROR = "signError";
    
    //商户信息不存在
    String SUB_MCH_INVALID = "subMchInavlid";
}
