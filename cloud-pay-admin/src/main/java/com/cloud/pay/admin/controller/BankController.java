package com.cloud.pay.admin.controller;

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
import com.cloud.pay.common.entity.Bank;
import com.cloud.pay.common.service.BankService;

@Controller
@RequestMapping("/bank")
public class BankController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(BankController.class);
	
	@Autowired
	private BankService bankService;
	
	private String menuUrl = "bank/list";
	
	/**
	 * 联行号列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model, String bankCode, String bankName){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		model.addAttribute("banks", bankService.getBankList(bankCode, bankName));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/bank/list";
	}
	
	/**
	 * 添加联行号
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"add", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Bank bank = new Bank();
			ParameterMap map = this.getParameterMap();
			bank.setBankCode(map.getString("bankCode"));
			bank.setBankName(map.getString("bankName"));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			bank.setModifer(userId);
			bank.setModifyTime(new Date());
			bankService.save(bank);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 编辑联行号
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Object edit(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Bank bank = new Bank();
			ParameterMap map = this.getParameterMap();
			bank.setBankCode(map.getString("bankCode"));
			bank.setBankName(map.getString("bankName"));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			bank.setModifer(userId);
			bank.setModifyTime(new Date());
			bank.setId(Integer.parseInt(map.getString("id")));
			bankService.update(bank);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 删除用户
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public Object del(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"del", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Integer id = Integer.parseInt(this.getParameterMap().getString("id"));
			bankService.del(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
}
