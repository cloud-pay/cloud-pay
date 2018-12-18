package com.cloud.pay.trade.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.cloud.pay.channel.dto.TradeDTO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.mapper.SysConfigMapper;
import com.cloud.pay.common.utils.OSSUnit;
import com.cloud.pay.common.utils.TableCodeUtils;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.entity.MerchantPrepayInfo;
import com.cloud.pay.merchant.entity.MerchantRouteConf;
import com.cloud.pay.merchant.mapper.MerchantBankInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantPrepayInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantRouteConfMapper;
import com.cloud.pay.trade.constant.SmsConstant;
import com.cloud.pay.trade.constant.TradeConstant;
import com.cloud.pay.trade.dto.BatchTradeDTO;
import com.cloud.pay.trade.entity.BatchTrade;
import com.cloud.pay.trade.entity.PaySms;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;
import com.cloud.pay.trade.mapper.BatchTradeMapper;
import com.cloud.pay.trade.mapper.PaySmsMapper;
import com.cloud.pay.trade.mapper.TradeMapper;
import com.cloud.pay.trade.util.ConvertUtil;
import com.cloud.pay.trade.util.ExcelUtil;


@Service
public class BatchTradeService {
	
	private Logger log =LoggerFactory.getLogger(BatchTradeService.class);

	@Autowired
	private BatchTradeMapper batchTradeMapper;

	@Autowired
	private MerchantBankInfoMapper merchantBankInfoMapper;
	
	@Autowired
	private MerchantBaseInfoMapper merchantBaseInfoMapper;
	
	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final static String TRADE_SQL = "insert into t_trade (order_no, merchant_id, trade_amount, "
			+ "status, payer_id, payee_name, payee_bank_card, payee_bank_code, remark, batch_no, payee_bank_name, payee_bank_acct_type,"
			+ "merchant_fee_amount,loan_benefit,org_benefit, loaning, seq_no) "
			+ "values (?, ?, ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?)";
	
	private String sources = "0123456789";
	
	@Autowired
	private PaySmsMapper paySmsMapper;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	@Autowired
	private PayHandler payHandler;
	
	@Autowired
	private PrepayInfoService prepayInfoService;
	
	@Autowired
	private MerchantRouteConfMapper merchantRouteConfMapper;
	
	@Autowired
	private MerchantPrepayInfoMapper merchantPrepayInfoMapper;
	
	@Autowired
	private BatchTradeHandler batchTradeHandler;
	
	@Autowired
	private SmsService smsService;

