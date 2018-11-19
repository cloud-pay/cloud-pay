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
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.constant.AmountLimitConstant;
import com.cloud.pay.trade.entity.AmountLimit;
import com.cloud.pay.trade.service.AmountLimitService;

@Controller
@RequestMapping("/amountLimit")
public class AmountLimitController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(AmountLimitController.class);
	
	@Autowired
	private AmountLimitService amountLimitService;
	
	@Autowired
	private MerchantService merchantService;
	
	private String menuUrl = "amountLimit/list";
	
	/**
	 * 限额列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model, Integer type, String orgName, String merchantName, String createDateBegin,
			String createDateEnd){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		try {
			if(StringUtils.isNotBlank(createDateBegin)) {
				startTime = sdf.parse(createDateBegin);
			}
			if(StringUtils.isNotBlank(createDateEnd)) {
				endTime = sdf.parse(createDateEnd);
			}
		} catch(Exception e) {
		}
		model.addAttribute("amountLimits", amountLimitService.getAmountLimitList(type, orgName, merchantName, startTime, endTime));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		model.addAttribute("merchants", merchantService.getMerchantDTOs("merchant"));
		model.addAttribute("orgs", merchantService.getMerchantDTOs("org"));
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
			amountLimit.setType(Integer.parseInt(map.getString("type")));
			if(AmountLimitConstant.PER_LIMIT != amountLimit.getType()) {
				amountLimit.setMerchantId(Integer.parseInt(map.getString("merchantId")));
				amountLimit.setPeriod(Integer.parseInt(map.getString("period")));
			}
			amountLimit.setAmountLimit(new BigDecimal(map.getString("amountLimit")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			amountLimit.setModifer(userId);
			amountLimit.setModifyTime(new Date());
			amountLimitService.save(amountLimit);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
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
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
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
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
}
