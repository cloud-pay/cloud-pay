package com.cloud.pay.common.utils.validation;

import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValueValidator implements ConstraintValidator<DateValue, String> {

	private String format;

	@Override
	public void initialize(DateValue constraintAnnotation) {
		format = constraintAnnotation.format();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || "".equals(value.trim())){return true;}
		try{  
	        SimpleDateFormat formatter = new SimpleDateFormat(format);  
	        formatter.parse(value);  
	    }catch(Exception e){  
	        return false;  
	    }  
		return true;

	}

}
