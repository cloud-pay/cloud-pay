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
import com.cloud.pay.common.service.ChannelService;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.constant.MerchantRouteConstant;
import com.cloud.pay.trade.entity.MerchantRouteConf;
import com.cloud.pay.trade.service.MerchantRouteConfService;

@Controller
@RequestMapping("/merchantRouteConf")
public class MerchantRouteConfController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(MerchantRouteConfController.class);
	
	@Autowired
	private MerchantRouteConfService merchantRouteConfService;
	
	private String menuUrl = "merchantRouteConf/list";
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private ChannelService channelService;
	
	/**
	 * 商戶路由配置列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model, Integer type, Integer status, String merchantName, String createDateBegin,
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
		model.addAttribute("merchantRoutes", merchantRouteConfService.getmerchantRouteConfList(type, status, merchantName, startTime, endTime));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		model.addAttribute("orgs", merchantService.getMerchantDTOs("org"));
		model.addAttribute("merchants", merchantService.getMerchantDTOs("merchant"));
		model.addAttribute("loans", merchantService.selectByMerchantType(3));
		model.addAttribute("channels", channelService.getchannelList(null, null));
		return "page/merchantRouteConf/list";
	}
	
	/**
	 * 添加商戶路由配置
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"add", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			MerchantRouteConf conf = new MerchantRouteConf();
			ParameterMap map = this.getParameterMap();
			conf.setType(Integer.parseInt(map.getString("type")));
			conf.setMerchantId(Integer.parseInt(map.getString("merchantId")));
			conf.setChannelId(Integer.parseInt(map.getString("channelId")));
			conf.setLoaning(Integer.parseInt(map.getString("loaning")));
			conf.setLoaningOrgId(Integer.parseInt(map.getString("loaningOrgId")));
			conf.setLoaningAmount(new BigDecimal(map.getString("loaningAmount")));
			conf.setStatus(MerchantRouteConstant.NORMAL);
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			conf.setCreator(userId);
			conf.setCreateTime(new Date());
			conf.setModifer(userId);
			conf.setModifyTime(new Date());
			merchantRouteConfService.save(conf);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 编辑商戶路由配置
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Object edit(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			MerchantRouteConf conf = new MerchantRouteConf();
			ParameterMap map = this.getParameterMap();
			conf.setType(Integer.parseInt(map.getString("type")));
			conf.setMerchantId(Integer.parseInt(map.getString("merchantId")));
			conf.setChannelId(Integer.parseInt(map.getString("channelId")));
			conf.setLoaning(Integer.parseInt(map.getString("loaning")));
			conf.setLoaningOrgId(Integer.parseInt(map.getString("loaningOrgId")));
			conf.setLoaningAmount(new BigDecimal(map.getString("loaningAmount")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			conf.setModifer(userId);
			conf.setModifyTime(new Date());
			conf.setId(Integer.parseInt(map.getString("id")));
			merchantRouteConfService.update(conf);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 删除商戶路由配置
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public Object del(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"del", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Integer id = Integer.parseInt(this.getParameterMap().getString("id"));
			merchantRouteConfService.del(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
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
			MerchantRouteConf conf = new MerchantRouteConf();
			ParameterMap map = this.getParameterMap();
			conf.setStatus(Integer.parseInt(map.getString("status")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			conf.setModifer(userId);
			conf.setModifyTime(new Date());
			conf.setId(Integer.parseInt(map.getString("id")));
			merchantRouteConfService.update(conf);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
}
