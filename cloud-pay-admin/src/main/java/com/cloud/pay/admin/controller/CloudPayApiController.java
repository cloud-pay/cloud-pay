package com.cloud.pay.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloudPayApiController {
  
	private Logger log = LoggerFactory.getLogger(CloudPayApiController.class);
	  
	@RequestMapping(value = "/api", method = RequestMethod.POST)
	public Object handleTrade(@RequestBody String requestContent) {
		 log.info("收到API请求，请求参数：{}",requestContent);
		 
		 
		 log.info("API接口响应参数");
		 return null;
	}
}
