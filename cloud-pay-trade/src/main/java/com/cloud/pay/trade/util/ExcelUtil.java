package com.cloud.pay.trade.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.Base64;
import com.cloud.pay.trade.entity.Trade;
import com.cloud.pay.trade.exception.TradeException;

public class ExcelUtil {
	
	private static Logger log =LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 读取手工代付文件
	 * @param payFilePath
	 * @param batchNo
	 * @param loaning
	 * @param payerMerchantId
	 * @return
	 */
	public static List<Trade> readExcel(String payFilePath, String batchNo, Integer loaning,
			Integer payerMerchantId) {
		StringBuilder errorDetails = new StringBuilder();
		Workbook wb = null;
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
			int seqNo = 1;
			for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) { // 遍历行
				Row row = sheet.getRow(rIndex);
				if (row != null) {
					trade = new Trade();
					trade.setBatchNo(batchNo);
					trade.setLoaning(loaning);
					trade.setMerchantId(payerMerchantId);
					String orderNo = UUID.randomUUID().toString();
					trade.setOrderNo(orderNo.replace("-", ""));
					int firstCellIndex = row.getFirstCellNum();
					int lastCellIndex = row.getLastCellNum();
					for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) { // 遍历列
						Cell cell = row.getCell(cIndex);
						switch (cIndex) {
						case 0:
							if (cell == null) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							String cellValue = cell.getStringCellValue();
							if (StringUtils.isBlank(cellValue)) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							trade.setPayeeName(cellValue);
							break;
						case 1:
							if (cell != null) {
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
							}
						case 2:
							if (cell == null) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							String acctNo = null;
							try{
								double temp = cell.getNumericCellValue();
								acctNo = String.valueOf(Double.valueOf(temp).longValue());
							} catch(Exception e) {
								acctNo = cell.getStringCellValue();
							}
							if (StringUtils.isBlank(acctNo)) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							trade.setPayeeBankCard(acctNo);
							break;
						case 3:
							if (cell == null) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							String bankCode = null;
							try{
								double temp = cell.getNumericCellValue();
								bankCode = String.valueOf(Double.valueOf(temp).longValue());
							} catch(Exception e) {
								bankCode = cell.getStringCellValue();
							}
							if (StringUtils.isBlank(bankCode)) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							trade.setPayeeBankCode(bankCode);
							break;
						case 4:
							if (cell != null) {
								String bankName = cell.getStringCellValue();
								trade.setPayeeBankName(bankName);
								break;
							}
						case 5:
							if (cell == null) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列为空!  ");
								continue;
							}
							double amount = cell.getNumericCellValue();
							if (amount <= 0) {
								errorDetails.append((rIndex + 1) + "行" + (cIndex + 1) + "列格式错误!");
								continue;
							}
							trade.setTradeAmount(new BigDecimal(amount));
							break;
						case 6:
							if (cell != null) {
								String remark = cell.getStringCellValue();
								trade.setRemark(remark);
							}
							break;
						default:
							break;
						}
					}
					trade.setSeqNo(seqNo++);
					trades.add(trade);
				}
			} 
			if (errorDetails.length() != 0) {
				log.info("读取手工代付文件格式异常，{}", errorDetails.toString());
				throw new TradeException(errorDetails.toString(), null);
			}
			return trades;
		} catch (Exception e) {
			log.warn("读取手工代付文件异常，{}", e);
			throw new TradeException("异常信息:" + e.getMessage(), null);
		} finally {
			try {
				if(wb != null)
					wb.close();
			} catch (IOException e) {
				log.warn("关闭文件流异常异常，{}", e);
				log.warn("忽略该异常");
			}
		}
	} 
}
