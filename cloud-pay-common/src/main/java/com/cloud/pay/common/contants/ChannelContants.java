package com.cloud.pay.common.contants;

public interface ChannelContants {
      
	public static final String CHANNEL_RESP_CODE_SUCCESS = "0";
	
	public static final String CHANNEL_RESP_CODE_FAIL = "1";
	
	public static final String CHANNEL_RESP_CODE_UNKNOWN = "99";
	
	//交易状态-成功
	public static final Integer CHANNEL_RETURN_STATUS_SUCCESS = 0;
	
	//交易状态-失败
	public static final Integer CHANNEL_RETURN_STATUS_FAIL = 1;
	
	//交易状态-未知
	public static final Integer CHANNEL_RETURN_STATUS_UNKNOWN = 99;
	
	// 渤海单笔实时代付请求头信息
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCS = "SCSP";
	
	// 渤海单笔实时代付响应头信息
	public static final String CHANNEL_BOHAI_RES_HEADER_SCS = "SCSPRes";
	
	// 渤海单笔代收付交易查询请求头信息
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCTQ = "SCTQ";
	
	// 渤海单笔代收付交易查询响应头信息
	public static final String CHANNEL_BOHAI_RES_HEADER_SCTQ = "SCTQRes";
	
	// 渤海单笔银联代付交易请求
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCUR = "SCUP";
	
	// 渤海单笔银联代付交易响应
	public static final String CHANNEL_BOHAI_RES_HEADER_SCUR = "SCUPRes";
	
	// 渤海单笔大额代付
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCHP = "SCHP";
	
	//批量代付响应
	public static final String CHANNEL_BOHAI_RES_HEADER_SCBP = "SCBPRes";
	
	//批量代付请求
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCBP = "SCBP";
	
	//批量代付查询请求
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCBR = "SCBR";
	
	//批量代付查询响应
	public static final String CHANNEL_BOHAI_RES_HEADER_SCBR = "SCBRRes";
	
	//批量代付单笔查询请求
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCBS = "SCBS";
	
	//批量代付单笔查询响应
	public static final String CHANNEL_BOHAI_RES_HEADER_SCBS = "SCBSRes";
	
	//批量代付重新重发
	public static final String CHANNEL_BOHAI_REQ_HEADER_SCBT = "SCBT";
	
	//批量代付重新重发
	public static final String CHANNEL_BOHAI_RES_HEADER_SCBT = "SCBTRes";
}
