/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.annotation;

import java.lang.annotation.*;

/**
 *
 * 监听器定义void func(Execution execution,String eventName)
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-29 上午11:14<br>
 * @see
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listen {

	/**
	 * 事件监听表达式。
	 * <ul>
	 * <li>1. 提供正则表达式支持，例如“\\S+”任意非空字符串事件。</li>
	 * <li>2. 提供支持，表示任意事件</li>
	 * <li>3. 提供"!"取反支持，表示排除</li>
	 * <li>4. 提供集合支持，例如："!error,!start_process",表示排除error和start_process外所有事件</li>
	 * </ul>
	 * @return
	 */
	String eventExpression() default "*";

	/** 执行优先级,越小越大，如果出现相当，则以定义顺序为准。*/
	int priority() default 0;
}
