package com.cloud.pay.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.common.entity.City;

public interface CityMapper {
	
    List<City> selectByPid(@Param("pid")Integer pid);

    City select(@Param("cid")Integer cid, @Param("pid")Integer pid);
}