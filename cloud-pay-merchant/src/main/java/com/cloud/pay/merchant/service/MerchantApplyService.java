package com.cloud.pay.merchant.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;
import com.cloud.pay.merchant.constant.MerchantConstant;
import com.cloud.pay.merchant.dto.MerchantApplyDTO;
import com.cloud.pay.merchant.entity.MerchantApplyAttachementInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBankInfo;
import com.cloud.pay.merchant.entity.MerchantApplyBaseInfo;
import com.cloud.pay.merchant.entity.MerchantApplyFeeInfo;
import com.cloud.pay.merchant.mapper.MerchantApplyAttachementInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantApplyBankInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantApplyBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantApplyFeeInfoMapper;
import com.cloud.pay.merchant.util.ImgUtil;

@Service
public class MerchantApplyService {

	@Autowired
	private MerchantApplyBaseInfoMapper baseInfoMapper;
	
	@Autowired
	private MerchantApplyBankInfoMapper bankInfoMapper;
	
	@Autowired
	private MerchantApplyFeeInfoMapper feeInfoMapper;
	
	@Autowired
	private MerchantApplyAttachementInfoMapper attachementInfoMapper;
	
	@Value("${upload.root.folder}")
	public String root_fold;
	
	@Value("${merchant.folder}")
	public String merchantFolder;
	
	private static final String SEQ_OFFSET = "00000000";
	private AtomicInteger seq = new AtomicInteger(0);
	
	public List<MerchantApplyDTO> getMerchantDTOs(Integer orgId, String code,
			String name, Integer status,  Date startTime,
			Date endTime) {
		return baseInfoMapper.getMerchantApplyDTOs(orgId, code, name, status, startTime, endTime);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public String save(MerchantApplyBaseInfo baseInfo, MerchantApplyBankInfo bankInfo,
			MerchantApplyFeeInfo feeInfo, JSONObject attachementJson) throws IOException {
		String code = getMerchantCode();
		baseInfo.setCode(code);
		baseInfo.setStatus(MerchantConstant.AUDITING);
		baseInfoMapper.insert(baseInfo);
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.insert(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.insert(feeInfo);
		if(attachementJson != null) {
			uploadImg(attachementJson, "businessPath", MerchantConstant.BUSINESS, baseInfo.getId(), true);
			uploadImg(attachementJson, "certPath", MerchantConstant.CERT, baseInfo.getId(), true);
			uploadImg(attachementJson, "bankCardPath", MerchantConstant.BANK_CARD, baseInfo.getId(), true);
			uploadImg(attachementJson, "protocolPath",MerchantConstant.PROTOCOL, baseInfo.getId(), true);
		}
		return code;
	}
	
	private String getMerchantCode() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
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
			MerchantApplyFeeInfo feeInfo,  JSONObject attachementJson) throws IOException {
		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.updateByPrimaryKeySelective(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.updateByPrimaryKeySelective(feeInfo);
		if(attachementJson != null) {
			uploadImg(attachementJson, "businessPath", MerchantConstant.BUSINESS, baseInfo.getId(), false);
			uploadImg(attachementJson, "certPath", MerchantConstant.CERT, baseInfo.getId(), false);
			uploadImg(attachementJson, "bankCardPath", MerchantConstant.BANK_CARD, baseInfo.getId(), false);
			uploadImg(attachementJson, "protocolPath",MerchantConstant.PROTOCOL, baseInfo.getId(), false);
		}
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void audit(Integer id, Integer status, String auditOptinion, String modifer) {
		MerchantApplyBaseInfo baseInfo = new MerchantApplyBaseInfo();
		baseInfo.setId(id);
		baseInfo.setStatus(status);
		baseInfo.setAuditOptinion(auditOptinion);
		baseInfo.setModifer(modifer);
		baseInfo.setModifyTime(new Date());
		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
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
				attachementInfoMapper.insert(info);
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
}
