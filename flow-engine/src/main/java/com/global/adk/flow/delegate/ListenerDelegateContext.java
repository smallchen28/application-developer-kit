/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.delegate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.global.adk.common.compiler.Compiler;
import com.global.adk.common.exception.FlowException;
import com.global.adk.flow.annotation.Listen;
import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.EventListener;
import com.global.adk.flow.module.Flow;
import com.global.common.util.StringUtils;
import javassist.CtClass;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-30 上午10:58<br>
 * @see
 * @since 1.0.0
 */
public class ListenerDelegateContext implements ListenerDelegate {
	
	private ApplicationContext applicationContext;
	
	private Map<Flow.Key, Map<String, List<AbstractListenerDelegate>>> listenerHolder = Maps.newConcurrentMap();
	
	public ListenerDelegateContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void action(Execution execution, String eventName) {
		Flow flow = execution.getCurrentFlow();
		Flow.Key key = new Flow.Key(flow.getName(), flow.getVersion());
		
		Map<String, List<AbstractListenerDelegate>> eventListeners = listenerHolder.get(key);
		
		if (eventListeners == null || eventListeners.size() == 0) {
			return;
		}
		
		List<AbstractListenerDelegate> listenerDelegates = eventListeners.get(eventName);
		if (listenerDelegates == null || listenerDelegates.size() == 0) {
			return;
		}
		
		for (AbstractListenerDelegate each : listenerDelegates) {
			each.action(execution, eventName);
		}
	}
	
	public void register(Flow flow, EventListener eventListener) {
		
		Flow.Key key = new Flow.Key(flow.getName(), flow.getVersion());
		
		//获取所有事件对应监听器
		Map<String, List<AbstractListenerDelegate>> eventListeners = listenerHolder.get(key);
		
		if (eventListeners == null) {
			eventListeners = Maps.newConcurrentMap();
			listenerHolder.put(key, eventListeners);
			
			for (int i = 0, j = flow.getEvents().size(); i < j; i++) {
				String event = flow.getEvents().get(i);
				//不要使用treeset,这个场景有问题。。。。
				List<AbstractListenerDelegate> listenerDelegates = Lists.newArrayList();
				eventListeners.put(event, listenerDelegates);
			}
		}
		
		String description = eventListener.getDescription();
		String eventListenerClass = eventListener.getListenerClass();
		try {
			Class<?> eventListenerType = Class.forName(eventListenerClass);
			
			//-1. 查找spring bean
			Map<String, Object> listenerBeans = (Map<String, Object>) applicationContext
				.getBeansOfType(eventListenerType);
			
			Iterator<Map.Entry<String, Object>> listenerBeansIterator = listenerBeans.entrySet().iterator();
			
			if (!listenerBeansIterator.hasNext()) {
				throw new FlowException(String.format(
					"Flow=%s,Version=%s处理器类型分析失败，类型为listenerClass=%s的监听器不存在相应的spring bean定义", key.getFlowName(),
					key.getVersion(), eventListenerClass));
			}
			if (listenerBeans.size() != 1) {
				throw new FlowException(String.format(
					"Flow=%s,Version=%s处理器类型分析失败，类型为listenerClass=%s的监听器定义存在多个实现版本..beans=%s", key.getFlowName(),
					key.getVersion(), eventListenerClass, listenerBeans));
			}
			Object listenerBean = listenerBeansIterator.next().getValue();


			if(AopUtils.isAopProxy(listenerBean)){
				throw new FlowException(String.format(
						"流程定义Flow=%s,,version=%s事件监听器分析失败，类型为targetType=%s的监听器是一个动态代理对象->(%s)",
						flow.getName(), flow.getVersion(), ((Advised) listenerBean).getTargetSource().getTarget().getClass().getName(), listenerBean));
			}

			//-3. 反射处理
			Class<?> supperType = eventListenerType;
			
			do {
				for (Method method : supperType.getDeclaredMethods()) {
					if (method.isAnnotationPresent(Listen.class)) {
						Listen listen = method.getAnnotation(Listen.class);
						String eventExpression = listen.eventExpression();
						int priority = listen.priority();
						AbstractListenerDelegate delegate = dynamicBuilder(key, listenerBean, method, priority);
						
						if (eventExpression.equals("*")) {//eventExpression为 * 时视为全部监听
							for (Iterator<Map.Entry<String, List<AbstractListenerDelegate>>> it = eventListeners
								.entrySet().iterator(); it.hasNext();) {
								addAndSort(it.next().getValue(), delegate);
							}
						}else{
							boolean isBreak = false;
							for (String event : flow.getEvents()) {
								if (event.matches(eventExpression)) { //此处处理正则表达式
									addAndSort(eventListeners.get(event),delegate);
									isBreak = true;
								}
							}
							if(!isBreak && eventExpression.matches("^(!\\S+|\\S+)(,!\\S+|\\S+)+$")){
								String[] eventExpressions = eventExpression.split(",");
								for (String event : flow.getEvents()) {
									for (String expression : eventExpressions) {
										if(!expression.startsWith("!")){ //取反直接忽略
											if (StringUtils.isNotBlank(expression) && event.equals(expression)) { //取反匹配成功表示不符合
												isBreak = true;
												break;
											}
										}
									}
									if (isBreak) {
										addAndSort(eventListeners.get(event), delegate);
										isBreak = false;
									}
								}
							}
						}
					}
				}
			} while ((supperType = supperType.getSuperclass()) != Object.class);
		} catch (Exception e) {
			if(e instanceof FlowException){
				throw (FlowException)e;
			}
			throw new FlowException(String.format("非法的监听器定义ListenerClass=%s,Description=%s,StateMachine=%s,Version=%s",
				eventListenerClass, description, key.getFlowName(), key.getVersion()),e);
		}
	}


