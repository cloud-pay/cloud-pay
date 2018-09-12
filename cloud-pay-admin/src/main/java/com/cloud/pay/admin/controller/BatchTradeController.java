package com.cloud.pay.admin.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.trade.entity.BatchTrade;
import com.cloud.pay.trade.service.BatchTradeService;
import com.cloud.pay.trade.service.TradeService;

@Controller
@RequestMapping("/batchTrade")
public class BatchTradeController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(BatchTradeController.class);
	
	@Autowired
	private BatchTradeService batchTradeService;
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private MerchantService merchantService;
	
	private String menuUrl = "batchTrade/list";
	
	private static final String SEQ_OFFSET = "00000000";
	private AtomicInteger seq = new AtomicInteger(0);
	
	/**
	 * 手工代付
	 * @return
	 */
	@RequestMapping(value="/handPay",method=RequestMethod.GET)
	public Object handPay(Model model){
		if(!Jurisdiction.buttonJurisdiction("batchTrade/handPay","query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		model.addAttribute("merchants", merchantService.getMerchantDTOs(null));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/batchTrade/handPay";
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest req, HttpServletResponse res) {
		req.getContextPath();
      res.setHeader("content-type", "application/octet-stream");
      res.setContentType("application/octet-stream");
      res.setHeader("Content-Disposition", "attachment;filename=" + "pay.xls");
      byte[] buff = new byte[1024];
      BufferedInputStream bis = null;
      OutputStream os = null;
      try {
        os = res.getOutputStream();
        bis = new BufferedInputStream(new FileInputStream(new File("G:\\git\\cloud-pay\\cloud-pay-admin\\src\\main\\resources\\templates\\page\\batchTrade\\代付模板.xls")));
        int i = bis.read(buff);
        while (i != -1) {
          os.write(buff, 0, buff.length);
          os.flush();
          i = bis.read(buff);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (bis != null) {
          try {
            bis.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
	
	/**
	 * 上传
	 * @return
	 */
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	public Object upload(){
		if(!Jurisdiction.buttonJurisdiction("batchTrade/handPay","add", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			BatchTrade batchTrade = new BatchTrade();
			ParameterMap map = this.getParameterMap();
			Integer payerMerchantId = Integer.parseInt(map.getString("payerMerchantId"));
			batchTrade.setPayerMerchantId(payerMerchantId);
			batchTrade.setBatchNo(getBatchNo());
			batchTrade.setStatus(1);
			batchTrade.setTradeTime(new Date());
			String payFilePath = map.getString("payFilePath");
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			batchTrade.setCreator(userId);
			batchTrade.setCreateTime(new Date());
			String returnInfo = batchTradeService.upload(batchTrade, payFilePath.replace("data:application/vnd.ms-excel;base64,", ""));
			if(StringUtils.isNotBlank(returnInfo)) {
				return ResponseModel.getModel("提交失败:" + returnInfo, "failed", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
	private String getBatchNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String date = sdf.format(new Date());
		final int value = seq.incrementAndGet();
		String temp = (SEQ_OFFSET + String.valueOf(value));
		temp = temp.substring(temp.length() - SEQ_OFFSET.length());
		return date + temp;
	}
	
	/**
	 * 批量交易列表
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(Model model, Integer status, String createDateBegin,
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
		model.addAttribute("batchTrades", batchTradeService.getBatchTradeList(status, startTime, endTime));
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/batchTrade/list";
	}
	
	/**
	 * 审核列表
	 * @return
	 */
	@RequestMapping(value="/toAudit",method=RequestMethod.GET)
	public Object toAudit(Model model, Integer tradeId, String batchNo){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"query", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		model.addAttribute("trades", tradeService.selectByBatchNo(batchNo));
		model.addAttribute("tradeId", tradeId);
		model.addAttribute("batchNo", batchNo);
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/batchTrade/audit";
	}
	
	
	/**
	 * 冻结/解冻批量交易
	 * @return
	 */
	@RequestMapping(value="/audit",method=RequestMethod.POST)
	@ResponseBody
	public Object audit(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl,"edit", this.getSession())){return ResponseModel.getModel(ResultEnum.NOT_AUTH, null);}
		try {
			BatchTrade batchTrade = new BatchTrade();
			ParameterMap map = this.getParameterMap();
			batchTrade.setStatus(Integer.parseInt(map.getString("status")));
			batchTrade.setBatchNo(map.getString("batchNo"));
			String smsCode = map.getString("smsCode");
			//TODO 验证短信验证码
			log.info("验证短信验证码:{}", smsCode);
			String userId = ((User) this.getSession().getAttribute(Const.SESSION_USER)).getUsername();
			batchTrade.setAuditor(userId);
			batchTrade.setAuditTime(new Date());
			batchTrade.setId(Integer.parseInt(map.getString("id")));
			batchTradeService.audit(batchTrade);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败", "failed", null);
		}
		return ResponseModel.getModel("ok", "success", null);
	}
	
}
