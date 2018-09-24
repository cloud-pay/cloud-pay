package com.cloud.pay.channel.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.cloud.pay.common.contants.ChannelContants;

/**
 * 渠道响应参数基类
 * @author wangy
 */
public class BaseTradeResVO implements Serializable{

	private static final long serialVersionUID = -7869992528965033473L;
	
	private Integer merchantId;//平台商户号
	
	private String orderNo; //平台订单号
	
	protected String respCode = ChannelContants.CHANNEL_RESP_CODE_SUCCESS; //响应码
	
	private String respMsg; //响应信息
	
	private String errorCode; //错误代码
	
	private String errorMessage; //错误描述
	
	public BaseTradeResVO() {}
	
	public BaseTradeResVO(String errorCode,String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		respCode = ChannelContants.CHANNEL_RESP_CODE_FAIL;
	}
	
	public BaseTradeResVO(Integer merchantId,String orderNo,String respMsg) {
	      this.merchantId = merchantId;
	      this.orderNo = orderNo;
	      this.respMsg = respMsg;
	}
	
	public BaseTradeResVO(Integer merchantId,String orderNo,String respCode,String respMsg) {
		  this.merchantId = merchantId;
		  this.orderNo = orderNo;
		  this.respCode = respCode;
		  this.respMsg = respMsg;
	}
	
	public BaseTradeResVO(Integer merchantId,String orderNo,String respCode,String errorCode,String errorMessage) {
		  this.merchantId = merchantId;
		  this.orderNo = orderNo;
		  this.respCode = respCode;
		  this.errorCode = errorCode;
		  this.errorMessage = errorMessage;
	}
   

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getOrderNo() {
		return orderNo;
	}



	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	


	public String getRespCode() {
		return respCode;
	}



	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}



	public String getRespMsg() {
		return respMsg;
	}



	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}



	public String getErrorCode() {
		return errorCode;
	}



	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public String getErrorMessage() {
		return errorMessage;
	}



	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}



	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
