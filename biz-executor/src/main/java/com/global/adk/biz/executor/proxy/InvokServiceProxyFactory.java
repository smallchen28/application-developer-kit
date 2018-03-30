/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.proxy;

import com.google.common.collect.Sets;
import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.biz.executor.InvokeElement;
import com.global.adk.biz.executor.InvokeService;
import com.global.adk.common.exception.InitializerException;
import com.global.adk.common.exception.KitNestException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * InvokService代理工厂
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月18日 上午11:20:31<br>
 */
@SuppressWarnings("rawtypes")
public class InvokServiceProxyFactory {
	
	private final PlatformTransactionManager transactionManager;
	
	private final DBPlugin dbPlugin;
	
	private final InvokeElement targetElement;
	
	private final Executor executor;
	
	private final Set<ProxyFilter> proxyFilters = Sets.newTreeSet();

	private MethodInterceptor activityExecutorInterceptor = new MethodInterceptor() {
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			
			return interceptor(invocation);
		}
	};
	
	public InvokServiceProxyFactory(InvokeElement targetElement, Executor executor,
									PlatformTransactionManager transactionManager, DBPlugin dbPlugin) {
		this.targetElement = targetElement;
		this.transactionManager = transactionManager;
		this.dbPlugin = dbPlugin;
		this.executor = executor;
		initProxyFilter(targetElement);
	}
	
	private void initProxyFilter(InvokeElement element) {
		proxyFilters.add(new AsyncProxyFilter(1, element, executor));
		proxyFilters.add(new TransactionProxyFilter(2, element, transactionManager));
		proxyFilters.add(new SerialProxyFilter(3, element, dbPlugin));
	}
	
	public InvokeService createInvokServiceProxy() {
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(targetElement.getInvokeService());
		NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor(activityExecutorInterceptor);
		advisor.setMappedName("invoke");
		factory.addAdvisor(advisor);
		return (InvokeService) factory.getProxy();
	}
	
	protected Object interceptor(final MethodInvocation invocation) {
		Object reuslt = null;
		try {
			MethodInvocation last = invocation;
			//- 利用递归调用实现过滤器、使之不再具备状态性，当然也没有那么灵活了详见CommandChain实现方式。
			for (final ProxyFilter filter : proxyFilters) {
				final MethodInvocation next = last;
				last = new MethodInvocation() {
					
					@Override
					public Object proceed() throws Throwable {
						return filter.proceed(next);
					}
					
					@Override
					public Object getThis() {
						return invocation.getThis();
					}
					
					@Override
					public AccessibleObject getStaticPart() {
						return invocation.getStaticPart();
					}
					
					@Override
					public Object[] getArguments() {
						return invocation.getArguments();
					}
					
					@Override
					public Method getMethod() {
						return invocation.getMethod();
					}
				};
			}
			reuslt = last.proceed();
		} catch (Throwable e) {
			if (e instanceof KitNestException) {
				throw (KitNestException) e;
			}
			throw new InitializerException("系统内部执行出错", e);
		}
		return reuslt;
	}
	
}
