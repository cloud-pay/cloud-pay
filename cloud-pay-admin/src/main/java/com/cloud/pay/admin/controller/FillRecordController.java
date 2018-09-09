package com.cloud.pay.admin.controller;

import java.math.BigDecimal;
import java.util.Date;

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
import com.cloud.pay.recon.entity.FillRecord;
import com.cloud.pay.recon.service.FillRecordService;

@Controller
@RequestMapping("/fillRecord")
public class FillRecordController extends BaseController {
     
	private Logger log = LoggerFactory.getLogger(FillRecordController.class);
	
	private String menuUrl = "/fillRecord/list";
	
	@Autowired
	private FillRecordService fillRecordService;
	
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model) {
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		model.addAttribute("fillRecords",fillRecordService.selectListByParam());
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
		   fillRecord.setRmk(map.getString("rmk"));
		   fillRecord.setUpdatorId(Integer.parseInt(userId));
		   fillRecord.setUpdateTime(new Date());
		}catch(Exception e) {
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
}
