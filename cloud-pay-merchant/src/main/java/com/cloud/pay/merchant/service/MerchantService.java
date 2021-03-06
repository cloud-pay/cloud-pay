package com.cloud.pay.merchant.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.pay.common.mapper.CityMapper;
import com.cloud.pay.merchant.constant.MerchantConstant;
import com.cloud.pay.merchant.dto.MerchantDTO;
import com.cloud.pay.merchant.entity.MerchantAttachementInfo;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantChannel;
import com.cloud.pay.merchant.entity.MerchantFeeInfo;
import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.entity.MerchantSecret;
import com.cloud.pay.merchant.entity.UserMerchant;
import com.cloud.pay.merchant.mapper.MerchantAttachementInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantBankInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantChannelMapper;
import com.cloud.pay.merchant.mapper.MerchantFeeInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantPrepayInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantSecretMapper;
import com.cloud.pay.merchant.mapper.UserMerchantMapper;
import com.cloud.pay.merchant.util.MD5;

@Service
public class MerchantService {
	
	private Logger log = LoggerFactory.getLogger(MerchantService.class);

	@Autowired
	private MerchantBaseInfoMapper baseInfoMapper;
	
	@Autowired
	private MerchantBankInfoMapper bankInfoMapper;
	
	@Autowired
	private MerchantFeeInfoMapper feeInfoMapper;
	
	@Autowired
	private MerchantAttachementInfoMapper attachementInfoMapper;
	
	@Autowired
	private MerchantSecretMapper merchantSecretMapper;
	
	@Autowired
	private MerchantChannelMapper merchantChannelMapper;
	
	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;
	
	@Autowired
	private UserMerchantMapper userMerchantMapper;
	
	@Autowired
	private CityMapper cityMapper;

	public List<MerchantDTO> getMerchantDTOs(String type) {
		return baseInfoMapper.getMerchantDTOs(type);
	}
	
	public List<MerchantDTO> selectByMerchantType(Integer type) {
		return baseInfoMapper.selectByMerchantType(type);
	}
	
	/**
	 * 机构列表查询list
	 * 
	 * @param orgId
	 * @param code
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<MerchantDTO> getOrgList(Integer type, String code, String name, 
			Date startTime, Date endTime, Integer userMerchantId, String userMerchantType) {
		return baseInfoMapper.getOrgList(type, code, name, startTime, endTime, userMerchantId, userMerchantType);
	}

	/**
	 * 商户列表查询list
	 * 
	 * @param orgId
	 * @param code
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<MerchantDTO> getMerchantList(Integer orgId, String code, String name, 
			Date startTime, Date endTime, Integer userMerchantId, String userMerchantType) {
		return baseInfoMapper.getMerchantList(orgId, code, name, startTime, endTime, userMerchantId, userMerchantType);
	}

	public int update(MerchantBaseInfo baseInfo) {
		return baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
	}
	
	@Transactional
	public Map<String, Object> select(Integer id) {
		Map<String, Object> merchantMap = new HashMap<>();
		MerchantBaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(id);
		merchantMap.put("baseInfo", baseInfo);
		merchantMap.put("bankInfo", bankInfoMapper.selectByMerchantId(id));
		merchantMap.put("feeInfo", feeInfoMapper.selectByMerchantId(id));
		List<MerchantAttachementInfo> infos = attachementInfoMapper.selectByMerchantId(id);
		if(infos != null) {
			for(MerchantAttachementInfo info : infos) {
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
		merchantMap.put("citys", cityMapper.selectByPid(baseInfo.getProvincial()));
		return merchantMap;
	}

	public MerchantSecret slectSecret(Integer merchantId) {
		return merchantSecretMapper.selectByPrimaryKey(merchantId);
	}
	
	public List<MerchantChannel> selectChennels(Integer merchantId) {
		return merchantChannelMapper.selectByMerchantId(merchantId);
	}
	
	/**
	 * 根据具体商户类型查询商户信息
	 * @param type
	 * @return
	 */
	public List<MerchantDTO> selectMerchantByType(Integer type){
		  return baseInfoMapper.getAdvanceMerchantList(type);
	}
	
	/**
	 * 查询商户的签名key
	 * @param code
	 * @return
	 */
	public MerchantSecret selectSecretByCode(String code) {
		return merchantSecretMapper.selectByCode(code);
	}
	
