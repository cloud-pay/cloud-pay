package com.cloud.pay.trade.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantRouteDTO {

	private Integer id;
	
	private Integer type;

    private Integer channelId;
    
    private String channelName;

    private Integer merchantId;
    
    private String merchantName;

    private Integer loaning;

    private Integer loaningOrgId;
    
    private String loaningOrgName;

    private BigDecimal loaningAmount;

    private Integer status;

    private String modifer;

    private Date modifyTime;
}
