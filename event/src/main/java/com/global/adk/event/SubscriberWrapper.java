package com.global.adk.event;

import com.global.adk.common.compiler.Compiler;
import com.global.adk.common.exception.EventException;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 事件订阅方法委托实现
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月30日 下午1:54:07<br>
 */
public abstract class SubscriberWrapper implements Comparable<SubscriberWrapper> {
	
	private static final Logger logger = LoggerFactory.getLogger(SubscriberWrapper.class);
	
	/** 监听器 <code>listener</code> */
	private Object listener;
	
	/** 异步标记 <code>aync</code> */
	private boolean aync = false;
	
	/** 优先级 <code>priority</code> */
	private int priority;
	
	/**
	 * 创建委托实例，生存相关代码，提高效率（强烈鄙视guava）
	 * 
	 * @param listener
	 * @param method
	 * @return
	 */
	public static SubscriberWrapper newInstance(Object listener, Method method) {
		
		try {
			
			String listenerClassName = listener.getClass().getName();
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			
			//- padding method
			StringBuilder methodDefinition = new StringBuilder();
			
			methodDefinition.append("public void invocation(Object[] events){\n\t").append(listenerClassName)
				.append(" listener=(").append(listenerClassName).append(")getListener();\n\t\t").append("listener.")
				.append(method.getName()).append("(");
			
			for (int i = 0, j = parameterTypes.length; i < j; i++) {
				if (i != 0) {
					methodDefinition.append(",");
				}
				Class<?> paramType = parameterTypes[i];
				if (paramType.isPrimitive()) {
					if (paramType == int.class) {
						methodDefinition.append("Integer.parseInt(events[").append(i).append("].toString())");
					} else if (paramType == long.class) {
						methodDefinition.append("Long.parseLong(events[").append(i).append("].toString())");
					} else if (paramType == short.class) {
						methodDefinition.append("Short.parseShort(events[").append(i).append("].toString())");
					} else if (paramType == double.class) {
						methodDefinition.append("Double.parseDouble(events[").append(i).append("].toString())");
					} else if (paramType == char.class) {
						methodDefinition.append("((Character)events[").append(i).append("]).charValue()");
					} else if (paramType == byte.class) {
						methodDefinition.append("Byte.parseByte(events[").append(i).append("].toString())");
					} else if (paramType == float.class) {
						methodDefinition.append("Float.parseFloat(events[").append(i).append("].toString())");
					} else if (paramType == boolean.class) {
						methodDefinition.append("Boolean.parseBoolean(events[").append(i).append("].toString())");
					}
				} else {
					methodDefinition.append("(").append(parameterTypes[i].getName()).append(")events[").append(i)
						.append("]");
				}
			}
			methodDefinition.append(");\n\t}");
			
			if (logger.isDebugEnabled()) {
				logger.debug("监听器[{}#{}]对应生存代码\n{}", listener.getClass().toString(), method.toString(),
					methodDefinition.toString());
			}
			
			CtClass ctClass = com.yiji.adk.common.compiler.Compiler.getInstance().newCtClass(SubscriberWrapper.class);
			return Compiler.getInstance().methodWeave(ctClass, SubscriberWrapper.class, methodDefinition.toString())
				.newInstance(ctClass, null, null);
			
		} catch (SecurityException | IllegalArgumentException e) {
			throw new EventException("创建代理对象过程中错误..", e);
		}
	}
	
	public void setAsync(boolean isAsync) {
		
		this.aync = isAsync;
	}
	
	public boolean isAsync() {
		
		return this.aync;
	}
	
	public Object getListener() {
		
		return listener;
	}
	
	public void setListener(Object listener) {
		
		this.listener = listener;
	}
	
	public int getPriority() {
		
		return priority;
	}
	
	public void setPriority(int priority) {
		
		this.priority = priority;
	}
	
	protected abstract void invocation(Object[] events);
	
	@Override
	public int compareTo(SubscriberWrapper o) {
		
		return this.priority == o.priority ? 0 : this.priority > o.priority ? 1 : -1;
	}
}