	/**
	 * 保存商户
	 * @param baseInfo
	 * @param bankInfo
	 * @param feeInfo
	 * @param infos
	 * @throws Exception
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void save(MerchantBaseInfo baseInfo, MerchantBankInfo bankInfo,
			MerchantFeeInfo feeInfo, List<MerchantAttachementInfo> infos) throws Exception {
		baseInfo.setId(null);
		log.info("新增商户信息:{}", baseInfo);
		baseInfoMapper.insert(baseInfo);
		MerchantSecret record = new MerchantSecret();
		record.setMerchantId(baseInfo.getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		record.setSecret(MD5.md5(baseInfo.getCode(), sdf.format(baseInfo.getCreateTime())));
		merchantSecretMapper.insert(record);
		bankInfo.setMerchantId(baseInfo.getId());
		log.info("新增商户银行卡信息:{}", bankInfo);
		bankInfoMapper.insert(bankInfo);
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.insert(feeInfo);
		if(infos != null) {
			for(MerchantAttachementInfo info : infos) {
				info.setMerchantId(baseInfo.getId());
				attachementInfoMapper.insert(info);
			}
		}
		//开通预缴户
		MerchantPrepayInfo perpay = new MerchantPrepayInfo();
		perpay.setMerchantId(baseInfo.getId());
		perpay.setBalance(BigDecimal.ZERO);
		perpay.setFreezeAmount(BigDecimal.ZERO);
		perpay.setOverdraw(MerchantConstant.OVERDRAW_NO);
		perpay.setCreateTime(new Date());
		perpay.setDigest(MD5.md5("0.00|0.00" , 
				String.valueOf(perpay.getMerchantId())));
		log.info("新增预缴户信息:{}", perpay);
		merchantPrepayInfoMapper.insert(perpay);
	}
	
	/**
	 * 根据商户编码查询商户信息
	 * @param code
	 * @return
	 */
	public Map<String, Object> selectByCode(String code) {
		Map<String, Object> merchantMap = new HashMap<>();
		MerchantBaseInfo baseInfo = baseInfoMapper.selectByCode(code);
		if(null == baseInfo) {
			return null;
			//throw new CloudApiBusinessException(ApiErrorCode.MCH_INVALID, "商户信息不存在");
		}
		merchantMap.put("baseInfo", baseInfo);
		merchantMap.put("bankInfo", bankInfoMapper.selectByMerchantId(baseInfo.getId()));
		merchantMap.put("feeInfo", feeInfoMapper.selectByMerchantId(baseInfo.getId()));
		List<MerchantAttachementInfo> infos = attachementInfoMapper.selectByMerchantId(baseInfo.getId());
		if(infos != null) {
			for(MerchantAttachementInfo info : infos) {
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
	 * 查询商户秘钥
	 * @param merchantId
	 * @return
	 */
	public MerchantSecret selectByMerchantId(Integer merchantId) {
		return merchantSecretMapper.selectByPrimaryKey(merchantId);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void saveUserMerchant(UserMerchant userMerchant) {
		userMerchantMapper.insert(userMerchant);
	}
	
	/**
	 * 变更商户
	 * @param baseInfo
	 * @param bankInfo
	 * @param feeInfo
	 * @param infos
	 * @throws Exception
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 3)
	public void edit(MerchantBaseInfo baseInfo, MerchantBankInfo bankInfo,
			MerchantFeeInfo feeInfo, List<MerchantAttachementInfo> infos) throws Exception {
		MerchantBaseInfo orginalMerchantBaseInfo = baseInfoMapper.selectByCode(baseInfo.getCode());
		baseInfo.setId(orginalMerchantBaseInfo.getId());
		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
		MerchantBankInfo orginalMerchantBankInfo = bankInfoMapper.selectByMerchantId(baseInfo.getId());
		bankInfo.setId(orginalMerchantBankInfo.getId());
		bankInfo.setMerchantId(baseInfo.getId());
		bankInfoMapper.updateByPrimaryKeySelective(bankInfo);
		MerchantFeeInfo orginalMerchantFeeInfo = feeInfoMapper.selectByMerchantId(baseInfo.getId());
		feeInfo.setId(orginalMerchantFeeInfo.getId());
		feeInfo.setMerchantId(baseInfo.getId());
		feeInfoMapper.updateByPrimaryKeySelective(feeInfo);
		List<MerchantAttachementInfo> orginalAttInfos = attachementInfoMapper.selectByMerchantId(baseInfo.getId());
		if(infos != null) {
			for(MerchantAttachementInfo info : infos) {
				for(MerchantAttachementInfo orginalInfo : orginalAttInfos) {
					if(info.getAttachementType() == orginalInfo.getAttachementType()) {
						info.setId(orginalInfo.getId());
						info.setMerchantId(baseInfo.getId());
						attachementInfoMapper.updateByPrimaryKeySelective(info);
					}
				}
			}
		}
	}
}
