package com.global.adk.api.annotation;
/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * @author lingxu (e-mail:wjiayin@yiji.com) 2017-05-24 10:37 创建
 *
 */

import java.lang.annotation.*;

/**
 * 与{@link DubboServiceAPI}配合使用,用来关联相应的Invoke
 *
 * @author lingxu (e-mail:wjiayin@yiji.com)
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeName {
	
	/**
	 * 使用Invoke的serviceName作为取值.详见:com.yiji.adk.biz.executor.annotation.Invoke#
	 * serviceName()
	 *
	 */
	String value();
}
