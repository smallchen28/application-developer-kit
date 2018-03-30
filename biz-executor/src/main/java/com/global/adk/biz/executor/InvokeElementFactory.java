/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor;

import com.global.adk.biz.executor.annotation.Invoke;
import com.global.adk.common.compiler.*;
import com.global.adk.common.compiler.Compiler;
import com.global.adk.common.log.TraceLogFactory;
import com.yjf.common.lang.result.StandardResultInfo;
import javassist.CtClass;
import org.slf4j.Logger;

/**
 * 执行元素工厂
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月18日 上午1:25:34<br>
 */
public class InvokeElementFactory {
	
	private InvokeElementFactory() {
		
	}
	
	private static class InvokElementFactoryHolder {
		
		private static final InvokeElementFactory INSTANCE = new InvokeElementFactory();
	}
	
	public static InvokeElementFactory getInstance() {
		
		return InvokElementFactoryHolder.INSTANCE;
	}
	
	@SuppressWarnings("rawtypes")
	public InvokeElement newInstance(String serviceName, Class[] validateGroups, String logName,
										InvokeService invokeService, Class entityClass, String resultClass,
										boolean isAsync, Invoke.SerialLock serialLock,
										Invoke.TransactionAttribute transactionAttribute,
										boolean isEntityInjectSpringBeans) {
		
		Logger logger = TraceLogFactory.getLogger(logName);
		
		Compiler compiler = com.yiji.adk.common.compiler.Compiler.getInstance();
		
		CtClass ctClass = compiler.newCtClass(InvokeElement.class);
		
		String constructScript = generateConstructor(ctClass.getSimpleName());
		String newEntityScript = generateNewEntityObject(entityClass, serviceName);
		String newResultScript = generateNewResult(resultClass, serviceName);
		
		if (logger.isInfoEnabled()) {
			logger.info("{}->script \nconstruct :\n{}\nnewResult:\n{}\nnewEntity:\n{}", invokeService.getClass()
				.getName(), constructScript, newResultScript, newEntityScript);
		}
		
		compiler.constructImplement(ctClass, constructScript);
		
		Class<?>[] parameterTypes = new Class[] { String.class, Class[].class, String.class, InvokeService.class,
													Class.class, String.class, boolean.class, Invoke.SerialLock.class,
													Invoke.TransactionAttribute.class, boolean.class };
		Object[] parameters = new Object[] { serviceName, validateGroups, logName, invokeService, entityClass,
											resultClass, isAsync, serialLock, transactionAttribute,
											isEntityInjectSpringBeans };
		return compiler.methodWeave(ctClass, InvokeElement.class, newEntityScript)
			.methodWeave(ctClass, InvokeElement.class, newResultScript)
			.newInstance(ctClass, parameterTypes, parameters);
	}
	
	private String generateConstructor(String constructName) {
		
		StringBuilder constructor = new StringBuilder();
		constructor
			.append("public ")
			.append(constructName)
			.append(
				"(String serviceName,Class[] validateGroups, String logName, com.yiji.adk.biz.executor.InvokeService invokeService, Class entityClass, String resultClass, boolean isAsync,  com.yiji.adk.biz.executor.annotation.Invoke.SerialLock serialLock,com.yiji.adk.biz.executor.annotation.Invoke.TransactionAttribute transactionAttribute,boolean isEntityInjectSpringBeans) {\n\t")
			.append(
				"super(serviceName,validateGroups,logName,invokeService,entityClass,resultClass,isAsync,serialLock,transactionAttribute,isEntityInjectSpringBeans);\n\t")
			.append("}");
		return constructor.toString();
	}
	
	private String generateNewEntityObject(Class entityClass, String serviceName) {
		
		StringBuilder src = new StringBuilder();
		src.append("public  com.yiji.adk.active.record.module.EntityObject newEntityObject(){\n\t").append("return ");
		if (entityClass == null || entityClass.equals("")) {
			src.append("null;\n}");
		} else {
			src.append("new ").append(entityClass.getName()).append("();\n").append("}");
		}
		return src.toString();
	}
	
	private String generateNewResult(String resultClass, String serviceName) {

		StringBuilder src = new StringBuilder();
		src.append("public com.yjf.common.lang.result.StandardResultInfo newResult(){\n\t").append("return ");
		if (resultClass == null || resultClass.equals("")) {
			src.append("null;\n}");
		} else {
			src.append("new ").append(resultClass).append("();\n").append("}");
		}
		return src.toString();
	}
}
