package com.global.adk.active.record.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlBinder {
	
	/** 插入 */
	String insert() default "";
	
	/** 更新 */
	String update() default "";
	
	/** 删除 */
	String delete() default "";
	
}