	private void addAndSort(List<AbstractListenerDelegate> delegates,AbstractListenerDelegate delegate){
		if(!delegates.contains(delegate)){  //去重……………………………………………………
			delegates.add(delegate);
			Collections.sort(delegates);
		}
	}

	private AbstractListenerDelegate dynamicBuilder(Flow.Key key, Object listenerBean, Method method, int priority) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		
		Class<?> returnType = method.getReturnType();
		
		valid(key, listenerBean, method, parameterTypes, returnType);
		
		com.global.adk.common.compiler.Compiler compiler = Compiler.getInstance();
		CtClass ctClass = compiler.newCtClass(AbstractListenerDelegate.class);
		
		StringBuilder constructScript = new StringBuilder();
		constructScript.append("public ").append(ctClass.getSimpleName()).append("(Object target,int priority){\n\t")
			.append("super(target,priority);\n").append("}\n");
		
		StringBuilder actionScript = new StringBuilder();

		actionScript
			.append("public void action(com.global.adk.flow.engine.Execution execution , String eventName){\n\t")
				.append("((").append(listenerBean.getClass().getName()).append(")getTarget()).").append(method.getName())
			.append("(execution,eventName);\n}\n");
		
		compiler.constructImplement(ctClass, constructScript.toString());
		
		return compiler.methodWeave(ctClass, AbstractListenerDelegate.class, actionScript.toString()).newInstance(
			ctClass, new Class[] { Object.class, int.class }, new Object[] { listenerBean, priority });
		
	}
	
	private void valid(Flow.Key key, Object listenerBean, Method method, Class<?>[] parameterTypes, Class<?> returnType) {
		//void fun(Execution execution , String eventName)
		if (parameterTypes.length != 2 || (parameterTypes[0] != Execution.class && parameterTypes[1] != String.class)) {
			throw new FlowException(
				String
					.format(
						"Flow=%s,version=%s,listener=%s，method=%s,parameterTypes=%s,入参定义出错，例如：void fun(Execution execution , String eventName)",
						key.getFlowName(), key.getVersion(), listenerBean.getClass().getName(), method.getName(),
						Arrays.toString(parameterTypes)));
		}
		
		//returnType = void
		if (returnType != void.class && returnType != Void.class) {
			throw new FlowException(
				String
					.format(
						"Flow=%s,version=%s,listener=%s，method=%s,returnType=%s,返回参数定义出错，例如：void func(Execution execution,String eventName) ",
						key.getFlowName(), key.getVersion(), listenerBean.getClass().getName(), method.getName(),
						returnType.getName()));
		}
		
	}
}
