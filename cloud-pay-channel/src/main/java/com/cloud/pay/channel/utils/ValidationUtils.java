package com.cloud.pay.channel.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;

import com.cloud.pay.common.contants.ChannelErrorCode;
import com.cloud.pay.common.exception.CloudApiExcetion;

/**
 * 使用hibernate注解来校验接口基本参数
 * @author wangy
 */
public class ValidationUtils {
  
	 private static Validator validator = Validation
	            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
     
	 public static <T> void validate(T obj) throws CloudApiExcetion{
	        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
	        // 抛出检验异常
	        if (constraintViolations.size() > 0) {
	            throw new CloudApiExcetion(ChannelErrorCode.ERROR_0001, String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage()));
	        }
	    }
}
