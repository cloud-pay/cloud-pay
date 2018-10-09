package com.cloud.pay.common.mapper;

import com.cloud.pay.common.entity.SysConfig;

public interface SysConfigMapper {

	SysConfig selectByPrimaryKey(String sysKey);

}