package com.cloud.pay.common.utils;

/**
 * 获取不同表对应的code编码 工具类
 * @author xiaohl
 *
 */
public class TableCodeUtils {

	/**
	 * code编码数字的长度
	 */
	public static final int CODE_LENGTH = 32;
	
	/**
	 * 获取表对应的code
	 * @param id 表主键id
	 * @param tableCode 表前缀
	 * @return
	 */
	public static String getTableCode(int id,String startWith){
		String code = "";
		String idStr = String.valueOf(id);
	    String fullStr = startWith + idStr;
		if(fullStr.length()<CODE_LENGTH){
			//如果长度不够，补0
			int distance = CODE_LENGTH - fullStr.length(); //计算相差的位数
			String zeroStr = "";
			for(int i=0;i<distance;i++){
				zeroStr = zeroStr+"0";
			}
			code = startWith + zeroStr + idStr;
		}else{
			code = startWith + idStr;
		}
		return code;
	}
	
	public static void main(String[] args) {
		System.out.println(getTableCode(20000, "20181201").length());
	}

}
