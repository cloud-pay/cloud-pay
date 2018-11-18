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
    String SUB_MCH_INVALID = "subMchInvalid";
    
    //商户当前状态不允许修改
    String SUB_MCH_STATUS_INVALID="subMchStatusInvalid";
    
    //订单不存在
    String ORDER_NOT_EXIST= "orderNotExist";
    
    //订单号存在
    String ORDER_EXIST ="orderExist";
    
    //批次不存在
    String BATCH_NOT_EXIST = "batchNotExist";
    
    //批次已存在
    String BATCH_EXIST = "batchExist";
    
    //子商户类型错误
    String SUB_MCH_TYPE_ERROR = "subMchTypeError";
    
    //对账文件未生成
    String RECON_FILE_NOT_CREATE = "reconFileNotCreate";
    
    //批次数据异常
    String BATCH_DATA_ERROR = "batchDataError";

    String NOT_AUTHORITY = "notAuthority";
}
