package com.cloud.pay.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.pay.admin.controller.base.BaseController;
import com.cloud.pay.admin.entity.Const;
import com.cloud.pay.admin.entity.ResponseModel;
import com.cloud.pay.admin.entity.ResultEnum;
import com.cloud.pay.admin.entity.User;
import com.cloud.pay.admin.service.IUserService;
import com.cloud.pay.admin.util.Jurisdiction;
import com.cloud.pay.merchant.entity.UserMerchant;
import com.cloud.pay.merchant.service.MerchantService;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private MerchantService merchantService;
	
	private String menuUrl = "user/list";
	
	/**
	 * 用户列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object login(Model model){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		model.addAttribute("users", userService.getUserList());
		model.addAttribute("merchants", merchantService.getMerchantDTOs(null));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/user/list";
	}
	
	/**
	 * 获取用户角色
	 * @return
	 */
	@RequestMapping(value="/getRole",method=RequestMethod.GET)
	@ResponseBody
	public Object userRole(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		return userService.getRole(this.getParameterMap());
	}
	
	/**
	 * 添加用户
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"add", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		return userService.add(this.getParameterMap());
	}
	
	
	/**
	 * 编辑用户
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Object edit(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		return userService.edit(this.getParameterMap());
	}
	
	/**
	 * 编辑用户
	 * @return
	 */
	@RequestMapping(value="/editRole",method=RequestMethod.POST)
	@ResponseBody
	public Object editRole(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		return userService.editRole(this.getParameterMap());
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public Object del(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"del", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		return userService.del(this.getParameterMap());
	}
	
	/**
	 * 用户关联商户
	 * @return
	 */
	@RequestMapping(value="/userRel",method=RequestMethod.POST)
	@ResponseBody
	public Object userRel(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"del", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Integer userId = Integer.parseInt(this.getParameterMap().getString("user_id"));
			Integer merchantId = Integer.parseInt(this.getParameterMap().getString("merchantId"));
			UserMerchant userMerchant = new UserMerchant();
			userMerchant.setMerchantId(merchantId);
			userMerchant.setUserId(userId);
			merchantService.saveUserMerchant(userMerchant);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			log.error("error:"+e.getMessage(), e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
}
