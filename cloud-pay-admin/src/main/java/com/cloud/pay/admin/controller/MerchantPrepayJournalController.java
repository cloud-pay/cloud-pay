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
import com.cloud.pay.merchant.service.MerchantPrepayJournalService;
import com.cloud.pay.merchant.service.MerchantService;

/**
 * 预缴户交易
 * @author dbnaxlc
 *
 */
@Controller
@RequestMapping("/journal")
public class MerchantPrepayJournalController extends BaseController {

	@Autowired
	private MerchantPrepayJournalService merchantPrepayJournalService;

	@Autowired
	private MerchantService merchantService;

	private String menuUrl = "journal/list";

	
	/**
	 * 流水记录查询
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Object list(Model model) {
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "query", this.getSession())) {
			return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);
		}
		model.addAttribute("merchants", merchantService.getMerchantDTOs(null));
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
		model.addAttribute("journals", merchantPrepayJournalService.selectList(merchantId, startTime, endTime));
		return "page/journal/list";
	}
	
}
