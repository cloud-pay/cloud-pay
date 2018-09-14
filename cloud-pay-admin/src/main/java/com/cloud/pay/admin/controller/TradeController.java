package com.cloud.pay.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloud.pay.admin.controller.base.BaseController;
import com.cloud.pay.admin.entity.Const;
import com.cloud.pay.admin.entity.ResponseModel;
import com.cloud.pay.admin.entity.ResultEnum;
import com.cloud.pay.admin.entity.User;
import com.cloud.pay.admin.util.Jurisdiction;
import com.cloud.pay.admin.util.ParameterMap;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.service.TradeService;

@Controller
@RequestMapping("/trade")
public class TradeController extends BaseController {

	@Autowired
	private TradeService tradeService;

	@Autowired
	private MerchantService merchantService;

	private String menuUrl = "trade/stat";

	/**
	 * 汇总数据查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/stat", method = RequestMethod.GET)
	public Object stat(Model model) {
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "query", this.getSession())) {
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		model.addAttribute("orgs", merchantService.getMerchantDTOs("org"));
		model.addAttribute("merchants", merchantService.getMerchantDTOs("merchant"));
		model.addAttribute("meid", ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		// 统计查询
		ParameterMap map = this.getParameterMap();
		String merchant = map.getString("merchantId");
		String org = map.getString("orgId");
		String createDateBegin = map.getString("createDateBegin");
		String createDateEnd = map.getString("createDateEnd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		Integer merchantId = null;
		Integer orgId = null;
		try {
			if (StringUtils.isNotBlank(merchant)) {
				merchantId = Integer.parseInt(merchant);
			}
			if (StringUtils.isNotBlank(org)) {
				orgId = Integer.parseInt(org);
			}
			if (StringUtils.isNotBlank(createDateBegin)) {
				startTime = sdf.parse(createDateBegin);
			}
			if (StringUtils.isNotBlank(createDateEnd)) {
				endTime = sdf.parse(createDateEnd);
			}
		} catch (Exception e) {
		}
		if (merchantId != null || orgId != null || startTime != null || endTime != null) {
			model.addAttribute("tradeStat", tradeService.tradeStat(merchantId, orgId, startTime, endTime));
			model.addAttribute("loanTradeStat", tradeService.loanTradeStat(merchantId, orgId, startTime, endTime));
		}
		return "page/trade/stat";
	}
	
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
		model.addAttribute("orgs", merchantService.getMerchantDTOs("org"));
		model.addAttribute("merchants", merchantService.getMerchantDTOs("merchant"));
		model.addAttribute("meid", ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		// 统计查询
		ParameterMap map = this.getParameterMap();
		String merchant = map.getString("merchantId");
		String org = map.getString("orgId");
		String createDateBegin = map.getString("createDateBegin");
		String createDateEnd = map.getString("createDateEnd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String orderNo = map.getString("orderNo");
		String batchNo = map.getString("batchNo");
		String loaning = map.getString("loaning");
		Date startTime = null;
		Date endTime = null;
		Integer merchantId = null;
		Integer orgId = null;
		Integer loan = null;
		try {
			if (StringUtils.isNotBlank(merchant)) {
				merchantId = Integer.parseInt(merchant);
			}
			if (StringUtils.isNotBlank(org)) {
				orgId = Integer.parseInt(org);
			}
			if (StringUtils.isNotBlank(loaning)) {
				loan = Integer.parseInt(loaning);
			}
			if (StringUtils.isNotBlank(createDateBegin)) {
				startTime = sdf.parse(createDateBegin);
			}
			if (StringUtils.isNotBlank(createDateEnd)) {
				endTime = sdf.parse(createDateEnd);
			}
		} catch (Exception e) {
		}
		if (merchantId != null || orgId != null || startTime != null || endTime != null) {
			model.addAttribute("trades", tradeService.selectTradeList(merchantId, orgId, orderNo, batchNo, loan, startTime, endTime));
		}
		return "page/trade/list";
	}
	
	/**
	 * 商户手续费统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/merchantFeeStat", method = RequestMethod.GET)
	public Object merchantFeeStat(Model model) {
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "query", this.getSession())) {
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		model.addAttribute("orgs", merchantService.getMerchantDTOs("org"));
		model.addAttribute("merchants", merchantService.getMerchantDTOs("merchant"));
		model.addAttribute("meid", ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		// 统计查询
		ParameterMap map = this.getParameterMap();
		String merchant = map.getString("merchantId");
		String org = map.getString("orgId");
		String createDateBegin = map.getString("createDateBegin");
		String createDateEnd = map.getString("createDateEnd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		Integer merchantId = null;
		Integer orgId = null;
		try {
			if (StringUtils.isNotBlank(merchant)) {
				merchantId = Integer.parseInt(merchant);
			}
			if (StringUtils.isNotBlank(org)) {
				orgId = Integer.parseInt(org);
			}
			if (StringUtils.isNotBlank(createDateBegin)) {
				startTime = sdf.parse(createDateBegin);
			}
			if (StringUtils.isNotBlank(createDateEnd)) {
				endTime = sdf.parse(createDateEnd);
			}
		} catch (Exception e) {
		}
		if (merchantId != null || orgId != null || startTime != null || endTime != null) {
			model.addAttribute("tradeStat", tradeService.tradeStat(merchantId, orgId, startTime, endTime));
			model.addAttribute("loanTradeStat", tradeService.loanTradeStat(merchantId, orgId, startTime, endTime));
		}
		return "page/trade/merchantFeeStat";
	}

	/**
	 * 机构手续费统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/orgFeeStat", method = RequestMethod.GET)
	public Object orgFeeStat(Model model) {
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "query", this.getSession())) {
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		model.addAttribute("orgs", merchantService.getMerchantDTOs("org"));
		model.addAttribute("merchants", merchantService.getMerchantDTOs("merchant"));
		model.addAttribute("meid", ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		// 统计查询
		ParameterMap map = this.getParameterMap();
		String merchant = map.getString("merchantId");
		String org = map.getString("orgId");
		String createDateBegin = map.getString("createDateBegin");
		String createDateEnd = map.getString("createDateEnd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		Integer merchantId = null;
		Integer orgId = null;
		try {
			if (StringUtils.isNotBlank(merchant)) {
				merchantId = Integer.parseInt(merchant);
			}
			if (StringUtils.isNotBlank(org)) {
				orgId = Integer.parseInt(org);
			}
			if (StringUtils.isNotBlank(createDateBegin)) {
				startTime = sdf.parse(createDateBegin);
			}
			if (StringUtils.isNotBlank(createDateEnd)) {
				endTime = sdf.parse(createDateEnd);
			}
		} catch (Exception e) {
		}
		if (merchantId != null || orgId != null || startTime != null || endTime != null) {
			model.addAttribute("tradeStat", tradeService.tradeStat(merchantId, orgId, startTime, endTime));
			model.addAttribute("loanTradeStat", tradeService.loanTradeStat(merchantId, orgId, startTime, endTime));
		}
		return "page/trade/orgFeeStat";
	}
}
