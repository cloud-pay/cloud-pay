package com.cloud.pay.trade.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.Base64;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.cloud.pay.channel.service.ICloudApiService;
import com.cloud.pay.channel.vo.BatchPayTradeQueryReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeQueryResVO;
import com.cloud.pay.channel.vo.BatchPayTradeReqVO;
import com.cloud.pay.channel.vo.BatchPayTradeResVO;
import com.cloud.pay.common.entity.SysConfig;
import com.cloud.pay.common.mapper.SysConfigMapper;
import com.cloud.pay.common.utils.OSSUnit;
import com.cloud.pay.merchant.entity.MerchantBankInfo;
import com.cloud.pay.merchant.entity.MerchantBaseInfo;
import com.cloud.pay.merchant.mapper.MerchantBankInfoMapper;
import com.cloud.pay.merchant.mapper.MerchantBaseInfoMapper;
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
import com.cloud.pay.trade.util.FileUtil;


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
			+ "merchant_fee_amount,loan_benefit,org_benefit) "
			+ "values (?, ?, ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
	
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
	private ICloudApiService payService;

	@SuppressWarnings({ "null", "deprecation" })
	@Transactional
	public String upload(BatchTrade batchTrade, String payFilePath) {
		StringBuilder errorDetails = new StringBuilder();
		Workbook wb = null;
		int count = 0;
		BigDecimal totalAmount = BigDecimal.ZERO;
		try {
			byte[] bytes = Base64.base64ToByteArray(payFilePath);
			InputStream excel = new ByteArrayInputStream(bytes);
			wb = new HSSFWorkbook(excel);
			// 开始解析
			Sheet sheet = wb.getSheetAt(0); // 读取sheet 0
			int firstRowIndex = sheet.getFirstRowNum() + 1; // 第一行是列名，所以不读
			int lastRowIndex = sheet.getLastRowNum();
			System.out.println("firstRowIndex: " + firstRowIndex);
			System.out.println("lastRowIndex: " + lastRowIndex);
			final List<Trade> trades = new ArrayList<Trade>();
			Trade trade = null;
			MerchantBankInfo bankInfo = merchantBankInfoMapper.selectByMerchantId(batchTrade.getPayerMerchantId());
			for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) { // 遍历行
				Row row = sheet.getRow(rIndex);
				if (row != null) {
					trade = new Trade();
					trade.setBatchNo(batchTrade.getBatchNo());
					trade.setMerchantId(batchTrade.getPayerMerchantId());
					if (bankInfo != null) {
						trade.setPayerId(bankInfo.getId());
					}
					String orderNo = UUID.randomUUID().toString();
					trade.setOrderNo(orderNo.replace("-", ""));
					int firstCellIndex = row.getFirstCellNum();
					int lastCellIndex = row.getLastCellNum();
					for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) { // 遍历列
						Cell cell = row.getCell(cIndex);
						if (cell == null && cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
							break;
						}
						switch (cIndex) {
						case 0:
							String cellValue = cell.getStringCellValue();
							if (StringUtils.isBlank(cellValue)) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							trade.setPayeeName(cellValue);
							break;
						case 1:
							String type = cell.getStringCellValue();
							if (StringUtils.isNotBlank(type)) {
								if ("企业".equals(type)) {
									trade.setPayeeBankAcctType(1);
								} else if ("个人".equals(type)) {
									trade.setPayeeBankAcctType(2);
								} else {
									errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列格式不正确，只能为企业/个人!  ");
								}
								continue;
							}
							break;
						case 2:
							String acctNo = cell.getStringCellValue();
							if (StringUtils.isBlank(acctNo)) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							trade.setPayeeBankCard(acctNo);
							break;
						case 3:
							String bankCode = cell.getStringCellValue();
							if (StringUtils.isBlank(bankCode)) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							trade.setPayeeBankCode(bankCode);
							break;
						case 4:
							String bankName = cell.getStringCellValue();
							if (StringUtils.isBlank(bankName)) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							trade.setPayeeBankName(bankName);
							break;
						case 5:
							double amount = cell.getNumericCellValue();
							if (amount <= 0) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列格式错误!");
								continue;
							}
							trade.setTradeAmount(new BigDecimal(amount));
							break;
						case 6:
							String remark = cell.getStringCellValue();
							trade.setRemark(remark);
							break;
						default:
							break;
						}
					}
					trades.add(trade);
					count++;
					totalAmount = totalAmount.add(trade.getTradeAmount());
				}
			}
			if (errorDetails.length() == 0) {
				//逐笔计算手续费
				for(Trade temp : trades) {
					BigDecimal merchantFee = BigDecimal.ZERO;
					BigDecimal[] fees = payHandler.getFee(trade.getMerchantId(), trade.getTradeAmount());
					merchantFee = fees[0];
					if(TradeConstant.LOANING_YES == trade.getLoaning()) {
						//计算垫资分润
						temp.setLoanBenefit(fees[1]);
					}
					temp.setMerchantFeeAmount(merchantFee.add(trade.getLoanBenefit()));
					BigDecimal orgFee = payHandler.getOrgFee(trade.getMerchantId(), trade.getTradeAmount());
					temp.setOrgBenefit(merchantFee.subtract(orgFee));
				} 
				batchTrade.setTotalAmount(totalAmount);
				batchTrade.setTotalCount(count);
				batchTradeMapper.insert(batchTrade);
				// TODO 批量新增
				jdbcTemplate.batchUpdate(TRADE_SQL, trades, trades.size(),
						new ParameterizedPreparedStatementSetter<Trade>() {
							@Override
							public void setValues(PreparedStatement ps, Trade argument) throws SQLException {
								ps.setString(1, argument.getOrderNo());
								ps.setInt(2, argument.getMerchantId());
								ps.setBigDecimal(3, argument.getTradeAmount());
								ps.setInt(4, argument.getPayerId());
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
							}
						});
				//判断商户余额
				BigDecimal total = BigDecimal.ZERO;
				for(Trade temp : trades) {
					total.add(add(temp.getMerchantFeeAmount(), temp.getLoanBenefit(), temp.getOrgBenefit()));
				}
				try {
					prepayInfoService.freezePrepayInfo(batchTrade.getPayerMerchantId(), total.add(totalAmount));
				} catch(Exception e1) {
					log.warn("冻结商户余额异常：", e1);
					errorDetails.append(e1.getMessage());
					//修改批量交易失败
					tradeMapper.updateStatusByBatchNo(batchTrade.getBatchNo(),
							e1.getMessage(), TradeConstant.PREPAY_BALANCE_NO_ENOUGH, TradeConstant.STATUS_FAIL, new Date());
					return errorDetails.toString();
				}
				//发送渠道
				BatchPayTradeReqVO reqVO = new BatchPayTradeReqVO();
				reqVO.setFileName(FileUtil.createFile(trades, batchTrade.getBatchNo()));
				reqVO.setMerchantId(batchTrade.getPayerMerchantId());
				reqVO.setOrderNo(batchTrade.getBatchNo());
				reqVO.setTotalAmt(totalAmount);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				reqVO.setTradeDate(sdf.format(new Date()));
				reqVO.setTotalNum(Long.valueOf(count));
				log.info("批量付款入参：{}", reqVO);
				BatchPayTradeResVO resVO = payService.batchPay(reqVO);
				log.info("批量付款結果：{}", resVO);
				if("1".equals(resVO.getRespCode())) {
					//触发失败，修改交易状态为失败
					tradeMapper.updateStatusByBatchNo(batchTrade.getBatchNo(),
							resVO.getRespMsg(), resVO.getRespCode(), TradeConstant.STATUS_FAIL, new Date());
					prepayInfoService.unfreezePrepayInfo(batchTrade.getPayerMerchantId(), total.add(totalAmount));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorDetails.append("异常信息:" + e.getMessage());
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return errorDetails.toString();
	}
	
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
	public String batchPay(BatchTrade batchTrade, String fileFullPath,String mchCode) {
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
		InputStream is = OSSUnit.getOSS2InputStream(OSSUnit.getOSSClient(accessKeyIdConfig.getSysValue(),secretAccessKeyConfig.getSysValue()), mchCode, fileFullPath);
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
				
				String[] line = lineTxt_cr.split("~");
				//获取商户信息（考虑机构发批量是发给多个商户发起）
				String merchantNo = line[0]; //商户号
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
				// TODO 批量新增
				jdbcTemplate.batchUpdate(TRADE_SQL, trades, trades.size(),
						new ParameterizedPreparedStatementSetter<Trade>() {
							@Override
							public void setValues(PreparedStatement ps, Trade argument) throws SQLException {
								ps.setString(1, argument.getOrderNo());
								ps.setInt(2, argument.getMerchantId());
								ps.setBigDecimal(3, argument.getTradeAmount());
								ps.setInt(4, argument.getPayerId());
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

	public List<BatchTradeDTO> getBatchTradeList(Integer status, Date startTime, Date endTime) {
		return batchTradeMapper.getBatchTradeList(status, startTime, endTime);
	}
	
	@Transactional
	public void audit(BatchTrade batchTrade, String smsCode) {
		batchTradeMapper.audit(batchTrade);
		if(batchTrade.getStatus() == 2) {
			//验证短信验证码
			PaySms sms = paySmsMapper.selectByBatchNo(batchTrade.getBatchNo());
			log.info("手工代付短信验证码信息：{}", sms);
			SysConfig config = sysConfigMapper.selectByPrimaryKey("verifyMaxTimes");
			int verifyMaxTimes = Integer.parseInt(config.getSysValue());
			config = sysConfigMapper.selectByPrimaryKey("expiryTime");
			int expiryTime = Integer.parseInt(config.getSysValue());
			if(sms.getVerfiyResult() != SmsConstant.VERIFY_SUCCESS 
					&& sms.getVerifyTimes() < verifyMaxTimes
					&& getDatePoor(new Date(), sms.getVerifyTime(), expiryTime)) {
				sms.setVerifyTimes(1 + sms.getVerifyTimes());
				sms.setVerifyTime(new Date());
				if(!smsCode.equals(sms.getSmsCode())) {
					log.info("手工代付短信验证码校验未通过");
					sms.setVerfiyResult(SmsConstant.VERIFY_FAIL);
					throw new TradeException("验证码不通过", SmsConstant.VERIFY_FAIL_CODE);
				} else {
					sms.setVerfiyResult(SmsConstant.VERIFY_SUCCESS);
				}
				paySmsMapper.updateVerifyResult(sms);
			} else {
				throw new TradeException("验证码不通过", SmsConstant.VERIFY_FAIL_CODE);
			}
			tradeMapper.updateByBatchNo(new Date(), 1, batchTrade.getBatchNo());
			//TODO 异步发起批量交易
		} else {
			//审核不通过，则交易置为失败
			tradeMapper.updateByBatchNo(new Date(), 3, batchTrade.getBatchNo());
		}
		
	}
	
	private boolean getDatePoor(Date endDate, Date nowDate, int expiryDate) {
		if(nowDate == null) {
			return true;
		}
	    long diff = endDate.getTime() - nowDate.getTime();
	    return diff < 1000l * 60 * expiryDate;
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
		SendSmsResponse response = SmsService.sendPaySms(info.getMobile(), smsCode);
		PaySms paySms = new PaySms();
		paySms.setBatchNo(batchNo);
		paySms.setCreateTime(new Date());
		paySms.setSmsCode(smsCode);
		paySms.setSmsBizId(response.getBizId());
		paySms.setVerfiyResult(SmsConstant.VERIFY_NO);
		paySms.setVerifyTimes(0);
		paySmsMapper.insert(paySms);
		return smsCode;
	}
	
	public void searchBatchTrade(String batchNo, Integer merchantId) {
		BatchPayTradeQueryReqVO reqVO = new BatchPayTradeQueryReqVO();
		reqVO.setBatchOrderNo(batchNo);
		reqVO.setFileName("/home/batch/" + batchNo + ".txt");
		reqVO.setMerchantId(merchantId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		reqVO.setTradeDate(sdf.format(new Date()));
		BatchPayTradeQueryResVO resVO = payService.batchPayQuery(reqVO);
		log.info("批量查询结果：{}", resVO);
		//TODO依次处理批次文件
	}
}
