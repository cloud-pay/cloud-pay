package com.cloud.pay.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.pay.admin.controller.base.BaseController;
import com.cloud.pay.admin.entity.ResponseModel;
import com.cloud.pay.admin.entity.ResultEnum;
import com.cloud.pay.admin.service.IMenuService;
import com.cloud.pay.admin.util.Jurisdiction;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{
	
	private String qxurl = "menu/list";
	
	@Autowired
	private IMenuService menuService;
	
	@RequestMapping("/list")
	public String list(Model model){
		model.addAttribute("menus",menuService.getAllParentMenuList());
		return "page/menu/list";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "add",this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);} //校验权限
		return menuService.addMenu(this.getParameterMap(),this.getSession());
	}
	@RequestMapping(value="/del/{menu_id}",method=RequestMethod.GET)
	@ResponseBody
	public Object del(@PathVariable("menu_id") String menuId){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "del",this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);} //校验权限
		return menuService.delMenu(menuId);
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Object edit(){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "edit",this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);} //校验权限
		return menuService.editMenu(this.getParameterMap());
	}
	@RequestMapping(value="/query/{menu_id}",method=RequestMethod.GET)
	@ResponseBody
	public Object find(@PathVariable("menu_id") String menuId){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "query",this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);} //校验权限
		return menuService.findMenu(menuId);
	}
	
	@RequestMapping("/getSubMenu")
	@ResponseBody
	public Object getSubMenu(){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "query",this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);} //校验权限
		return menuService.getSubMenuList(this.getParameterMap());
	}
}
