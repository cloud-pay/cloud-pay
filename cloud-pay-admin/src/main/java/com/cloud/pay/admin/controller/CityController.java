package com.cloud.pay.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.pay.admin.controller.base.BaseController;
import com.cloud.pay.admin.entity.ResponseModel;
import com.cloud.pay.admin.util.ParameterMap;
import com.cloud.pay.common.mapper.CityMapper;

@Controller
@RequestMapping("/city")
public class CityController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(CityController.class);
	
	@Autowired
	private CityMapper cityMapper;
	
	/**
	 * 获取商戶
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public Object list(){
		try {
			ParameterMap map = this.getParameterMap();
			Integer pid = Integer.parseInt(map.getString("pid"));
			return ResponseModel.getModel("ok", "success", cityMapper.selectByPid(pid));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:{}", e);
			return ResponseModel.getModel("提交失败:"+e.getMessage(), "failed", null);
		}
	}
	
}