	/**
	 * 批量文件上传
	 * @param batchTrade
	 * @param payFilePath
	 * @param loaning
	 * @return
	 */
	@Transactional
	public String upload(BatchTrade batchTrade, String payFilePath, Integer loaning) {
		StringBuilder errorDetails = new StringBuilder();
		BigDecimal totalAmount = BigDecimal.ZERO;
		try {
			final List<Trade> trades = ExcelUtil.readExcel(payFilePath, batchTrade.getBatchNo(), 
					loaning, batchTrade.getPayerMerchantId());
			//逐笔计算手续费
			for(Trade temp : trades) {
				BigDecimal merchantFee = BigDecimal.ZERO;
				BigDecimal[] fees = payHandler.getFee(temp.getMerchantId(), temp.getTradeAmount());
				merchantFee = fees[0];
				if(TradeConstant.LOANING_YES == temp.getLoaning()) {
					//计算垫资分润
					temp.setLoanBenefit(fees[1]);
					merchantFee = merchantFee.add(fees[1]);
				}
				temp.setMerchantFeeAmount(merchantFee);
				BigDecimal orgFee = payHandler.getOrgFee(temp.getMerchantId(), temp.getTradeAmount());
				temp.setOrgBenefit(merchantFee.subtract(orgFee));
				totalAmount = totalAmount.add(temp.getTradeAmount());
			} 
			batchTrade.setTotalAmount(totalAmount);
			batchTrade.setTotalCount(trades.size());
			batchTrade.setTradeStatus(TradeConstant.BATCH_STATUS_UNPROCESS);
			log.info("新增批量交易{}", batchTrade);
			batchTradeMapper.insert(batchTrade);
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMddHHmmss");
			String platBatchNo = TableCodeUtils.getTableCode(batchTrade.getId(), sdfTime.format(new Date()));
			//生成平台订单号
			batchTrade.setPlatBatchNo(platBatchNo);
			batchTradeMapper.updateByPrimaryKeySelective(batchTrade);
			// TODO 批量新增
			jdbcTemplate.batchUpdate(TRADE_SQL, trades, trades.size(),
					new ParameterizedPreparedStatementSetter<Trade>() {
						@Override
						public void setValues(PreparedStatement ps, Trade argument) throws SQLException {
							ps.setString(1, argument.getOrderNo());
							ps.setInt(2, argument.getMerchantId());
							ps.setBigDecimal(3, argument.getTradeAmount());
							ps.setNull(4, Types.INTEGER);
							ps.setString(5, argument.getPayeeName());
							ps.setString(6, argument.getPayeeBankCard());
							ps.setString(7, argument.getPayeeBankCode());
							if (null != argument.getRemark()) {
								ps.setString(8, argument.getRemark());
							} else {
								ps.setString(8, "");
							}
							ps.setString(9, argument.getBatchNo());
							if (null != argument.getPayeeBankName()) {
								ps.setString(10, argument.getPayeeBankName());
							} else {
								ps.setString(10, "");
							}
							if (null != argument.getPayeeBankAcctType()) {
								ps.setInt(11, argument.getPayeeBankAcctType());
							} else {
								ps.setNull(11, Types.INTEGER);
							}
							if (null != argument.getMerchantFeeAmount()) {
								ps.setBigDecimal(12, argument.getMerchantFeeAmount());
							} else {
								ps.setNull(12, Types.DECIMAL);
							}
							if (null != argument.getLoanBenefit()) {
								ps.setBigDecimal(13, argument.getLoanBenefit());
							} else {
								ps.setNull(13, Types.DECIMAL);
							}
							if (null != argument.getOrgBenefit()) {
								ps.setBigDecimal(14, argument.getOrgBenefit());
							} else {
								ps.setNull(14, Types.DECIMAL);
							}
							ps.setInt(15, loaning);
							ps.setInt(16, argument.getSeqNo());
						}
					});
		} catch(TradeException e){
			return e.getMessage();
		} 
		return errorDetails.toString();
	}
	
	/**
	 * 计算总金额
	 * @param merchantFee
	 * @param loanBenefit
	 * @param orgBenefit
	 * @return
	 */
	private BigDecimal add(BigDecimal merchantFee, BigDecimal loanBenefit, BigDecimal orgBenefit) {
		if(merchantFee == null) {
			merchantFee = BigDecimal.ZERO;
		}
		if(loanBenefit == null) {
			loanBenefit = BigDecimal.ZERO;
		}
		if(orgBenefit == null) {
			orgBenefit = BigDecimal.ZERO;
		}
		return merchantFee.add(orgBenefit).add(loanBenefit);
	}
	
