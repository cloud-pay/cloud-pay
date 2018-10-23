package com.cloud.pay.merchant.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;
import com.cloud.pay.common.contants.ApiErrorCode;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.exception.CloudApiBusinessException;
import com.cloud.pay.common.mapper.SysConfigMapper;
import com.cloud.pay.common.utils.OSSUnit;
import com.cloud.pay.merchant.constant.MerchantConstant;
import com.cloud.pay.merchant.dto.MerchantApplyDTO;
import com.cloud.pay.merchant.entity.MerchantApplyAttachementInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBankInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.entity.MerchantApplyFeeInfo;
import com.cloud.pay.merchant.entity.MerchantAttachementInfo;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantFeeInfo;
import com.cloud.pay.merchant.mapper.MerchantApplyAttachementInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantApplyBankInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantApplyBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantApplyFeeInfoMapper;
import com.cloud.pay.merchant.util.ImgUtil;

@Service
public class MerchantApplyService {
	
	private Logger log =LoggerFactory.getLogger(MerchantApplyService.class);

	@Autowired
	private MerchantApplyBaseInfoMapper baseInfoMapper;
	
	@Autowired
	private MerchantApplyBankInfoMapper bankInfoMapper;
	
	@Autowired
	private MerchantApplyFeeInfoMapper feeInfoMapper;
	
	@Autowired
	private MerchantApplyAttachementInfoMapper attachementInfoMapper;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	@Autowired
	private MerchantService merchantService;
	
	@Value("${upload.root.folder}")
	public String root_fold;
	
	@Value("${merchant.folder}")
	public String merchantFolder;
	
	private static final String SEQ_OFFSET = "00000000";
	private AtomicInteger seq = new AtomicInteger(0);
	
	public List<MerchantApplyDTO> getOrgDTOs(Integer type, String code,
			String name, Integer status,  Date startTime,
			Date endTime) {
		return baseInfoMapper.getOrgApplyDTOs(type, code, name, status, startTime, endTime);
	}
	
