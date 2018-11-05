package com.cloud.pay.admin.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.cloud.pay.admin.entity.Const;
import com.cloud.pay.admin.entity.ResponseModel;
import com.cloud.pay.admin.entity.ResultEnum;
import com.cloud.pay.admin.entity.User;
import com.cloud.pay.admin.util.Jurisdiction;
import com.cloud.pay.admin.util.ParameterMap;
import com.cloud.pay.common.exception.CloudPayException;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.recon.entity.FillRecord;
import com.cloud.pay.recon.service.FillRecordService;

@Controller
@RequestMapping("/fillRecord")
public class FillRecordController extends BaseController {
     
	private Logger log = LoggerFactory.getLogger(FillRecordController.class);
	
	private String menuUrl = "/fillRecord/list";
	
	@Autowired
	private FillRecordService fillRecordService;
	
	@Autowired
	private MerchantService merchantService;
	
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model,String orgCode,String orgName,String startDate,String endDate) {
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		try {
			if(StringUtils.isNotBlank(startDate)) {
				startTime = sdf.parse(startDate+" 00:00:00");
			}
			if(StringUtils.isNotBlank(endDate)) {
				endTime = sdf.parse(endDate + " 23:59:59");
			}
		} catch(Exception e) {
		}
		model.addAttribute("orgCode", orgCode);
		model.addAttribute("orgName", orgName);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("fillRecords",fillRecordService.selectListByParam(orgCode,orgName,startTime,endTime));
		model.addAttribute("orgs", merchantService.selectMerchantByType(3));
		return "page/fill/list";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		//校验权限
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add",this.getSession())){
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		} 
		try {
		   ParameterMap map = this.getParameterMap();
		   String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUserId();
		   FillRecord fillRecord = new FillRecord();
		   fillRecord.setOrgId(Integer.parseInt(map.getString("orgId")));
		   fillRecord.setFillAmount(new BigDecimal(map.getString("fillAmount")));
		   fillRecord.setStatus(1);
		   fillRecord.setRmk(StringUtils.isNotBlank(map.getString("rmk"))?map.getString("rmk"):"");
		   fillRecord.setUpdatorId(Integer.parseInt(userId));
		   fillRecord.setUpdateTime(new Date());
		   fillRecordService.insert(fillRecord);
		}catch(Exception e) {
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	/**
	 * 冲正
	 * @return
	 */
	@RequestMapping(value="/reversal",method=RequestMethod.POST)
	@ResponseBody
	public Object reversal() {
		Integer id = Integer.parseInt(this.getParameterMap().getString("id"));
		try {
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUserId();
			fillRecordService.reversal(id, Integer.parseInt(userId));
		}catch(Exception e) {
			log.error("error:{}",e);
			if(e instanceof CloudPayException) {
				return ResponseModel.getModel(e.getMessage(), "failed", null); 
			}
			return ResponseModel.getModel("冲正失败", "failed", null); 
		}
		return ResponseModel.getModel("ok", "success", null);
	}
}
