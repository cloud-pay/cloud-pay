package com.cloud.pay.client.handler.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.client.constants.Constants;
import com.cloud.pay.client.handler.ICloudPayApiHandler;
import com.cloud.pay.client.vo.CloudApiReconFileParam;
import com.cloud.pay.client.vo.CloudApiReconFileResult;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.service.MerchantService;
import com.cloud.pay.recon.service.ReconService;

/**
 * 获取对账文件
 * @author THINKPAD
 *
 */
@Service("cloudReconFileHandler")
public class CloudApiReconFileHandler 
         implements ICloudPayApiHandler<CloudApiReconFileParam, CloudApiReconFileResult> {
	
	private Logger log = LoggerFactory.getLogger(CloudApiReconFileHandler.class);
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private ReconService reconService;

	@Override
	public CloudApiReconFileResult handle(CloudApiReconFileParam reqParam) {
		log.info("获取对账文件，请求参数：{}",reqParam);
		CloudApiReconFileResult result = new CloudApiReconFileResult();
		result.setMchCode(reqParam.getMchCode());
		String mchCode = reqParam.getMchCode();
		//如果subMchCode不为空，则为机构获取指定商户的对账文件
		if(StringUtils.isNotBlank(reqParam.getSubMchCode())) {
			mchCode = reqParam.getSubMchCode();
			result.setSubMchCode(mchCode);
		    
		}
		Map<String, Object> merchantMap = merchantService.selectByCode(mchCode);
		if(null == merchantMap || merchantMap.size() <= 0 ) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.SUB_MCH_INVALID);
			result.setErrorMsg("机构或者商户不存在");
			log.info("获取对账文件，响应结果：{}",result);
			return result;
		}
		MerchantBaseInfo baseInfo = (MerchantBaseInfo) merchantMap.get("baseInfo");
		//如果为机构获取商户的对账文件，则必须校验subMchCode对应的商户信息是否为商户
		if(StringUtils.isNotBlank(reqParam.getSubMchCode())) {
			if(4 != baseInfo.getType() && 5 != baseInfo.getType()) {
				result.setResultCode(Constants.RESULT_CODE_FAIL);
				result.setErrorCode(ApiErrorCode.SUB_MCH_TYPE_ERROR);
				result.setErrorMsg("子商户类型错误");
				log.info("获取对账文件，响应结果：{}",result);
				return result;
			}
		}
		//获取对账文件只需要区分机构和商户
		Integer mchType = 2;
		if(4 == baseInfo.getType() || 5 == baseInfo.getType()) {
			mchType = 1;
		}
		String filePath = reconService.queryReconFile(baseInfo.getId(), reqParam.getReconDate(), mchType, true);
		if(StringUtils.isBlank(filePath)) {
			result.setResultCode(Constants.RESULT_CODE_FAIL);
			result.setErrorCode(ApiErrorCode.RECON_FILE_NOT_CREATE);
			result.setErrorMsg("对账文件未生成");
			log.info("获取对账文件，响应结果：{}",result);
			return result;
		}
		result.setFilePath(filePath);
		log.info("获取对账文件，响应结果：{}",result);
		return result;
	}

	@Override
	public Class<CloudApiReconFileParam> getReqParamType() {
		return CloudApiReconFileParam.class;
	}

}
