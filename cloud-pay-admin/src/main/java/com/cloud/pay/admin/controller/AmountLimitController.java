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
import com.cloud.pay.trade.entity.AmountLimit;
import com.cloud.pay.trade.service.AmountLimitService;

@Controller
@RequestMapping("/amountLimit")
public class AmountLimitController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(AmountLimitController.class);
	
	@Autowired
	private AmountLimitService amountLimitService;
	
	private String menuUrl = "amountLimit/list";
	
	/**
	 * 限额列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model, Integer type, String orgName, String merchantName, Date startTime,
			Date endTime){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		model.addAttribute("amountLimits", amountLimitService.getAmountLimitList(type, orgName, merchantName, startTime, endTime));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/amountLimit/list";
	}
	
	/**
	 * 添加限额
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"add", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			AmountLimit amountLimit = new AmountLimit();
			ParameterMap map = this.getParameterMap();
			amountLimit.setMerchantId(Integer.parseInt(map.getString("merchantId")));
			amountLimit.setType(Integer.parseInt(map.getString("type")));
			amountLimit.setPeriod(Integer.parseInt(map.getString("period")));
			amountLimit.setAmountLimit(new BigDecimal(map.getString("amountLimit")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			amountLimit.setModifer(userId);
			amountLimit.setModifyTime(new Date());
			amountLimitService.save(amountLimit);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 编辑限额
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Object edit(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			AmountLimit amountLimit = new AmountLimit();
			ParameterMap map = this.getParameterMap();
			amountLimit.setMerchantId(Integer.parseInt(map.getString("merchantId")));
			amountLimit.setType(Integer.parseInt(map.getString("type")));
			amountLimit.setPeriod(Integer.parseInt(map.getString("period")));
			amountLimit.setAmountLimit(new BigDecimal(map.getString("amountLimit")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			amountLimit.setModifer(userId);
			amountLimit.setModifyTime(new Date());
			amountLimit.setId(Integer.parseInt(map.getString("id")));
			amountLimitService.update(amountLimit);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 删除限额
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public Object del(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"del", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Integer id = Integer.parseInt(this.getParameterMap().getString("id"));
			amountLimitService.del(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
}
