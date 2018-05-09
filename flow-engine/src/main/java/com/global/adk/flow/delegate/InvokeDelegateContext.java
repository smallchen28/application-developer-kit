/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.delegate;

import com.google.common.collect.Maps;
import com.global.adk.common.exception.FlowException;
import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.Flow;
import com.yjf.common.util.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-19 下午2:20<br>
 * @see
 * @since 1.0.0
 */
public class InvokeDelegateContext extends AbstractInvokeDelegate implements InvokeSupport {
	
	private final ApplicationContext applicationContext;
	
	private Map<Flow.Key, Map<Key, InvokeDelegate>> invokeDelegateArrayHolder = Maps.newConcurrentMap();
	
	public InvokeDelegateContext(ApplicationContext applicationContext) {
		super(null);
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object invoke(Object[] args) {
		Object result = null;
		
		//-1. 参数组合为args[0]=StateMachine.Key，args[1]=nodeName，args[2]=Annotation , args[3]=Execution
		Flow.Key key = (Flow.Key) args[0];
		String nodeName = (String) args[1];
		Class<Annotation> annotationType = (Class<Annotation>) args[2];
		Execution execution = (Execution) args[3];
		
		//-2. 根据定义获取相关集合
		Map<Key, InvokeDelegate> invokeDelegateHolder = invokeDelegateArrayHolder.get(key);
		
		Key mKey = new Key(key.getFlowName(), key.getVersion(), nodeName, annotationType);
		InvokeDelegate invokeDelegate = invokeDelegateHolder.get(mKey);
		
		//-3. 获取到执行InvokeDelegate
		if (invokeDelegate != null) {
			result = invokeDelegate.invoke(new Object[] { execution });
		}
		
		return result;
	}
	
	@Override
	public void proceed(Flow.Key flowKey, String nodeName, String target) {
		
		if (StringUtils.isNotBlank(target)) {
			if (target == Object.class.getName()) {
				throw new FlowException(String.format("targetClass为Object类型，还搞个喵！"));
			}
			
			Map<Key, InvokeDelegate> invokeHolder = invokeDelegateArrayHolder.get(flowKey);
			
			if (invokeHolder == null) {
				invokeHolder = Maps.newConcurrentMap();
				invokeDelegateArrayHolder.put(flowKey, invokeHolder);
			}
			
			try {
				Class targetType = Class.forName(target);
				
				//-1. 查找spring bean
				Map<String, Object> targets = applicationContext.getBeansOfType(targetType);
				Iterator<Map.Entry<String, Object>> targetsIterator = targets.entrySet().iterator();
				
				if (!targetsIterator.hasNext()) {
					throw new FlowException(String.format(
						"流程定义Flow=%s,,version=%s处理器类型分析失败，类型为targetType=%s的处理器不存在相应的spring bean定义",
						flowKey.getFlowName(), flowKey.getVersion(), targetType));
				}
				if (targets.size() != 1) {
					throw new FlowException(String.format(
						"流程定义Flow=%s,,version=%s处理器类型分析失败，类型为targetType=%s的处理器定义存在多个实现版本..beans=%s",
						flowKey.getFlowName(), flowKey.getVersion(), targetType, targets));
				}
				Object targetInstance = targetsIterator.next().getValue();
				//对Aop进行特殊处理,屏蔽所有的动态代理。
				//因为爷现在不想在代码生成部分去获取目标对象进行转换，更重要的是爷不想大家在节点触发器上去开启什么@Transactional注解
				if(AopUtils.isAopProxy(targetInstance)){
					throw new FlowException(String.format(
							"流程定义Flow=%s,,version=%s处理器类型分析失败，类型为targetType=%s的处理器是一个动态代理对象->(%s)",
							flowKey.getFlowName(), flowKey.getVersion(), targetType,targetInstance));
				}
				
				//-2. 反射处理
				Class<?> supperType = targetType;
				
				do {
					for (Method method : supperType.getDeclaredMethods()) {
						Annotation[] annotations = method.getDeclaredAnnotations();
						for (Annotation annotation : annotations) {
							InvokeCoder coder = InvokeCoder.coder(annotation);
							//其他注解存在的情况下，就会出现null值
							if (coder != null) {
								Key key = new Key(flowKey.getFlowName(), flowKey.getVersion(), nodeName,
									annotation.annotationType());
								
								if (!invokeHolder.containsKey(key)) {
									InvokeDelegate invokeDelegate = coder.create(key, targetInstance, method);
									invokeHolder.put(key, invokeDelegate);
								}
							}
						}
					}
				} while ((supperType = supperType.getSuperclass()) != Object.class);
				
			} catch (Exception e) {
				if(e instanceof FlowException){
					throw (FlowException)e;
				}
				throw new FlowException(String.format("Flow=%s,Version=%s处理器类型分析失败,targetClasses=%s",
					flowKey.getFlowName(), flowKey.getVersion(), target), e);
			}
		}
	}
	
	public static class Key {
		
		private String flowName;
		
		private int version;
		
		private String nodeName;
		
		private Class<? extends Annotation> annotationType;
		
		public Key(String flowName, int version, String nodeName, Class<? extends Annotation> annotationType) {
			this.flowName = flowName;
			this.version = version;
			this.nodeName = nodeName;
			this.annotationType = annotationType;
		}
		
		public String getFlowName() {
			return flowName;
		}
		
		public int getVersion() {
			return version;
		}
		
		public String getNodeName() {
			return nodeName;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			
			Key key = (Key) o;
			
			if (version != key.version) {
				return false;
			}
			if (annotationType != null ? !annotationType.equals(key.annotationType) : key.annotationType != null) {
				return false;
			}
			if (nodeName != null ? !nodeName.equals(key.nodeName) : key.nodeName != null) {
				return false;
			}
			if (flowName != null ? !flowName.equals(key.flowName) : key.flowName != null) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public int hashCode() {
			int result = flowName != null ? flowName.hashCode() : 0;
			result = 31 * result + version;
			result = 31 * result + (nodeName != null ? nodeName.hashCode() : 0);
			result = 31 * result + (annotationType != null ? annotationType.hashCode() : 0);
			return result;
		}
	}
}
