package com.cloud.pay.channel.vo;

/**
 * 代付接口响应实体
 * @author wangy
 *
 */
public class PayTradeResVO extends BaseTradeResVO{
   
	private static final long serialVersionUID = 6964116147301941371L;
	
	private String accountDate; //银行会计日期
	
	private Integer channelId; //渠道编码
	
	private Integer status; //0-成功，1-失败，99-未知状态
	
	public PayTradeResVO(String errorCode,String errorMessage) {
	   	super(errorCode, errorMessage);
	}
	
	public PayTradeResVO(String respCode,String errorCode,String errorMessage) {
		super(errorCode, errorMessage);
		this.respCode = respCode;
	}
	
	public PayTradeResVO(Integer merchantId, String orderNo, String respMsg) {
		super(merchantId, orderNo, respMsg);
	}
	
	public PayTradeResVO(Integer merchantId, String orderNo, String respMsg,String accountDate) {
		super(merchantId, orderNo, respMsg);
		this.accountDate = accountDate;
	}
   
	public PayTradeResVO(Integer merchantId,String orderNo,String respCode,String errorCode,String errorMessage) {
		super(merchantId, orderNo, respCode, errorCode, errorMessage);
	}

	public String getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
