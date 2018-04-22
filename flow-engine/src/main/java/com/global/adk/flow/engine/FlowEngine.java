/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.engine;

import com.google.common.collect.Maps;
import com.global.adk.common.exception.FlowException;
import com.global.adk.flow.delegate.InvokeDelegateContext;
import com.global.adk.flow.delegate.ListenerDelegateContext;
import com.global.adk.flow.delegate.MvelScriptContext;
import com.global.adk.flow.module.*;
import com.global.common.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Iterator;
import java.util.Map;

/**
 * 流程引擎
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-9 上午11:10<br>
 * @see
 * @since 1.0.0
 */
public class FlowEngine implements InitializingBean, ApplicationContextAware {
	
	private InvokeDelegateContext invokeDelegateContext;
	
	private ListenerDelegateContext listenerDelegateContext;
	
	private MvelScriptContext mvelScriptContext;
	
	private Map<Flow.Key, Flow> FlowsHolder = Maps.newHashMap();
	
	private Map<Flow.Key, ExceptionMonitor> FlowExceptionMonitorHolder = Maps.newHashMap();
	
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		invokeDelegateContext = new InvokeDelegateContext(applicationContext);
		mvelScriptContext = new MvelScriptContext(applicationContext);
		listenerDelegateContext = new ListenerDelegateContext(applicationContext);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void registry(Flow flow) {
		//-1. 检查完整性、合法性
		flow.validate();
		
		//-2.构建填充（StateNodeRef、StateMachineRef）
		flow.initialize(null);
		
		//-3. 重复检查
		Flow.Key key = new Flow.Key(flow.getName(), flow.getVersion());
		if (FlowsHolder.containsKey(key)) {
			throw new FlowException(String.format("重复的流程定义Flow=%s,Version=%s", flow.getName(), flow.getVersion()));
		}
		//-4. 初始化监听器
		ListenersInitializer(flow);
		
		//-5. 初始化处理器
		actionInitializer(flow);
		
		//-6. 初始化错误监视器
		monitorInitializer(flow);
		
		//-7. 添加定义
		FlowsHolder.put(key, flow);
		
	}
	
	private void ListenersInitializer(Flow flow) {
		EventListeners eventListeners = flow.getEventListeners();
		
		if (eventListeners != null) {
			for (EventListener eventListener : eventListeners.listeners()) {
				listenerDelegateContext.register(flow, eventListener);
			}
		}
	}
	
	private void monitorInitializer(Flow flow) {
		ErrorMonitor monitor = flow.getErrorMonitor();
		
		//为空是允许滴~
		if (monitor != null) {
			String monitorClass = monitor.getErrorMonitorClass();
			try {
				Class<ExceptionMonitor> monitorType = (Class<ExceptionMonitor>) Class.forName(monitorClass);
				
				Map<String, ExceptionMonitor> monitorMap = applicationContext.getBeansOfType(monitorType);
				
				Iterator<Map.Entry<String, ExceptionMonitor>> monitorIterator = monitorMap.entrySet().iterator();
				
				if (!monitorIterator.hasNext()) {
					throw new FlowException(String.format(
						"流程定义Flow=%s,Version=%s错误监视器没有获取到正确的spring bean定义,并且ErrorMonitor必须为ExceptionMonitor实现。",
						flow.getName(), flow.getVersion(), monitorClass));
				}
				
				FlowExceptionMonitorHolder.put(new Flow.Key(flow.getName(), flow.getVersion()),
					monitorIterator.next().getValue());
			} catch (ClassNotFoundException e) {
				throw new FlowException(String.format("流程定义Flow=%s,Version=%s错误监视器定义出错MonitorClass=%s", flow.getName(),
					flow.getVersion(), monitorClass));
			}
		}
		
	}
	
	private void actionInitializer(Flow flow) {
		Flow.Key key = new Flow.Key(flow.getName(), flow.getVersion());
		
		//-1. 开始节点/结束节点
		invokeDelegateContext.proceed(key, flow.getStartNode().getName(), flow.getStartNode().getTriggerClass());
		invokeDelegateContext.proceed(key, flow.getEndNode().getName(), flow.getEndNode().getTriggerClass());
		
		//-2. 对其他节点进行处理（trigger、mvelScript）
		for (ActivityNode node : flow.getNodes()) {
			String triggerClass = node.getTriggerClass();
			String mvelScript = node.getCondition().getMvelScript();
			invokeDelegateContext.proceed(key, node.getName(), triggerClass);
			mvelScriptContext.proceed(key, node.getName(), mvelScript);
		}
		
	}
	
	public Execution execute(String flowName, String activeNode, int version, Object target, Map<String, Object> args) {
		Flow flow = obtain(flowName, version);
		
		FlowNode node = null;
		Execution execution = new Execution(this, flow, target, args);
		if (StringUtils.isBlank(activeNode)) {//从定义执行
			node = flow;
		} else {// 从指定节点执行
			String startNodeName = flow.getStartNode().getName();
			String endNodeName = flow.getEndNode().getName();
			
			if (activeNode.equals(startNodeName)) {
				node = flow.getStartNode();
			} else if (activeNode.equals(endNodeName)) {
				node = flow.getEndNode();
			} else {
				for (ActivityNode nd : flow.getNodes()) {
					if (activeNode.equals(nd.getName())) {
						node = nd;
						break;
					}
				}
			}
		}
		
		if (node == null) {
			throw new FlowException(
				String.format("指定执行节点不存在Flow=%s,Version=%s,activeNode=%s", flow, version, activeNode));
		}
		
		execution.execute(flow, node);
		return execution;
	}
	
	private Flow obtain(String flowName, int version) {
		Flow flow;
		//如果小于0则选取最低版本version=1作为潜规则。
		Flow.Key key = new Flow.Key(flowName, version <= 0 ? 1 : version);
		
		flow = FlowsHolder.get(key);
		if (flow == null) {
			throw new FlowException(String.format("流程定义不存在Flow=%s,version=%s", flowName, version));
		}
		
		return flow;
	}
	
	public Map<Flow.Key, ExceptionMonitor> getFlowExceptionMonitorHolder() {
		return FlowExceptionMonitorHolder;
	}
	
	public MvelScriptContext getMvelScriptContext() {
		return mvelScriptContext;
	}
	
	public InvokeDelegateContext getInvokeDelegateContext() {
		return invokeDelegateContext;
	}
	
	public ListenerDelegateContext getListenerDelegateContext() {
		return listenerDelegateContext;
	}
}
