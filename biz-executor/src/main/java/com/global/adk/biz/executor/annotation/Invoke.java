/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * ligen@yiji.com 2014-09-17 13:36 创建
 * qzhanbo@yiji.com 2014-12-01 21:51 修改
 *
 */
package com.global.adk.biz.executor.annotation;

import com.global.adk.common.exception.SystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

import javax.validation.groups.Default;
import java.lang.annotation.*;

/**
 * 执行描述
 *
 * @author ligen@yiji.com
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface Invoke {
	
	/**
	 * 服务名称
	 */
	String serviceName();
	
	/**
	 * 日志名称,如果不填,选择服务类名InvokeSerivce前面部分
	 */
	String logName() default "";
	
	/**
	 * jsr303校验分组策略
	 */
	Class[] validateGroup() default { Default.class };
	
	/**
	 * 领域模型类型
	 */
	Class<?> entityType() default Void.class;
	
	/**
	 * 实体对象是否注入spring bean.
	 * <p/>
	 * 如果启用此参数.可以在实体对象中使用
	 * {@link org.springframework.beans.factory.annotation.Autowired}注入spring
	 * bean.
	 *
	 * @return
	 */
	boolean isEntityInjectSpringBeans() default false;
	
	/**
	 * 是否异步执行
	 */
	boolean isAsync() default false;
	
	/**
	 * 事务支持
	 */
	TransactionAttribute transactionAttribute() default @TransactionAttribute();
	
	/**
	 * 是否上锁、此步骤必须带事务，将忽略isTransaction参数
	 */
	SerialLock lock() default @SerialLock();
	
	public @interface SerialLock {
		boolean isLock() default false;
		
		boolean isNowaite() default false;
		
		String policy() default "";
		
		String module() default "";
		
		String lock() default "";
	}
	
	/**
	 * @see TransactionDefinition
	 */
	public @interface TransactionAttribute {
		boolean isTx() default true;
		
		int propagation() default TransactionDefinition.PROPAGATION_REQUIRED;
		
		int isolation() default TransactionDefinition.ISOLATION_DEFAULT;
		
		int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;
		
		boolean isReadOnly() default false;

		Class<? extends SystemException>[] notRollbackFor() default {} ;
	}
}
