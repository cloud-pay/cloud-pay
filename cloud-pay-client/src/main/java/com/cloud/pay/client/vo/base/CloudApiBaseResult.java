package com.cloud.pay.client.vo.base;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.cloud.pay.client.constants.Constants;

/**
 * 接口响应参数
 * @author 
 */
public class CloudApiBaseResult implements Serializable {

	private static final long serialVersionUID = 3959251493603871375L;

	//响应码
    protected String returnCode = Constants.RETURN_CODE_SUCCESS;
    
    //响应信息
    protected String returnMsg;
    
    //业务状态
    protected String resultCode  = Constants.RESULT_CODE_SUCCESS;
    
    //错误码
    protected String errorCode ;
    
    //错误消息
    protected String errorMsg;
    
    //交易流水
    protected String orderNo;
    
    //机构编码
    protected String orgCode;
    
    //商户编码
    protected String merchantCode;
    
    //签名信息
    protected String sign;
    
    public CloudApiBaseResult() {}
    
    public CloudApiBaseResult(String resultCode) {
    	this.resultCode = resultCode;
    }
    
    public CloudApiBaseResult(String returnCode,String returnMsg) {
    	this.returnCode = returnCode;
    	this.returnMsg = returnMsg;
    }
    
    public CloudApiBaseResult(String resultCode,String errorCode,String errorMsg) {
    	this.resultCode = resultCode;
    	this.errorCode = errorCode;
    	this.errorMsg = errorMsg;
    }
    
    public CloudApiBaseResult(String returnCode,String resultCode,String errorCode,String errorMsg) {
    	this.returnCode = returnCode;
    	this.resultCode = resultCode;
    	this.errorCode = errorCode;
    	this.errorMsg = errorMsg;
    }

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
    
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
