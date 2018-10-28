package com.cloud.pay.common.utils.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

/**
 * 日期格式验证,可以在{@code format}中添加有效的日期格式
 * @author Canaan
 * @date 2017年3月8日
 */
@Documented
@Constraint(validatedBy = { DateValueValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface DateValue {
	
	String message() default "日期格式不正确";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * 有效的日期格式
	 */
	String format() default "yyyyMMdd HH:mm:ss";  
	
	/**
	 * Defines several {@link DateValue} annotations on the same element.
	 *
	 * @see com.yhbc.validation.DateValue.DateValue
	 */
	@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		DateValue[] value();
	}

}
