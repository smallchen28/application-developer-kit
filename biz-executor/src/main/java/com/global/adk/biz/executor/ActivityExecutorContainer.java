/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" .  "88
 *                            (|  -_-  |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *
 *  www.yiji.com Inc.
 *  Copyright (c) 2011 All Rights Reserved.
 */

package com.global.adk.biz.executor;

import com.google.common.base.Strings;
import com.global.adk.active.record.DomainFactory;
import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.biz.executor.annotation.Invoke;
import com.global.adk.biz.executor.event.InitEvent;
import com.global.adk.biz.executor.event.ServiceApplyEvent;
import com.global.adk.biz.executor.event.ServiceFinishEvent;
import com.global.adk.biz.executor.monitor.ExceptionMonitor;
import com.global.adk.biz.executor.monitor.StandardExceptionMonitor;
import com.global.adk.biz.executor.proxy.InvokServiceProxyFactory;
import com.global.adk.biz.executor.regcode.RegistryCodeVerify;
import com.global.adk.biz.executor.statement.*;
import com.global.adk.common.exception.InitializerException;
import com.global.adk.event.NotifierBus;
import com.global.common.lang.context.OperationContext;
import com.global.common.lang.result.StandardResultInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 活动执行器容器，彻底改版之前biz层通用实现，培根你好，培根再见.(￣▽￣)
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月16日 下午12:54:21<br>
 * @see
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public class ActivityExecutorContainer implements ExecutorContainer, InitializingBean {

	/**
	 * 服务名后缀
	 */
	private static final String INVOKE_SERVICE_SUFFIX = "InvokeService";
	
	private NotifierBus notifierBus;
	
	private DBPlugin dbPlugin;
	
	private ExceptionMonitor monitor;
	
	private DomainFactory domainFactory;
	
	private PlatformTransactionManager transactionManager;
	
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	private RegistryCodeVerify registryCodeVerify;
	
	private Map<String, InvokeElement> invokElements = new ConcurrentHashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		initCheck();
		registerListener();
		notifierInitializer();
	}
	
	private void notifierInitializer() {
		
		notifierBus.dispatcher(new Object[] { new InitEvent(this) });
	}
	
	private void registerListener() {
		
		notifierBus.register(new MDCStatement());
		notifierBus.register(new LogStatement());
		notifierBus.register(new PreparedStatement());
		notifierBus.register(new ValidateStatement());
		notifierBus.register(new ThreadHolderStatement());
	}
	
	private void initCheck() {
		
		if (registryCodeVerify == null) {
			registryCodeVerify = new RegistryCodeVerify() {
				@Override
				public boolean validate(String serviceName, Object parameter) {
					return true;
				}
			};
		}
		
		if (monitor == null) {
			monitor = new StandardExceptionMonitor("ERROR", "ADK_SYSTEM");
		}
		
		notifierBus = new NotifierBus(threadPoolTaskExecutor);
		
		if (dbPlugin == null)
			throw new InitializerException("初始化ActivityExecutorContianer过程出错,dbPlugin=null");
			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <Param, R extends StandardResultInfo> R accept(	Param parameter, String serviceName,
															OperationContext operationContext) {
															
		//- 构建服务上下文
		ServiceContext<Param, R> serviceContext = new ServiceContext(operationContext, parameter);
		
		try {
			//- 1. 系统时间、验证器，db错误不再特殊处理。
			serviceContext.setCurrentTimestamp(dbPlugin.currentTimestamp());
			serviceContext.setBegin(System.currentTimeMillis());
			serviceContext.setRegistryCodeVerify(registryCodeVerify);
			
			//- 2. 获取服务定义invokServiceElement
			InvokeElement invokServiceElement = invokElements.get(serviceName);
			
			//- 3. 获取invok服务
			if (invokServiceElement == null) {
				throw new InitializerException(String.format("服务->%s没有正确配置InvokService", serviceName));
			}
			serviceContext.setInvokeElement(invokServiceElement);
			
			//- 4. 服务申请事件,检查事件,预处理事件
			ServiceApplyEvent applyEvent = new ServiceApplyEvent(this, serviceContext);
			notifierBus.dispatcher(applyEvent);
			
			//- 5. 业务前置处理
			invokServiceElement.getInvokeService().before(serviceContext);
			
			//- 6. 执行invok
			invokServiceElement.getInvokeService().invoke(serviceContext);
			
			//- 7. 后续处理
			invokServiceElement.getInvokeService().after(serviceContext);
			
		} catch (Throwable ex) {
			monitor.catcher(ex, serviceContext);
		} finally {
			InvokeElement invokeElement = serviceContext.getInvokeElement();
			if (invokeElement != null) {
				try {
					invokeElement.getInvokeService().end(serviceContext);
				} catch (Throwable ex) {
					org.slf4j.Logger logger = serviceContext.getLogger();
					if (logger.isErrorEnabled()) {
						logger.error("业务{}执行最终操作(#end)时出错", invokeElement.getServiceName(), ex);
					}
				} finally {
					ServiceFinishEvent finishEvent = new ServiceFinishEvent(this, serviceContext);
					notifierBus.dispatcher(finishEvent);
				}
			}
		}
		return serviceContext.result();
	}
	
	@SuppressWarnings({ "unchecked" })
	public void registerInvockService(InvokeService invokeService) {
		checkInvoke(invokeService);
		Class<InvokeService> invokServiceClass = (Class<InvokeService>) invokeService.getClass();
		Invoke invoke = invokServiceClass.getAnnotation(Invoke.class);
		String serviceName = invoke.serviceName();
		Class[] validateGroups = invoke.validateGroup();
		String resultClass = getResultClassName(invokServiceClass);
		Class entityClass = getEntityClass(invoke.entityType());
		boolean isAsync = invoke.isAsync();
		boolean isEntityInjectSpringBeans = invoke.isEntityInjectSpringBeans();
		Invoke.TransactionAttribute transactionAttribute = invoke.transactionAttribute();
		Invoke.SerialLock serialLock = invoke.lock();
		String logName = getLogName(invokeService, invoke);
		
		//- 构建InvokElement
		InvokeElement invokeElement = InvokeElementFactory.getInstance().newInstance(serviceName, validateGroups,
			logName, invokeService, entityClass, resultClass, isAsync, serialLock, transactionAttribute,
			isEntityInjectSpringBeans);
			
		//- 代理设置
		invokeElement.setInvokeService(
			new InvokServiceProxyFactory(invokeElement, threadPoolTaskExecutor, transactionManager, dbPlugin)
				.createInvokServiceProxy());
				
		invokElements.put(serviceName, invokeElement);
	}
	
	private String getLogName(InvokeService invokeService, Invoke invoke) {
		String logName = invoke.logName();
		if (Strings.isNullOrEmpty(logName)) {
			String invokeServiceName = invokeService.getClass().getSimpleName();
			if (invokeServiceName.endsWith(INVOKE_SERVICE_SUFFIX)) {
				logName = invokeServiceName.substring(0, invokeServiceName.lastIndexOf(INVOKE_SERVICE_SUFFIX));
			} else {
				logName = invokeServiceName;
			}
		}
		return logName;
	}
	
	private void checkInvoke(InvokeService invokeService) {
		Class<InvokeService> invokServiceClass = (Class<InvokeService>) invokeService.getClass();
		Invoke invoke = invokServiceClass.getAnnotation(Invoke.class);
		if (invoke == null) {
			throw new InitializerException(
				String.format("InvokService->%s配置错误,@invok注解不可为空", invokeService.getInvockServiceName()));
		}
		String serviceName = invoke.serviceName();
		InvokeElement ori = invokElements.get(serviceName);
		if (ori != null) {
			throw new InitializerException(
				String.format("InvokService->服务名冲突%s和%s服务名都为%s", ori.getInvokeService(), invokeService, serviceName));
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	private Class getEntityClass(Class entityClass) {
		Class targetClass = null;
		if (!(entityClass == null || entityClass == Void.class || entityClass == void.class)) {
			targetClass = entityClass;
		}
		return targetClass;
	}
	
	private String getResultClassName(Class<InvokeService> invokServiceClass) {
		
		try {
			
			do {
				
				Type genericType = invokServiceClass.getGenericSuperclass();
				
				if (genericType instanceof ParameterizedType) {
					Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
					if (types == null || types.length != 2) {
						throw new InitializerException(
							String.format("InvockService->%s范型配置错误", invokServiceClass.getName()));
					}
					//- 应答部分1.void；2.Generic；3.Class
					Type resultType = types[1];
					String typeName = null;
					if (resultType instanceof Class) {
						typeName = ((Class<?>) types[1]).getName();
						if (typeName == Void.class.getName() || typeName == void.class.getName()) {
							typeName = null;
						}
					} else if (resultType instanceof ParameterizedType) {
						typeName = ((Class<?>) ((ParameterizedType) resultType).getRawType()).getName();
					}
					
					return typeName;
				}
				
				invokServiceClass = (Class<InvokeService>) invokServiceClass.getSuperclass();
				
			} while (InvokeService.class.isAssignableFrom(invokServiceClass));
			
			throw new InitializerException(String.format("InvockService->%s范型配置错误", invokServiceClass.getName()));
			
		} catch (SecurityException e) {
			throw new InitializerException(String.format("InvockService->%s范型配置错误", invokServiceClass.getName()));
		}
	}
	
	public void setDbPlugin(DBPlugin dbPlugin) {
		
		this.dbPlugin = dbPlugin;
	}
	
	public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}
	
	public void setDomainFactory(DomainFactory domainFactory) {
		
		this.domainFactory = domainFactory;
	}
	
	public DomainFactory getDomainFactory() {
		
		return domainFactory;
	}
	
	public void setMonitor(ExceptionMonitor monitor) {
		
		this.monitor = monitor;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		
		this.transactionManager = transactionManager;
	}
	
	public ExceptionMonitor getMonitor() {
		
		return monitor;
	}
	
	public void setRegistryCodeVerify(RegistryCodeVerify registryCodeVerify) {
		
		this.registryCodeVerify = registryCodeVerify;
	}
	
}
