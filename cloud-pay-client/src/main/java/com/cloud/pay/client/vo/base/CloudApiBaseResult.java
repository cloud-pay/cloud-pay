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
    
    //机构编码
    private String mchCode;
    
    //商户编码
    private String subMchCode;
    
    //签名信息
    private String sign;
    
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
	
	public String getMchCode() {
		return mchCode;
	}

	public void setMchCode(String mchCode) {
		this.mchCode = mchCode;
	}

	public String getSubMchCode() {
		return subMchCode;
	}

	public void setSubMchCode(String subMchCode) {
		this.subMchCode = subMchCode;
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
