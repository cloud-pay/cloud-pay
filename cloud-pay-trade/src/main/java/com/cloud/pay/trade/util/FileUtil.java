package com.cloud.pay.trade.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.cloud.pay.trade.entity.Trade;

/**
 * 批量付款文件
 * 
 * @author dbnaxlc
 *
 */
public class FileUtil {

	public static String createFile(List<Trade> trades, String batchNo) throws IOException {
		String fileName = "/home/batch/" + batchNo + ".txt";
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		try (FileOutputStream out = new FileOutputStream(file, true)) {
			for (Trade trade : trades) {
				StringBuffer sb = new StringBuffer();
				sb.append(trade.getId()).append("~").append(trade.getPayeeBankCard()).append("~")
						.append(trade.getPayeeName()).append("~").append(trade.getPayeeBankCode()).append("~CNY~")
						.append(trade.getTradeAmount()).append("~").append(trade.getRemark())
						.append(System.getProperty("line.separator"));
				out.write(sb.toString().getBytes("utf-8"));
			}
		}
		return fileName;
	}
}