	public List<MerchantApplyDTO> getMerchantDTOs(Integer orgId, String code,
			String name, Integer status,  Date startTime,
			Date endTime) {
		return baseInfoMapper.getMerchantApplyDTOs(orgId, code, name, status, startTime, endTime);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public String save(MerchantApplyBaseInfo baseInfo, MerchantApplyBankInfo bankInfo,
			MerchantApplyFeeInfo feeInfo, JSONObject attachementJson,String agentCode,boolean isFromOSS) throws IOException {
		String code = getMerchantCode();
		baseInfo.setCode(code);
		baseInfo.setStatus(MerchantConstant.AUDITING);
		log.info("保存商户申请数据:{}", baseInfo);
		baseInfoMapper.insert(baseInfo);
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.insert(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.insert(feeInfo);
		//如果从接口上送商户信息，则从OSS服务器读取文件信息
		if(isFromOSS) {
			if(null != attachementJson) {
				uploadImg(attachementJson, "businessPath",  MerchantConstant.BUSINESS,  baseInfo.getId(), true, agentCode);
				uploadImg(attachementJson, "certPath", MerchantConstant.CERT, baseInfo.getId(), true, agentCode);
				uploadImg(attachementJson, "bankCardPath", MerchantConstant.BANK_CARD, baseInfo.getId(), true, agentCode);
				uploadImg(attachementJson, "protocolPath",MerchantConstant.PROTOCOL, baseInfo.getId(), true, agentCode);
			}
		}else {
			if(attachementJson != null) {
				uploadImg(attachementJson, "businessPath", MerchantConstant.BUSINESS, baseInfo.getId(), true);
				uploadImg(attachementJson, "certPath", MerchantConstant.CERT, baseInfo.getId(), true);
				uploadImg(attachementJson, "bankCardPath", MerchantConstant.BANK_CARD, baseInfo.getId(), true);
				uploadImg(attachementJson, "protocolPath",MerchantConstant.PROTOCOL, baseInfo.getId(), true);
			}
		}
		return code;
	}
	
	private String getMerchantCode() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		final String date = sdf.format(new Date());
		final int value = seq.incrementAndGet();
		String temp = (SEQ_OFFSET + String.valueOf(value));
		temp = temp.substring(temp.length() - SEQ_OFFSET.length());
		return date + temp;
	}
	
	@Transactional
	public Map<String, Object> select(Integer id) {
		Map<String, Object> merchantMap = new HashMap<>();
		merchantMap.put("baseInfo", baseInfoMapper.selectByPrimaryKey(id));
		merchantMap.put("bankInfo", bankInfoMapper.selectByMerchantId(id));
		merchantMap.put("feeInfo", feeInfoMapper.selectByMerchantId(id));
		List<MerchantApplyAttachementInfo> infos = attachementInfoMapper.selectByMerchantId(id);
		if(infos != null) {
			for(MerchantApplyAttachementInfo info : infos) {
				if(MerchantConstant.BUSINESS == info.getAttachementType()) {
					merchantMap.put("businessPath", info.getAttachementPath());
				} else if(MerchantConstant.BANK_CARD == info.getAttachementType()) {
					merchantMap.put("bankCardPath", info.getAttachementPath());
				} else if(MerchantConstant.CERT == info.getAttachementType()) {
					merchantMap.put("certPath", info.getAttachementPath());
				} else if(MerchantConstant.PROTOCOL == info.getAttachementType()) {
					merchantMap.put("protocolPath", info.getAttachementPath());
				}
				
			}
		}
		return merchantMap;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void update(MerchantApplyBaseInfo baseInfo, MerchantApplyBankInfo bankInfo,
			MerchantApplyFeeInfo feeInfo,  JSONObject attachementJson,String agentCode,boolean isFromOSS) throws IOException {
		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.updateByPrimaryKeySelective(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.updateByPrimaryKeySelective(feeInfo);
		//如果从接口上送商户信息，则从OSS服务器读取文件信息
		if(isFromOSS) {
			if(null != attachementJson) {
				uploadImg(attachementJson, "businessPath",  MerchantConstant.BUSINESS,  baseInfo.getId(), false, agentCode);
				uploadImg(attachementJson, "certPath", MerchantConstant.CERT, baseInfo.getId(), false, agentCode);
				uploadImg(attachementJson, "bankCardPath", MerchantConstant.BANK_CARD, baseInfo.getId(), false, agentCode);
				uploadImg(attachementJson, "protocolPath",MerchantConstant.PROTOCOL, baseInfo.getId(), false, agentCode);
			}
		}else {
			if(attachementJson != null) {
				uploadImg(attachementJson, "businessPath", MerchantConstant.BUSINESS, baseInfo.getId(), false);
				uploadImg(attachementJson, "certPath", MerchantConstant.CERT, baseInfo.getId(), false);
				uploadImg(attachementJson, "bankCardPath", MerchantConstant.BANK_CARD, baseInfo.getId(), false);
				uploadImg(attachementJson, "protocolPath",MerchantConstant.PROTOCOL, baseInfo.getId(), false);
			}
		}
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void audit(Integer id, Integer status, String auditOptinion, String modifer) throws Exception {
		MerchantApplyBaseInfo baseInfo = new MerchantApplyBaseInfo();
		baseInfo.setId(id);
		baseInfo.setStatus(status);
		baseInfo.setAuditOptinion(auditOptinion);
		baseInfo.setModifer(modifer);
		baseInfo.setModifyTime(new Date());
		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
		//审核通过后，新增商户
		if(status == MerchantConstant.AUDIT_YES) {
			MerchantApplyBaseInfo merchantApplyBaseInfo = baseInfoMapper.selectByPrimaryKey(id);
			MerchantBaseInfo merchantBaseInfo = new MerchantBaseInfo();
			BeanUtils.copyProperties(merchantApplyBaseInfo, merchantBaseInfo);
			merchantBaseInfo.setStatus(MerchantConstant.NORMAL);
			MerchantApplyBankInfo merchantApplyBankInfo = bankInfoMapper.selectByMerchantId(id);
			MerchantBankInfo merchantBankInfo = new MerchantBankInfo();
			BeanUtils.copyProperties(merchantApplyBankInfo, merchantBankInfo);
			MerchantApplyFeeInfo merchantApplyFeeInfo = feeInfoMapper.selectByMerchantId(id);
			MerchantFeeInfo merchantFeeInfo = new MerchantFeeInfo();
			BeanUtils.copyProperties(merchantApplyFeeInfo, merchantFeeInfo);
			List<MerchantApplyAttachementInfo> infos = attachementInfoMapper.selectByMerchantId(id);
			List<MerchantAttachementInfo> attachements = null;
			if(infos != null && infos.size() > 0) {
				attachements = new ArrayList<>();
				for(MerchantApplyAttachementInfo info : infos) {
					MerchantAttachementInfo attachement = new MerchantAttachementInfo();
					BeanUtils.copyProperties(info, attachement);
					attachements.add(attachement);
				}
			}
			if(merchantApplyBaseInfo.getVersion() == 1) {
				merchantService.save(merchantBaseInfo, merchantBankInfo, merchantFeeInfo, attachements);
			} else {
				//商户信息变更
				merchantService.edit(merchantBaseInfo, merchantBankInfo, merchantFeeInfo, attachements);
			}
		}
	}
	
	/**
	 * 上传文件
	 * @param attachementJson
	 * @param key
	 * @param type
	 * @param merchantId
	 * @return
	 * @throws IOException
	 */
	private void uploadImg(JSONObject attachementJson, String key, Integer type, Integer merchantId, boolean save) throws IOException {
		String path = attachementJson.getString(key);
		if(notEmpty(path)){
			path = replaceBase64Before(path);
			byte[] bytes = Base64.base64ToByteArray(path);
			InputStream in = new ByteArrayInputStream(bytes);
			String filePath = merchantFolder+random(8)+".png";
			String uploadPath = ImgUtil.uploadImg(root_fold,filePath, in);
			MerchantApplyAttachementInfo info = new MerchantApplyAttachementInfo();
			info.setMerchantId(merchantId);
			info.setAttachementType(type);
			info.setAttachementPath(uploadPath);
			if(save) {
				attachementInfoMapper.insert(info);
			} else {
				//attachementInfoMapper.insert(info);	
				attachementInfoMapper.updateByMerchantIdAndType(merchantId, type, uploadPath);
			}
		}
	}
	
	/**
	 * 从OSS下载文件到本地服务器
	 * @param attachementJson
	 * @param key
	 * @param type
	 * @param merchantId
	 * @param save
	 * @param agentCode
	 * @throws IOException
	 */
	public void uploadImg(JSONObject attachementJson,String key, Integer type,Integer merchantId,boolean save,String agentCode)throws IOException{
		String sourceFileFullPath = attachementJson.getString(key);
		if(notEmpty(sourceFileFullPath)){
			SysConfig accessKeyIdConfig = null;
			SysConfig secretAccessKeyConfig = null;
			try {
				accessKeyIdConfig = sysConfigMapper.selectByPrimaryKey("ossAccessKeyId");
				secretAccessKeyConfig = sysConfigMapper.selectByPrimaryKey("ossSecretAccessKey");
		    }catch(Exception e) {
		    	log.error("读取OSS服务器配置错误：{}",e);
		    }
			InputStream in = OSSUnit.getOSS2InputStream(OSSUnit.getOSSClient(accessKeyIdConfig.getSysValue(),secretAccessKeyConfig.getSysValue()), agentCode, sourceFileFullPath);
			String filePath = merchantFolder+random(8)+".png";
			String uploadPath = ImgUtil.uploadImg(root_fold,filePath, in);
			MerchantApplyAttachementInfo info = new MerchantApplyAttachementInfo();
			info.setMerchantId(merchantId);
			info.setAttachementType(type);
			info.setAttachementPath(uploadPath);
			if(save) {
				attachementInfoMapper.insert(info);
			} else {
				attachementInfoMapper.updateByMerchantIdAndType(merchantId, type, uploadPath);
			}
		}
	}
	
	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	private boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}
	
	/**
	 * 替换base64的前缀
	 * @param pics
	 * @return
	 */
	private String replaceBase64Before(String pics){
		pics = pics.replace("data:image/png;base64,", "");
		pics = pics.replace("data:image/jpeg;base64,", "");
		pics = pics.replace("data:image/bmp;base64,", "");
		pics = pics.replace("data:image/x-icon;base64,", "");
		pics = pics.replace("data:image/gif;base64,", "");
		return pics;
	}
	
	/**
	 * 返回随机数
	 * 
	 * @param n 个数
	 * @return
	 */
	private String random(int n) {
		if (n < 1 || n > 10) {
			throw new IllegalArgumentException("cannot random " + n + " bit number");
		}
		Random ran = new Random();
		if (n == 1) {
			return String.valueOf(ran.nextInt(10));
		}
		int bitField = 0;
		char[] chs = new char[n];
		for (int i = 0; i < n; i++) {
			while (true) {
				int k = ran.nextInt(10);
				if ((bitField & (1 << k)) == 0) {
					bitField |= 1 << k;
					chs[i] = (char) (k + '0');
					break;
				}
			}
		}
		return new String(chs);
	}
	
	/**
	 * 根据商户编号获取商户信息
	 * @param code
	 * @return
	 */
	public Map<String, Object> selectByCode(String code) {
		Map<String, Object> merchantMap = new HashMap<>();
		MerchantApplyBaseInfo baseInfo = baseInfoMapper.selectByCode(code);
		if(null == baseInfo) {
			throw new CloudApiBusinessException(ApiErrorCode.MCH_INVALID, "商户信息不存在");
		}
		merchantMap.put("baseInfo", baseInfo);
		merchantMap.put("bankInfo", bankInfoMapper.selectByMerchantId(baseInfo.getId()));
		merchantMap.put("feeInfo", feeInfoMapper.selectByMerchantId(baseInfo.getId()));
		List<MerchantApplyAttachementInfo> infos = attachementInfoMapper.selectByMerchantId(baseInfo.getId());
		if(infos != null) {
			for(MerchantApplyAttachementInfo info : infos) {
				if(MerchantConstant.BUSINESS == info.getAttachementType()) {
					merchantMap.put("businessPath", info.getAttachementPath());
				} else if(MerchantConstant.BANK_CARD == info.getAttachementType()) {
					merchantMap.put("bankCardPath", info.getAttachementPath());
				} else if(MerchantConstant.CERT == info.getAttachementType()) {
					merchantMap.put("certPath", info.getAttachementPath());
				} else if(MerchantConstant.PROTOCOL == info.getAttachementType()) {
					merchantMap.put("protocolPath", info.getAttachementPath());
				}
				
			}
		}
		return merchantMap;
	}
	
	/**
	 * 变更商户
	 * @param baseInfo
	 * @param bankInfo
	 * @param feeInfo
	 * @param attachementJson
	 * @return
	 * @throws IOException
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void change(MerchantApplyBaseInfo baseInfo, MerchantApplyBankInfo bankInfo,
			MerchantApplyFeeInfo feeInfo, JSONObject attachementJson) throws IOException {
		Integer originalId = baseInfo.getId();
		baseInfo.setId(null);
		baseInfo.setStatus(MerchantConstant.AUDITING);
		log.info("保存商户变更数据:{}", baseInfo);
		baseInfoMapper.insert(baseInfo);
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.insert(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.insert(feeInfo);
		List<MerchantApplyAttachementInfo> infos = attachementInfoMapper.selectByMerchantId(originalId);
		if(infos != null) {
			for(MerchantApplyAttachementInfo info : infos) {
				if(MerchantConstant.BUSINESS == info.getAttachementType()) {
					if(attachementJson != null && notEmpty(attachementJson.getString("businessPath"))) {
						uploadImg(attachementJson, "businessPath", MerchantConstant.BUSINESS, baseInfo.getId(), true);
					} else {
						this.saveAttachmentInfo(info, baseInfo.getId(), MerchantConstant.BUSINESS);
					}
				} else if(MerchantConstant.BANK_CARD == info.getAttachementType()) {
					if(attachementJson != null && notEmpty(attachementJson.getString("bankCardPath"))) {
						uploadImg(attachementJson, "bankCardPath", MerchantConstant.BANK_CARD, baseInfo.getId(), true);
					} else {
						this.saveAttachmentInfo(info, baseInfo.getId(), MerchantConstant.BANK_CARD);
					}
				} else if(MerchantConstant.CERT == info.getAttachementType()) {
					if(attachementJson != null && notEmpty(attachementJson.getString("certPath"))) {
						uploadImg(attachementJson, "certPath", MerchantConstant.CERT, baseInfo.getId(), true);
					} else {
						this.saveAttachmentInfo(info, baseInfo.getId(), MerchantConstant.CERT);
					}
				} else if(MerchantConstant.PROTOCOL == info.getAttachementType()) {
					if(attachementJson != null && notEmpty(attachementJson.getString("protocolPath"))) {
						uploadImg(attachementJson, "protocolPath", MerchantConstant.PROTOCOL, baseInfo.getId(), true);
					} else {
						this.saveAttachmentInfo(info, baseInfo.getId(), MerchantConstant.PROTOCOL);
					}
				}
			}
		}
	}
	
	/**
	 * 根据原附件信息保存
	 * @param info
	 * @param id
	 * @param type
	 */
	private void saveAttachmentInfo(MerchantApplyAttachementInfo info, Integer id, Integer type) {
		MerchantApplyAttachementInfo attInfo = new MerchantApplyAttachementInfo();
		attInfo.setMerchantId(id);
		attInfo.setAttachementType(type);
		attInfo.setAttachementPath(info.getAttachementPath());
		attachementInfoMapper.insert(attInfo);
	}
}
