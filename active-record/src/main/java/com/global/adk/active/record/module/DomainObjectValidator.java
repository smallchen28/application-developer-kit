/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record.module;

import com.global.adk.active.record.ValidatorSupport;
import com.global.adk.common.exception.DomainException;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年7月24日
 * @version 1.0.0
 * @see
 */
public abstract class DomainObjectValidator extends DomainObject {
	
	private static final long serialVersionUID = 7919490992852883701L;
	
	private ValidatorSupport validatorSupport;
	
	public DomainObjectValidator() {
	}
	
	public void setValidatorSupport(ValidatorSupport validatorSupport) {
		
		this.validatorSupport = validatorSupport;
	}
	
	public void validate(Class<?>... groups) {

		if(validatorSupport == null){
			throw new DomainException(String.format("检查器ValidatorSupport尚未初始化，不支持的操作..."));
		}
		
		if (groups == null || groups.length == 0) {
			groups = new Class<?>[] { Default.class };
		}
		
		Set<ConstraintViolation<DomainObjectValidator>> constraintViolations = validatorSupport.getValidator()
			.validate(this, groups);
		
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<DomainObjectValidator> constraintViolation : constraintViolations) {
			sb.append(constraintViolation.getPropertyPath()).append(":").append(constraintViolation.getMessage())
				.append(",");
		}

		if(sb.length() > 0){
			throw new DomainException(String.format("业务参数检查出错:%s", sb.toString()));
		}

	}

	public ValidatorSupport validatorSupport(){
		return validatorSupport;
	}
}
