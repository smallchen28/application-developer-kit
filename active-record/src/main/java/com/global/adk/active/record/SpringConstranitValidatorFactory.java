/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

import com.global.adk.common.exception.DomainException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * spring支持
 *
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年7月25日
 * @version 1.0.0
 * @see
 */
public class SpringConstranitValidatorFactory implements ConstraintValidatorFactory {
	
	private final AutowireCapableBeanFactory beanFactory;
	
	public SpringConstranitValidatorFactory(AutowireCapableBeanFactory beanFactory) {
		if (beanFactory == null) {
			throw new DomainException("错误的构建ValidatiorFactory，beanFactory不可为空。");
		}
		this.beanFactory = beanFactory;
	}
	
	@Override
	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
		return beanFactory.createBean(key);
	}
	
	public void releaseInstance(ConstraintValidator<?, ?> instance) {
		this.beanFactory.destroyBean(instance);
	}
	
}
