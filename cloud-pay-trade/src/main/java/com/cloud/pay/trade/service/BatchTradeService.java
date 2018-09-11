package com.cloud.pay.trade.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.Base64;
import com.cloud.pay.trade.dto.BatchTradeDTO;
import com.cloud.pay.trade.entity.BatchTrade;
import com.cloud.pay.trade.mapper.BatchTradeMapper;

@Service
public class BatchTradeService {

	@Autowired
	private BatchTradeMapper batchTradeMapper;
	
	public void upload(BatchTrade batchTrade, String payFilePath) {
		 try {
			 byte[] bytes = Base64.base64ToByteArray(payFilePath);
				InputStream excel = new ByteArrayInputStream(bytes);
	                Workbook wb = new HSSFWorkbook(excel);
	                //开始解析
	                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

	                int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
	                int lastRowIndex = sheet.getLastRowNum();
	                System.out.println("firstRowIndex: "+firstRowIndex);
	                System.out.println("lastRowIndex: "+lastRowIndex);

	                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
	                    System.out.println("rIndex: " + rIndex);
	                    Row row = sheet.getRow(rIndex);
	                    if (row != null) {
	                        int firstCellIndex = row.getFirstCellNum();
	                        int lastCellIndex = row.getLastCellNum();
	                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
	                            Cell cell = row.getCell(cIndex);
	                            if (cell != null) {
	                                System.out.println(cell.getStringCellValue());
	                            }
	                        }
	                    }
	                }
	                wb.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	public List<BatchTradeDTO> getBatchTradeList(Integer status, Date startTime, Date endTime) {
		return batchTradeMapper.getBatchTradeList(status, startTime, endTime);
	}
}
