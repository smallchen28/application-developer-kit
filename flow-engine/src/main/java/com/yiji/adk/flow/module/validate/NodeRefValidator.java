/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module.validate;

import com.yiji.adk.flow.module.NodeRef;
import com.yjf.common.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-7 下午12:58<br>
 * @see
 * @since 1.0.0
 */
public class NodeRefValidator implements ConstraintValidator<NodeRefConstraint, NodeRef> {
	
	@Override
	public void initialize(NodeRefConstraint constraintAnnotation) {
		//nothing ...
	}
	
	@Override
	public boolean isValid(NodeRef value, ConstraintValidatorContext context) {
		//            context.disableDefaultConstraintViolation();
		//            context.buildConstraintViolationWithTemplate("{XXX}").addConstraintViolation();
		return value != null && StringUtils.isNotBlank(value.getName());
	}
}
