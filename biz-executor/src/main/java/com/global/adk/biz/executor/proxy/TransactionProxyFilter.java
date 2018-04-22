/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.proxy;

import com.global.adk.biz.executor.InvokeElement;
import com.global.adk.biz.executor.annotation.Invoke;
import com.global.adk.common.exception.NestError;
import com.global.adk.common.exception.SuspendException;
import com.global.adk.common.exception.SystemException;
import com.global.adk.common.log.TraceLogFactory;
import com.global.common.log.Logger;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 事务代理过滤器
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月19日 下午1:15:38<br>
 */
public class TransactionProxyFilter extends ProxyFilterBase {
	
	private InvokeElement element;
	
	private PlatformTransactionManager transactionManager;
	
	public TransactionProxyFilter(int order, InvokeElement element, PlatformTransactionManager transactionManager) {
		
		super(order);
		this.element = element;
		this.transactionManager = transactionManager;
	}
	
	@Override
	public Object proceed(MethodInvocation methodInvocation) {
		//- 检查上锁是否与事务冲突
		Invoke.TransactionAttribute transactionAttribute = element.getTransactionAttribute();
		Invoke.SerialLock lock = element.getSerialLock();
		
		if (transactionAttribute == null && (lock != null && lock.isLock())) {
			throw new NestError(String.format("错误TransactionAttribute->(%s)与SerialLock->(%s)配置", transactionAttribute,
				lock));
		}
		
		//需要事务或需要串行上锁。
		if (transactionManager != null && ((lock != null && lock.isLock()) || transactionAttribute.isTx())) {
			TransactionStatus status = null;
			Throwable execThrowable = null;

			try{
				TransactionDefinition definition = createDefinition(element);
				status = transactionManager.getTransaction(definition);
				methodInvocation.proceed();
			}catch(Throwable e){
				execThrowable = e;
			}finally{
				try{
					if(status != null) {

						//-1. 对SuspendException挂起操作忽略回滚。
						if(execThrowable == null || execThrowable instanceof SuspendException){
							transactionManager.commit(status);
							return null;
						}

						//-2.  对notRollbackFor列表中的异常忽略回滚。
						Class<? extends SystemException>[] classes = transactionAttribute.notRollbackFor();
						if(classes != null){
							for(int i = 0 , j = classes.length ; i < j ; i++){
								if(classes[i].equals(execThrowable.getClass())){
									transactionManager.commit(status);
									return null;
								}
							}

						}
						transactionManager.rollback(status);
					}
				}catch(Throwable e){
					if(execThrowable == null){
						execThrowable = e;
					}else{
						execThrowable.addSuppressed(e);
					}
				}finally{
					if(execThrowable != null){
						throwException(execThrowable);
					}
				}
			}
		} else {
			try {
				methodInvocation.proceed();
			} catch (Throwable e) {
				throwException(e);
			}
		}
		return null;
	}

	
	private TransactionDefinition createDefinition(InvokeElement element) {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		Invoke.TransactionAttribute transactionAttribute = element.getTransactionAttribute();
		definition.setIsolationLevel(transactionAttribute.isolation());
		definition.setPropagationBehavior(transactionAttribute.propagation());
		definition.setReadOnly(transactionAttribute.isReadOnly());
		definition.setTimeout(transactionAttribute.timeout());
		return definition;
	}
	
}
