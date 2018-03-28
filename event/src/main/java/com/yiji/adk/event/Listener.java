package com.yiji.adk.event;
/**
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 *
 * 修订记录： lingxu@yiji.com 2016/9/13 11:11 创建
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface Listener {
	
	/**
	 * Notifier beanName
	 *
	 * default:notifierBus
	 */
	String notifier() default "";
}
