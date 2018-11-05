package com.cloud.pay.admin.controller;

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

import com.alibaba.fastjson.JSON;
import com.cloud.pay.admin.controller.base.BaseController;
import com.cloud.pay.admin.entity.Const;
import com.cloud.pay.admin.entity.ResponseModel;
import com.cloud.pay.admin.entity.ResultEnum;
import com.cloud.pay.admin.entity.User;
import com.cloud.pay.admin.util.Jurisdiction;
import com.cloud.pay.admin.util.ParameterMap;
import com.cloud.pay.common.service.BankService;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;

@Controller
@RequestMapping("/merchant")
public class MerchantController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(MerchantController.class);
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private BankService bankService;
	
	private String menuUrl = "amountLimit/list";
	
	/**
	 * 商戶列表
	 * @return
	 */
	@RequestMapping(value="/dtos",method=RequestMethod.GET)
	@ResponseBody
	public String dtos(Model model, String type){
		return JSON.toJSONString(merchantService.getMerchantDTOs(type));
	}
	
	/**
	 * 商戶列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model, Integer orgId, String code, String name, String createDateBegin,
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
		model.addAttribute("merchants", merchantService.getMerchantList(orgId, code, name, startTime, endTime));
		model.addAttribute("orgs", merchantService.getMerchantDTOs("org"));
		model.addAttribute("banks", bankService.getBankList(null, null));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/merchant/merchantList";
	}
	
	/**
	 * 冻结/解冻商戶路由配置
	 * @return
	 */
	@RequestMapping(value="/updateStatus",method=RequestMethod.POST)
	@ResponseBody
	public Object updateStatus(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"del", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			MerchantBaseInfo baseInfo = new MerchantBaseInfo();
			ParameterMap map = this.getParameterMap();
			baseInfo.setStatus(Integer.parseInt(map.getString("status")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			baseInfo.setModifer(userId);
			baseInfo.setModifyTime(new Date());
			baseInfo.setId(Integer.parseInt(map.getString("id")));
			merchantService.update(baseInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	/**
	 * 获取商戶
	 * @return
	 */
	@RequestMapping(value="/get",method=RequestMethod.GET)
	@ResponseBody
	public Object get(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			ParameterMap map = this.getParameterMap();
			Integer id = Integer.parseInt(map.getString("id"));
			return ResponseModel.getModel("ok", "success", merchantService.select(id));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
	}
	
	/**
	 * 获取商戶秘钥
	 * @return
	 */
	@RequestMapping(value="/getMerchantSecret",method=RequestMethod.GET)
	@ResponseBody
	public Object getMerchantSecret(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			ParameterMap map = this.getParameterMap();
			Integer id = Integer.parseInt(map.getString("id"));
			return ResponseModel.getModel("ok", "success", merchantService.selectByMerchantId(id));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
	}
	
	/**
	 * 获取商戶通道号
	 * @return
	 */
	@RequestMapping(value="/getMerchantChannel",method=RequestMethod.GET)
	@ResponseBody
	public Object getMerchantChannel(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			ParameterMap map = this.getParameterMap();
			Integer id = Integer.parseInt(map.getString("id"));
			return ResponseModel.getModel("ok", "success", merchantService.selectChennels(id));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
	}
}
