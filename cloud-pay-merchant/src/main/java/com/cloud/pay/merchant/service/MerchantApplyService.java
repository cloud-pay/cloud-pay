package com.cloud.pay.merchant.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	
	private static final String SEQ_OFFSET = "00000000";
	private AtomicInteger seq = new AtomicInteger(0);
	
	public List<MerchantApplyDTO> getMerchantDTOs(Integer orgId, String code,
			String name, Integer status,  Date startTime,
			Date endTime) {
		return baseInfoMapper.getMerchantApplyDTOs(orgId, code, name, status, startTime, endTime);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void save(MerchantApplyBaseInfo baseInfo, MerchantApplyBankInfo bankInfo,
			MerchantApplyFeeInfo feeInfo, List<MerchantApplyAttachementInfo> attachementInfos) {
		baseInfo.setCode(getMerchantCode());
		baseInfo.setStatus(MerchantConstant.AUDITING);
		baseInfoMapper.insert(baseInfo);
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.insert(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.insert(feeInfo);
		if(attachementInfos != null) {
			for(MerchantApplyAttachementInfo att : attachementInfos) {
				att.setMerchantId(baseInfo.getId());
				attachementInfoMapper.insert(att);
			}
		}
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
		List<MerchantApplyAttachementInfo> attachementInfos = attachementInfoMapper.selectByMerchantId(id);
		return merchantMap;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void update(MerchantApplyBaseInfo baseInfo, MerchantApplyBankInfo bankInfo,
			MerchantApplyFeeInfo feeInfo, List<MerchantApplyAttachementInfo> attachementInfos) {
		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.updateByPrimaryKeySelective(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.updateByPrimaryKeySelective(feeInfo);
		if(attachementInfos != null) {
			for(MerchantApplyAttachementInfo att : attachementInfos) {
				att.setMerchantId(baseInfo.getId());
				attachementInfoMapper.updateByPrimaryKeySelective(att);
			}
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
}
