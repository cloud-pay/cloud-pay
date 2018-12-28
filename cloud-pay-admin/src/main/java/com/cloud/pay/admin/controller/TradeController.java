package com.cloud.pay.admin.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.cloud.pay.trade.constant.MerchantRouteConstant;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.FeeStatDTO;
import com.cloud.pay.trade.dto.TradeRecordDTO;
import com.cloud.pay.trade.service.TradeService;

@Controller
@RequestMapping("/trade")
public class TradeController extends BaseController {
	
	private Logger log = LoggerFactory.getLogger(TradeController.class);

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
		log.info("汇总数据查询入参：{}", map);
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
			User user = ((User) this.getSession().getAttribute(Const.SESSION_USER));
			model.addAttribute("tradeStat", tradeService.tradeStat(merchantId, orgId, 
					startTime, endTime, user.getMerchantId(), user.getMerchantType()));
			model.addAttribute("loanTradeStat", tradeService.loanTradeStat(merchantId, orgId, 
					startTime, endTime, user.getMerchantId(), user.getMerchantType()));
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
		log.info("交易记录查询入参：{}", map);
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
			User user = ((User) this.getSession().getAttribute(Const.SESSION_USER));
			model.addAttribute("trades", tradeService.selectTradeList(merchantId, orgId, orderNo, batchNo, 
					loan, startTime, endTime, user.getMerchantId(), user.getMerchantType()));
		}
		return "page/trade/list";
	}
	
	/**
	 * 导出交易记录
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportList", method = RequestMethod.GET)
	public void exportList(HttpServletResponse res) {
		// 统计查询
		ParameterMap map = this.getParameterMap();
		log.info("导出交易记录入参：{}", map);
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
			User user = ((User) this.getSession().getAttribute(Const.SESSION_USER));
			List<TradeRecordDTO> trades = tradeService.selectTradeList(merchantId, orgId, orderNo, batchNo, 
					loan, startTime, endTime, user.getMerchantId(), user.getMerchantType());
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("订单");
	        String fileName = "订单"  + ".xls";
	        int rowNum = 1;
	        String[] headers = { "交易时间","所属机构","批次号","订单编号","商户编号","商户名称","交易状态","交易金额（元）","是否垫资"};
	        HSSFRow row = sheet.createRow(0);
	        for(int i=0;i<headers.length;i++){
	            HSSFCell cell = row.createCell(i);
	            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	            cell.setCellValue(text);
	        }
	        for (TradeRecordDTO dto : trades) {
	            HSSFRow row1 = sheet.createRow(rowNum);
	            row1.createCell(0).setCellValue(dto.getTradeTime());
	            row1.createCell(1).setCellValue(dto.getOrgName());
	            row1.createCell(2).setCellValue(dto.getBatchNo());
	            row1.createCell(3).setCellValue(dto.getOrderNo());
	            row1.createCell(4).setCellValue(dto.getMerchantCode());
	            row1.createCell(5).setCellValue(dto.getMerchantName());
	            if(dto.getStatus() == TradeConstant.STATUS_PROCESSING) {
	            	row1.createCell(6).setCellValue("处理中");
	            } else if(dto.getStatus() == TradeConstant.STATUS_SUCCESS) {
	            	row1.createCell(6).setCellValue("成功");
	            } else if(dto.getStatus() == TradeConstant.STATUS_FAIL) {
	            	row1.createCell(6).setCellValue("失败");
	            }
	            row1.createCell(7).setCellValue(dto.getTradeAmount().toString());
	            if(dto.getLoaning() == MerchantRouteConstant.LOANING_YES) {
	            	row1.createCell(8).setCellValue("垫资");
	            } else if(dto.getStatus() == MerchantRouteConstant.LOANING_NO) {
	            	row1.createCell(8).setCellValue("不垫资");
	            }
	            rowNum++;
	        }
	        res.setContentType("application/octet-stream");
	        try {
	        	res.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
				res.flushBuffer();
				workbook.write(res.getOutputStream());
			} catch (IOException e) {
				log.error("导出订单异常，{}", e);
			} finally {
				if(workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
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
		log.info("商户手续费统计查询入参：{}", map);
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
			User user = ((User) this.getSession().getAttribute(Const.SESSION_USER));
			model.addAttribute("merchantStats", tradeService.selectMerchantFeeStats(merchantId, orgId, 
					startTime, endTime, user.getMerchantId(), user.getMerchantType()));
		}
		return "page/trade/merchantFeeStat";
	}
	
	/**
	 * 导出商户手续费统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportMerchantFeeStat", method = RequestMethod.GET)
	public void exportMerchantFeeStat(HttpServletResponse res) {
		// 统计查询
		ParameterMap map = this.getParameterMap();
		log.info("导出商户手续费查询入参：{}", map);
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
			User user = ((User) this.getSession().getAttribute(Const.SESSION_USER));
			List<FeeStatDTO> fees = tradeService.selectMerchantFeeStats(merchantId, orgId, 
					startTime, endTime, user.getMerchantId(), user.getMerchantType());
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("商户手续费统计");
	        String fileName = "商户手续费统计.xls";
	        int rowNum = 1;
	        String[] headers = { "统计日期", "商户编号", "商户名称", "所属机构", "付款成功总笔数", "付款成功总金额(元)", "垫资总金额(元)", "商户手续费(元)"};
	        HSSFRow row = sheet.createRow(0);
	        for(int i=0;i<headers.length;i++){
	            HSSFCell cell = row.createCell(i);
	            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	            cell.setCellValue(text);
	        }
	        for (FeeStatDTO dto : fees) {
	            HSSFRow row1 = sheet.createRow(rowNum);
	            row1.createCell(0).setCellValue(dto.getStatDate());
	            row1.createCell(1).setCellValue(dto.getMerchantCode());
	            row1.createCell(2).setCellValue(dto.getMerchantName());
	            row1.createCell(3).setCellValue(dto.getOrgName());
	            row1.createCell(4).setCellValue(dto.getTotalCount());
	            row1.createCell(5).setCellValue(dto.getTotalAmount().toString());
	            row1.createCell(6).setCellValue(dto.getLoaningTotalAmount().toString());
	            row1.createCell(7).setCellValue(dto.getFeeAmount().toString());
	            rowNum++;
	        }
	        res.setHeader("content-type", "application/octet-stream");
	        res.setContentType("application/octet-stream");
	        try {
	        	res.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
				res.flushBuffer();
				workbook.write(res.getOutputStream());
			} catch (IOException e) {
				log.error("导出商户手续费异常，{}", e);
			} finally {
				if(workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
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
		log.info("机构手续费统计查询入参：{}", map);
		String org = map.getString("orgId");
		String createDateBegin = map.getString("createDateBegin");
		String createDateEnd = map.getString("createDateEnd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		Integer orgId = null;
		try {
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
		if (orgId != null || startTime != null || endTime != null) {
			User user = ((User) this.getSession().getAttribute(Const.SESSION_USER));
			model.addAttribute("orgStats", tradeService.selectOrgFeeStats(orgId, 
					startTime, endTime, user.getMerchantId(), user.getMerchantType()));
		}
		return "page/trade/orgFeeStat";
	}
	
	/**
	 * 导出机构手续费统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportOrgFeeStat", method = RequestMethod.GET)
	public void exportOrgFeeStat(HttpServletResponse res) {
		// 统计查询
		ParameterMap map = this.getParameterMap();
		log.info("导出机构手续费统计查询入参：{}", map);
		String org = map.getString("orgId");
		String createDateBegin = map.getString("createDateBegin");
		String createDateEnd = map.getString("createDateEnd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		Integer orgId = null;
		try {
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
		if (orgId != null || startTime != null || endTime != null) {
			User user = ((User) this.getSession().getAttribute(Const.SESSION_USER));
			List<FeeStatDTO> fees =  tradeService.selectOrgFeeStats(orgId, 
					startTime, endTime, user.getMerchantId(), user.getMerchantType());
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("机构手续费统计");
	        String fileName = "机构手续费统计"  + ".xls";
	        int rowNum = 1;
	        String[] headers = { "统计日期", "机构编号", "机构名称", "付款成功总笔数", "付款成功总金额(元)", "垫资总金额(元)", "商户手续费(元)"};
	        HSSFRow row = sheet.createRow(0);
	        for(int i=0;i<headers.length;i++){
	            HSSFCell cell = row.createCell(i);
	            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	            cell.setCellValue(text);
	        }
	        for (FeeStatDTO dto : fees) {
	            HSSFRow row1 = sheet.createRow(rowNum);
	            row1.createCell(0).setCellValue(dto.getStatDate());
	            row1.createCell(1).setCellValue(dto.getMerchantCode());
	            row1.createCell(2).setCellValue(dto.getMerchantName());
	            row1.createCell(3).setCellValue(dto.getTotalCount());
	            row1.createCell(4).setCellValue(dto.getTotalAmount().toString());
	            row1.createCell(5).setCellValue(dto.getLoaningTotalAmount().toString());
	            row1.createCell(6).setCellValue(dto.getFeeAmount().toString());
	            rowNum++;
	        }
	        res.setContentType("application/octet-stream");
	        try {
	        	res.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
				res.flushBuffer();
				workbook.write(res.getOutputStream());
			} catch (IOException e) {
				log.error("导出机构手续费异常，{}", e);
			} finally {
				if(workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