	/**
	 *  如果插入数据库底层的数据有问题，直接不处理了，简单点处理先
	 * @param batchTrade
	 * @param payFilePath
	 * @return
	 */
	@Transactional
	public String batchPay(BatchTrade batchTrade, String fileName,String mchCode,String subMchCode,Integer loaning) {
		StringBuilder errorDetails = new StringBuilder();
		//step 1 从OSS服务器读取文件流
		SysConfig accessKeyIdConfig = null;
		SysConfig secretAccessKeyConfig = null;
		try {
			accessKeyIdConfig = sysConfigMapper.selectByPrimaryKey("ossAccessKeyId");
			secretAccessKeyConfig = sysConfigMapper.selectByPrimaryKey("ossSecretAccessKey");
	    }catch(Exception e) {
	    	log.error("读取OSS服务器配置错误：{}",e);
	    }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileFullpath = "batch" +  File.separator + "request" +  File.separator + 
				batchTrade.getBatchNo() + File.separator  + sdf.format(new Date())  + File.separator+ fileName;
		InputStream is = OSSUnit.getOSS2InputStream(OSSUnit.getOSSClient(accessKeyIdConfig.getSysValue(),secretAccessKeyConfig.getSysValue()), mchCode, fileFullpath.replaceAll("\\\\", "/"));
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			read = new InputStreamReader(is,"UTF-8");
			bufferedReader = new BufferedReader(read); //从字符输入流中读取文本，缓存到内存中
			String lineTxt_cr = null;//行读字符串
			final List<Trade> trades = new ArrayList<Trade>();
			Trade trade = null;
			int num = 0; //行数
			while((lineTxt_cr = bufferedReader.readLine()) != null){
				num ++;
				trade = new Trade();
				trade.setBatchNo(batchTrade.getBatchNo());
				trade.setLoaning(loaning);
				String[] line = lineTxt_cr.split("~");
				//获取商户信息（考虑机构发批量是发给多个商户发起）
				String merchantNo = line[0]; //商户号
				//判断请求商户和交易商户是否为同一个(如果有指定下游商户，则需要判断交易商户和指定商户是否一致)
				if(StringUtils.isNotBlank(subMchCode)) {
					if(!subMchCode.equals(merchantNo)) {
						log.info("第{}行，交易商户{}和请求商户{}非同一商户",num,merchantNo,subMchCode);
						errorDetails.append("第"+num+"行,交易商户和请求商户非同一商户;");
						continue;
					}
				}
				MerchantBaseInfo merchantBaseInfo = merchantBaseInfoMapper.selectByCode(merchantNo);
				if(null == merchantBaseInfo) {
					log.info("第{}行，商户信息{}不存在",num,merchantNo);
					errorDetails.append("第"+num+"行,商户信息"+merchantNo+"不存在");
					continue;
				}
				MerchantBankInfo bankInfo = merchantBankInfoMapper.selectByMerchantId(merchantBaseInfo.getId());
				if (null == bankInfo) {
				    log.info("第{}行,商户信息{},缺少银行配置信息,请联系平台工作人员",num,merchantNo);
				    errorDetails.append("第"+num+"行,商户信息"+merchantNo+"缺少银行配置信息,请联系平台工作人员");
				    continue;
				}
				trade.setPayerId(bankInfo.getId());
				trade.setMerchantId(merchantBaseInfo.getId());
				trade.setOrderNo(line[1]);//订单流水
				trade.setPayeeName(line[2]);
				String type = line[3];
				
				if (StringUtils.isNotBlank(type)) {
					if("1".equals(type) || "2".equals(type)) {
						trade.setPayeeBankAcctType(Integer.parseInt(type));
					}else {
						log.info("第{}行，商户{},账户类型不正确",num,merchantNo);
						errorDetails.append("第"+num+"行，商户："+merchantNo+",订单号:"+line[1]+",账户类型不正确");
						continue;
					}
				}
				trade.setPayeeBankCard(line[4]);
				trade.setPayeeBankCode(line[5]);
				trade.setPayeeBankName(line[6]);
				trade.setTradeAmount(new BigDecimal(line[7]));
				if(line.length == 9 ) {
				    trade.setRemark(line[8]);
				}
				trades.add(trade);
			}
			if (errorDetails.length() == 0) {
				//保存批量信息
				batchTradeMapper.insert(batchTrade);
				SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMddHHmmss");
				String platBatchNo = TableCodeUtils.getTableCode(batchTrade.getId(), sdfTime.format(new Date()));
				//生成平台订单号
				batchTrade.setPlatBatchNo(platBatchNo);
				batchTradeMapper.updateByPrimaryKeySelective(batchTrade);
				errorDetails.append("platBatchNo:" + platBatchNo +"||");
				// TODO 批量新增
				jdbcTemplate.batchUpdate(TRADE_SQL, trades, trades.size(),
						new ParameterizedPreparedStatementSetter<Trade>() {
							@Override
							public void setValues(PreparedStatement ps, Trade argument) throws SQLException {
								ps.setString(1, argument.getOrderNo());
								ps.setInt(2, argument.getMerchantId());
								ps.setBigDecimal(3, argument.getTradeAmount());
								ps.setNull(4, Types.INTEGER);
								ps.setString(5, argument.getPayeeName());
								ps.setString(6, argument.getPayeeBankCard());
								ps.setString(7, argument.getPayeeBankCode());
								if (null != argument.getRemark()) {
									ps.setString(8, argument.getRemark());
								} else {
									ps.setString(8, "");
								}
								ps.setString(9, argument.getBatchNo());
								if (null != argument.getPayeeBankName()) {
									ps.setString(10, argument.getPayeeBankName());
								} else {
									ps.setString(10, "");
								}
								if (null != argument.getPayeeBankAcctType()) {
									ps.setInt(11, argument.getPayeeBankAcctType());
								} else {
									ps.setNull(11, Types.INTEGER);
								}
								if (null != argument.getMerchantFeeAmount()) {
									ps.setBigDecimal(12, argument.getMerchantFeeAmount());
								} else {
									ps.setNull(12, Types.DECIMAL);
								}
								if (null != argument.getLoanBenefit()) {
									ps.setBigDecimal(13, argument.getLoanBenefit());
								} else {
									ps.setNull(13, Types.DECIMAL);
								}
								if (null != argument.getOrgBenefit()) {
									ps.setBigDecimal(14, argument.getOrgBenefit());
								} else {
									ps.setNull(14, Types.DECIMAL);
								}
								ps.setInt(15, loaning);
							}
						});
			}
		} catch (IOException e) {
			log.error("解析批量文件异常:{}",e);
			errorDetails.append("异常信息:解析文件异常");
		} catch (Exception e){
			log.error("系统错误：{}",e);
			errorDetails.append("异常信息：系统错误");
		}finally {
			 try {
				bufferedReader.close();
				read.close();
			    is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return errorDetails.toString();
	}

	public List<BatchTradeDTO> getBatchTradeList(Integer status, Date startTime, Date endTime,
			Integer userMerchantId, String userMerchantType) {
		return batchTradeMapper.getBatchTradeList(status, startTime, endTime, userMerchantId, userMerchantType);
	}
	
	/**
	 * 审批通过后，发起批量代付
	 * @param batchTrade
	 * @param smsCode
	 * @throws Exception
	 */
	public void audit(BatchTrade batchTrade, String smsCode) throws Exception {
		if(batchTrade.getStatus() == 2) {
			BigDecimal total = batchTradeHandler.audit(batchTrade, smsCode);
			batchTrade = batchTradeMapper.selectByPrimaryKey(batchTrade.getId());
			BatchPayTradeResVO resVO = batchTradeHandler.invokeBatchPay(batchTrade);
			batchTradeHandler.dealBatchPayTradeRes(resVO, batchTrade.getBatchNo(), batchTrade.getPayerMerchantId(), total);
		} else {
			batchTradeHandler.auditNo(batchTrade, smsCode);
		}
	}
	
	/**
	 *  根据批次号查询批量处理状态
	 * @param batchNo
	 * @param merchantId
	 * @return
	 */
	public BatchTradeDTO getBatchByBatchNo(String batchNo,Integer merchantId) {
		 return batchTradeMapper.queryBatchByBatchNo(batchNo, merchantId);
	}
	
	/**
	 * 获取短信验证码
	 * @param batchNo
	 * @param payerMerchantId
	 * @return
	 */
	public String getSmsCode(String batchNo, Integer payerMerchantId) {
		
		String smsCode = null;
		Random rand = new Random();
		StringBuffer sbCode = new StringBuffer();
		for (int j = 0; j < 6; j++) {
			sbCode.append(sources.charAt(rand.nextInt(9)) + "");
		}
		smsCode = sbCode.toString();
		MerchantBaseInfo info = merchantBaseInfoMapper.selectByPrimaryKey(payerMerchantId);
		SendSmsResponse response = smsService.sendPaySms(info.getMobile(), smsCode);
		PaySms paySms = new PaySms();
		paySms.setBatchNo(batchNo);
		paySms.setCreateTime(new Date());
		paySms.setSmsCode(smsCode);
		paySms.setSmsBizId(response.getBizId());
		paySms.setVerfiyResult(SmsConstant.VERIFY_NO);
		paySms.setVerifyTimes(0);
		log.info("生成验证码{}", paySms);
		paySmsMapper.insert(paySms);
		return smsCode;
	}
	
	/**
	 * 处理批量代付结果
	 * @param batchNo
	 * @param merchantId
	 * @throws Exception 
	 */
	@Transactional
	public String dealBatchTrade(String batchNo, Integer merchantId, BatchPayTradeQueryResVO resVO) throws Exception {
		List<Trade> trades = tradeMapper.selectByBatchNo(batchNo);
		BigDecimal totalAmount = BigDecimal.ZERO;
		for(Trade trade : trades) {
			totalAmount = totalAmount.add(trade.getTradeAmount()).
					add(add(trade.getMerchantFeeAmount(), trade.getLoanBenefit(), trade.getOrgBenefit()));
		}
		if(resVO.getStatus() != null && 0 == resVO.getStatus()) {
			batchTradeMapper.updateTradeStatus(TradeConstant.BATCH_STATUS_SUCCESS, batchNo);
			//TODO依次处理批次文件
			Map<String, Trade> tradeMap = ConvertUtil.convertTradeMap(trades);
			for(TradeDTO tradeDTO : resVO.getTrades()) {
				Trade trade = tradeMap.get(tradeDTO.getSeqNo());
				trade.setStatus(tradeDTO.getStatus());
				trade.setChannelId(resVO.getChannelId());
			}
			List<Integer> merchantIds = new ArrayList<Integer>();
			Integer orgId = null;
			Integer loanId = null;
			merchantIds.add(merchantId);
			merchantIds.add(1);//平台账户
			if(trades.get(0).getOrgBenefit() != null) {
				MerchantBaseInfo baseInfo =  merchantBaseInfoMapper.selectByPrimaryKey(merchantId);
				if(baseInfo.getOrgId() != null) {
					orgId = baseInfo.getOrgId();
					merchantIds.add(baseInfo.getOrgId());
				}
			}
			if(trades.get(0).getLoanBenefit() != null) {
				MerchantRouteConf conf = merchantRouteConfMapper.selectByMerchantIdAndChannelId(trades.get(0).getMerchantId(), trades.get(0).getChannelId());
				if(conf != null && conf.getLoaningOrgId() != null) {
					loanId = conf.getLoaningOrgId();
					merchantIds.add(conf.getLoaningOrgId());
				}
			}
			List<MerchantPrepayInfo> infos = merchantPrepayInfoMapper.lockByMerchantIds(merchantIds);
			Map<Integer, MerchantPrepayInfo> maps = ConvertUtil.convertMap(infos);
			for(Trade trade : trades) {
				/** 商户资金变动 */
				prepayInfoService.insertPrepayInfoJournal(maps.get(trade.getMerchantId()), TradeConstant.TRADE_FEE, trade.getTradeAmount(), TradeConstant.CREDIT, trade.getId());			
				/** 商户手续费资金变动 */
				prepayInfoService.insertPrepayInfoJournal(maps.get(trade.getMerchantId()), TradeConstant.HADNING_FEE, trade.getMerchantFeeAmount(), TradeConstant.CREDIT, trade.getId());
				BigDecimal platFee = trade.getMerchantFeeAmount();
				/** 机构资金变动 */
				if(orgId != null) {
					prepayInfoService.insertPrepayInfoJournal(maps.get(orgId), TradeConstant.HADNING_FEE, trade.getOrgBenefit(), TradeConstant.DEBIT, trade.getId());
					platFee = platFee.subtract(trade.getOrgBenefit());
				}
				/** 垫资机构资金变动 */
				if(loanId != null) {
					prepayInfoService.insertPrepayInfoJournal(maps.get(loanId), TradeConstant.HADNING_FEE, trade.getLoanBenefit(), TradeConstant.DEBIT, trade.getId());
					platFee = platFee.subtract(trade.getLoanBenefit());
				}
				/** 平台资金变动 */
				prepayInfoService.insertPrepayInfoJournal(maps.get(1), TradeConstant.HADNING_FEE, platFee, TradeConstant.DEBIT, trade.getId());
				tradeMapper.updateStatus(trade);
			}
			prepayInfoService.updatePrepayInfos(maps.values());
			return "批量代付成功";
		} else if(resVO.getStatus() != null && 1 == resVO.getStatus()) {
			batchTradeMapper.updateTradeStatus(TradeConstant.BATCH_STATUS_FAIL, batchNo);
			//触发失败，修改交易状态为失败
			tradeMapper.updateStatusByBatchNo(batchNo,
					resVO.getRespMsg(), resVO.getRespCode(), TradeConstant.STATUS_FAIL, new Date());
			prepayInfoService.unfreezePrepayInfo(merchantId, totalAmount);
			return "批量代付失败";
		} 
		return "批量代付处理中";
	}
}
