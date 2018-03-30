/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.delegate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import javassist.CtClass;

import com.global.adk.common.compiler.Compiler;
import com.global.adk.common.exception.FlowException;
import com.global.adk.flow.annotation.*;
import com.global.adk.flow.annotation.Error;
import com.global.adk.flow.engine.Execution;

/**
 * 动态编码支持
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-5-10 上午1:46<br>
 * @see
 * @since 1.0.0
 */
public enum InvokeCoder {
	
	executor(Executor.class) {
		@Override
		protected void validateTypes(InvokeDelegateContext.Key key, Method method, Class<?>[] parameterTypes,
										Class<?> returnType) {
			//Object fun(Execution execution)
			if (parameterTypes.length != 1 || (parameterTypes[0] != Execution.class)) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Executor处理器入参定义出错，例如：func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
			
			//returnType = {void.class,Void.class,String.class}
			if (returnType != Void.class && returnType != void.class && returnType != String.class) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Executor处理器返回参数定义出错，例如：Object、Void、void、String func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
		}
	},
	
	before(Before.class) {
		@Override
		protected void validateTypes(InvokeDelegateContext.Key key, Method method, Class<?>[] parameterTypes,
										Class<?> returnType) {
			//void fun(Execution execution)
			if (parameterTypes.length != 1 || (parameterTypes[0] != Execution.class)) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Before处理器入参定义出错，例如：func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
			
			//returnType = {void.class,Void.class}
			if (returnType != Void.class && returnType != void.class) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Before处理器返回参数定义出错，例如：Void、void func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
		}
	},
	
	after(After.class) {
		@Override
		protected void validateTypes(InvokeDelegateContext.Key key, Method method, Class<?>[] parameterTypes,
										Class<?> returnType) {
			//void fun(Execution execution)
			if (parameterTypes.length != 1 || (parameterTypes[0] != Execution.class)) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，After处理器入参定义出错，例如：func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
			
			//returnType = {void.class,Void.class}
			if (returnType != Void.class && returnType != void.class) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,,version=%s,nodeName=%s，After处理器返回参数定义出错，例如：Void、void func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
		}
	},
	
	end(End.class) {
		@Override
		protected void validateTypes(InvokeDelegateContext.Key key, Method method, Class<?>[] parameterTypes,
										Class<?> returnType) {
			//void fun(Execution execution)
			if (parameterTypes.length != 1 || (parameterTypes[0] != Execution.class)) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,,version=%s,nodeName=%s，End处理器入参定义出错，例如：func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
			
			//returnType = {void.class,Void.class}
			if (returnType != Void.class && returnType != void.class) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，End处理器返回参数定义出错，例如：Void、void func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
		}
	},
	
	condition(Condition.class) {
		@Override
		protected void validateTypes(InvokeDelegateContext.Key key, Method method, Class<?>[] parameterTypes,
										Class<?> returnType) {
			//String fun(Execution execution)
			if (parameterTypes.length != 1 || (parameterTypes[0] != Execution.class)) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Condition处理器入参定义出错，例如：func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
			
			//returnType = String
			if (returnType != String.class) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Condition处理器返回参数定义出错，例如：String func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
			
		}
	},
	
	error(Error.class) {
		@Override
		protected void validateTypes(InvokeDelegateContext.Key key, Method method, Class<?>[] parameterTypes,
										Class<?> returnType) {
			// fun(Execution execution)
			if (parameterTypes.length != 1 || (parameterTypes[0] != Execution.class)) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Error处理器入参定义出错，例如：String func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
			
			if (returnType != String.class) {
				throw new FlowException(
					String
						.format(
							"流程定义Flow=%s,version=%s,nodeName=%s，Error处理器返回参数定义出错，例如：String func(Execution execution) ,出错方法method=%s,parameterTypes=%s",
							key.getFlowName(), key.getVersion(), key.getNodeName(), method,
							Arrays.toString(parameterTypes)));
			}
		}
	};
	
	//创建InvokeDelegate
	public InvokeDelegate create(InvokeDelegateContext.Key key, Object targetInstance, Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?> returnTypes = method.getReturnType();
		
		validateTypes(key, method, parameterTypes, returnTypes);

		return createInvokeDelegate(method, targetInstance, parameterTypes);
	}
	
	//动态生存InvokeDelegate
	InvokeDelegate createInvokeDelegate(Method method, Object targetInstance, Class<?>[] parameterTypes) {
		
		com.yiji.adk.common.compiler.Compiler compiler = Compiler.getInstance();
		CtClass ctClass = compiler.newCtClass(AbstractInvokeDelegate.class);
		
		StringBuilder constructScript = new StringBuilder();
		constructScript.append("public ").append(ctClass.getSimpleName()).append("(Object target){\n\t")
			.append("super(target);\n").append("}\n");
		
		StringBuilder executeScript = new StringBuilder();
		executeScript.append("public Object invoke(Object[] args){\n\t");
		Class<?> returnType = method.getReturnType();
		if (returnType == Void.class || returnType == void.class) {
			executeScript.append("((").append(targetInstance.getClass().getName()).append(")getTarget()).")
				.append(method.getName()).append("((").append(parameterTypes[0].getName()).append(")")
				.append("args[0]);\n").append("return null;").append("}\n");
		} else {
			executeScript.append("return ((").append(targetInstance.getClass().getName()).append(")getTarget()).")
				.append(method.getName()).append("((").append(parameterTypes[0].getName()).append(")")
				.append("args[0]);\n").append("}\n");
		}
		
		compiler.constructImplement(ctClass, constructScript.toString());
		
		Object[] parameters = new Object[] { targetInstance };
		return compiler.methodWeave(ctClass, AbstractInvokeDelegate.class, executeScript.toString()).newInstance(
			ctClass, new Class[] { Object.class }, parameters);
	}
	
	private Class<? extends Annotation> annotationType;
	
	private InvokeCoder(Class<? extends Annotation> annotationType) {
		this.annotationType = annotationType;
	}
	
	public static InvokeCoder coder(Annotation annotation) {
		InvokeCoder coder = null;
		
		for (InvokeCoder dc : values()) {
			if (dc.annotationType == annotation.annotationType()) {
				coder = dc;
				break;
			}
		}
		return coder;
	}
	
	protected abstract void validateTypes(InvokeDelegateContext.Key key, Method method, Class<?>[] parameterTypes,
											Class<?> returnTypes);
}