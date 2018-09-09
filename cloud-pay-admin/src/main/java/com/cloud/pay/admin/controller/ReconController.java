package com.cloud.pay.admin.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.pay.admin.controller.base.BaseController;
import com.cloud.pay.admin.entity.ResponseModel;
import com.cloud.pay.admin.entity.ResultEnum;
import com.cloud.pay.admin.util.DateUtil;
import com.cloud.pay.admin.util.Jurisdiction;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.recon.service.ReconExceptionService;
import com.cloud.pay.recon.service.ReconService;

@Controller
@RequestMapping("/recon")
public class ReconController extends BaseController {
  
	private Logger log = LoggerFactory.getLogger(ReconController.class);
	
	private String menuUrl = "/recon/list";
	
	@Autowired
	private ReconService reconService;
	
	@Autowired
	private ReconExceptionService reconExceptionService;
	
	/**
	 * 对账列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model,Integer reconStatus,String channelName,String tradeDate) {
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		Date date = null;
		if(StringUtils.isNotBlank(tradeDate)) {
			date = DateUtil.fomatDate(tradeDate);
		}
		model.addAttribute("recons",reconService.queryReconList(reconStatus, channelName, date));
		return "page/recon/list";
	}
	
	/**
	 * 异常数据
	 * @param model
	 * @param reconId
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value="/exceptionList",method=RequestMethod.GET)
	public Object exceptionList(Model model,Integer reconId,Integer channelId,String orderNo,Integer exceptionType) {
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		model.addAttribute("reconId", reconId);
		model.addAttribute("channelId", channelId);
		model.addAttribute("orderNo", orderNo);
		model.addAttribute("exceptionType", exceptionType);
		model.addAttribute("exceptionList", reconExceptionService.selectListByParam(channelId, reconId,orderNo,exceptionType));
		return "page/recon/exceptionList";
	}
	
	/**
	 * 初始化对账数据
	 * @return
	 */
	@RequestMapping(value="/init",method=RequestMethod.GET)
	@ResponseBody
	public Object initRecon() {
		try {
		   reconService.initRecon(null);
		}catch(Exception e) {
			log.error("error:{}",e);
			if(e instanceof CloudPayException){
				return ResponseModel.getModel(e.getMessage(), "failed", null);
			}
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	} 
	
	@RequestMapping(value="/recon",method=RequestMethod.POST)
	@ResponseBody
	public Object recon() {
		Integer id = Integer.parseInt(this.getParameterMap().getString("id"));
		try {
			reconService.recon(id);
		}catch(Exception e) {
			log.error("error:{}",e);
			if(e instanceof CloudPayException) {
				return ResponseModel.getModel(e.getMessage(), "failed", null); 
			}
			return ResponseModel.getModel("提交失败", "failed", null); 
		}
		return ResponseModel.getModel("ok", "success", null);
	}
}
