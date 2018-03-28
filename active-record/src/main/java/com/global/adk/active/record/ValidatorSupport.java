/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.validation.*;
import javax.validation.spi.ValidationProvider;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年7月25日
 * @version 1.0.0
 * @see
 */
public class ValidatorSupport implements InitializingBean, ApplicationContextAware {
	
	private Class<ValidationProvider> validationProviderClass;
	
	private ApplicationContext applicationContext;
	
	private MessageInterpolator messageInterpolator;
	
	private ValidatorFactory validatorFactory;
	
	private Validator validator;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
		
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		//- 构建Jsr303配置信息
		Configuration config = (this.validationProviderClass == null ? Validation.byDefaultProvider().configure()
			: Validation.byProvider(validationProviderClass).configure());
		//- 错误信息定义(直接使用进行自定义ResourceBundleMessageInterpolator , PlatformResourceBundleLocator)
		if (messageInterpolator == null) {
			this.messageInterpolator = config.getDefaultMessageInterpolator();
		}
		config.messageInterpolator(this.messageInterpolator);
		
		config.constraintValidatorFactory(new SpringConstranitValidatorFactory(this.applicationContext
			.getAutowireCapableBeanFactory()));
		
		this.validatorFactory = config.buildValidatorFactory();
		
		this.validator = this.validatorFactory.getValidator();
	}
	
	public void setValidationProviderClass(Class<ValidationProvider> validationProviderClass) {
		
		this.validationProviderClass = validationProviderClass;
	}
	
	public void setMessageInterpolator(final String validationMessage) {
		
		if (validationMessage != null && !validationMessage.equals("")) {
			this.messageInterpolator = new ResourceBundleMessageInterpolator(new ResourceBundleLocator() {
				
				@Override
				public ResourceBundle getResourceBundle(Locale locale) {
					
					return ResourceBundle.getBundle(validationMessage, locale, Thread.currentThread()
						.getContextClassLoader());
				}
			});
		}
	}
	
	public Validator getValidator() {
		
		return this.validator;
	}
}
