package com.cloud.pay.common.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerValueValidator implements ConstraintValidator<IntegerValue, Integer> {
  
	private int[] values;
	
	@Override
	public void initialize(IntegerValue constraintAnnotation) {
		 values = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if(null == value)return true;
		for(int val:values) {
			if(value == val) {
				return true;
			}
		}
		return false;
	}

	

}