/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.annotation;

import java.lang.annotation.*;

/**
 * Executor处理器返回参数定义出错，例如：Object、Void、void、String func(Execution execution)
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-25 下午2:05<br>
 * @see
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Executor {
}
