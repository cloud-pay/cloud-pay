package com.cloud.pay.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
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
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.entity.PrepayTrade;
import com.cloud.pay.trade.service.PrepayTradeService;

/**
 * 预缴户交易
 * @author kftpay-core
 *
 */
@Controller
@RequestMapping("/prepayTrade")
public class PrepayTradeController extends BaseController {

	@Autowired
	private PrepayTradeService tradeService;

	@Autowired
	private MerchantService merchantService;

	private String menuUrl = "prepayTrade/list";

	
	/**
	 * 交易记录查询
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Object list(Model model) {
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "query", this.getSession())) {
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		model.addAttribute("merchants", merchantService.getMerchantDTOs("merchant"));
		model.addAttribute("meid", ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		// 统计查询
		ParameterMap map = this.getParameterMap();
		String merchant = map.getString("merchantId");
		String createDateBegin = map.getString("createDateBegin");
		String createDateEnd = map.getString("createDateEnd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		Integer merchantId = null;
		try {
			if (StringUtils.isNotBlank(merchant)) {
				merchantId = Integer.parseInt(merchant);
			}
			if (StringUtils.isNotBlank(createDateBegin)) {
				startTime = sdf.parse(createDateBegin);
			}
			if (StringUtils.isNotBlank(createDateEnd)) {
				endTime = sdf.parse(createDateEnd);
			}
		} catch (Exception e) {
		}
		model.addAttribute("trades", tradeService.selectTradeList(merchantId, startTime, endTime));
		return "page/prepayTrade/list";
	}
	
	/**
	 * 保存预缴户交易
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"add", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			ParameterMap map = this.getParameterMap();
			String trade = map.getString("trade");
			PrepayTrade prepayTrade = JSON.parseObject(trade, PrepayTrade.class);
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			prepayTrade.setCreator(userId);
			prepayTrade.setCreateTime(new Date());
			tradeService.saveTrade(prepayTrade);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
}
