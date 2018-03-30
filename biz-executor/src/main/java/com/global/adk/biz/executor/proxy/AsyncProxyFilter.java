package com.global.adk.biz.executor.proxy;

import java.util.concurrent.Executor;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;

import com.global.adk.biz.executor.InvokeElement;
import com.global.adk.common.exception.KitNestException;
import com.global.adk.common.exception.SystemException;
import com.global.adk.common.log.TraceLogFactory;

/**
 * 异步代理过滤器
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月19日 下午1:17:52<br>
 * @see
 * @since 1.0.0
 */
public class AsyncProxyFilter extends ProxyFilterBase {
	
	private final Executor executor;
	
	private final Logger logger;
	
	private final InvokeElement invokeElement;
	
	public AsyncProxyFilter(int order, InvokeElement element, Executor executor) {
		
		super(order);
		this.executor = executor;
		this.invokeElement = element;
		this.logger = TraceLogFactory.getLogger(invokeElement.getLogName());
	}
	
	@Override
	public Object proceed(final MethodInvocation methodInvocation) {
		
		try {
			if (invokeElement.isAsync() && executor != null) {
				
				executor.execute(new Runnable() {
					
					@Override
					public void run() {
						
						try {
							methodInvocation.proceed();
						} catch (Throwable e) {
							if (e instanceof SystemException) {
								String message = e.getMessage();
								String errorCode = ((SystemException) e).getErrorCode();
								
								logger.error("异步任务执行过程中出现错误(外部应答为success),message={},errorCode={}",
									new Object[] { message, errorCode });
							} else if (e instanceof KitNestException) {
								logger.error("异步任务执行过程中出现错误->{}", e.getMessage());
							} else {
								logger.error("异步任务执行过程中出现错误", e);
							}
						}
					}
				});
				
			} else {
				methodInvocation.proceed();
			}
		} catch (Throwable e) {
			throwException(e);
		}
		return null;
	}
	
}
