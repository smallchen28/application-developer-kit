/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.event;

import java.lang.annotation.*;

/**
 * 事件订阅方法注解
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 *
 * @history hasuelee创建于2014年9月30日 下午1:54:07<br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
	boolean isAsync() default false;
	
	int priority() default 0;
}
