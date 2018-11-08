package com.cloud.pay.common.mapper;

import java.util.List;

import com.cloud.pay.common.entity.Provincial;

public interface ProvincialMapper {
    
    List<Provincial> selectList();

    Provincial selectByPrimaryKey(Integer pid);

    
}