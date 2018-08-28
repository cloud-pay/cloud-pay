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
import com.cloud.pay.common.entity.Channel;
import com.cloud.pay.common.service.ChannelService;

@Controller
@RequestMapping("/channel")
public class ChannelController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(ChannelController.class);
	
	@Autowired
	private ChannelService channelService;
	
	private String menuUrl = "channel/list";
	
	/**
	 * 渠道列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model, String channelCode, String channelName){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		model.addAttribute("channels", channelService.getchannelList(channelCode, channelName));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/channel/list";
	}
	
	/**
	 * 添加渠道
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"add", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Channel channel = new Channel();
			ParameterMap map = this.getParameterMap();
			channel.setChannelCode(map.getString("channelCode"));
			channel.setChannelName(map.getString("channelName"));
			channel.setChannelMerchantId(map.getString("channelMerchantId"));
			channel.setChannelType(Integer.parseInt(map.getString("channelType")));
			channel.setFeeType(Integer.parseInt(map.getString("feeType")));
			channel.setFee(new BigDecimal(map.getString("fee")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			channel.setCreator(userId);
			channel.setCreateTime(new Date());
			channel.setModifer(userId);
			channel.setModifyTime(new Date());
			channel.setId(Integer.parseInt(map.getString("id")));
			channelService.save(channel);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 编辑渠道
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Object edit(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Channel channel = new Channel();
			ParameterMap map = this.getParameterMap();
			channel.setChannelCode(map.getString("channelCode"));
			channel.setChannelName(map.getString("channelName"));
			channel.setChannelMerchantId(map.getString("channelMerchantId"));
			channel.setChannelType(Integer.parseInt(map.getString("channelType")));
			channel.setFeeType(Integer.parseInt(map.getString("feeType")));
			channel.setFee(new BigDecimal(map.getString("fee")));
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			channel.setModifer(userId);
			channel.setModifyTime(new Date());
			channel.setId(Integer.parseInt(map.getString("id")));
			channelService.update(channel);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
	/**
	 * 删除渠道
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public Object del(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"del", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			Integer id = Integer.parseInt(this.getParameterMap().getString("id"));
			channelService.del(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	
}
