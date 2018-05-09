/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.statement;

import com.global.adk.biz.executor.event.InitEvent;
import com.global.adk.biz.executor.event.ServiceApplyEvent;
import com.global.adk.common.Constants;
import com.global.adk.common.exception.BizException;
import com.global.adk.common.exception.IllegalParameterException;
import com.global.adk.common.exception.NestError;
import com.global.adk.common.exception.SystemException;
import com.global.adk.event.Subscribe;
import com.yjf.common.service.Validatable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证处理
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月17日 下午6:48:06<br>
 * @see
 * @since 1.0.0
 */
public class ValidateStatement {
	
	private Map<Type, Class<?>> voidParameters = new ConcurrentHashMap<>();
	
	@Subscribe(priority = 4, isAsync = false)
	public void checkRequestCode(ServiceApplyEvent event) {
		String serviceName = event.value().getInvokeElement().getServiceName();
		Object parameter = event.value().getParameter();
		if (parameter != null) {
			event.value().getRegistryCodeVerify().validate(serviceName, parameter);
		}
	}
	
	@Subscribe(priority = 5, isAsync = false)
	public void checkParameter(ServiceApplyEvent event) {
		try {
			Object parameter = event.value().getParameter();
			
			if (parameter != null) {
				//OrderBase 实现了 Validatable 的 checkWithGroup 方法，
				if (parameter instanceof Validatable) {
					Class[] validateGroups = event.value().getInvokeElement().getValidateGroups();
					((Validatable) parameter).checkWithGroup(validateGroups);
				}
			} else {
				Class<?> invokeServiceClass = event.value().getInvokeElement().getInvokeService().getClass();
				
				Class<?> parameterType = voidParameters.get(invokeServiceClass);
				
				if (parameterType == null) {
					Class<?> supperClass = invokeServiceClass;
					
					do {
						Type parameterizedType = supperClass.getGenericSuperclass();
						if (parameterizedType instanceof ParameterizedType) {
							Type[] genericTypes = ((ParameterizedType) parameterizedType).getActualTypeArguments();
							
							if (genericTypes.length > 2) {
								throw new NestError(String.format("%s范型配置参数超限（%s>2）", event.value().getInvokeElement()
									.getInvokeService().getClass().getName(), genericTypes.length));
							}
							
							if (genericTypes.length < 2) {
								throw new NestError(String.format("%s范型配置出错缺失parameter或result", event.value()
									.getInvokeElement().getInvokeService().getClass().getName()));
							}
							
							parameterType = (Class<?>) genericTypes[0];
							
							if (parameterType != Void.class) {
								throw new IllegalParameterException(String.format(
									"InvokeService(serviceName=%s)请求Order不可为空!", event.value().getInvokeElement()
										.getServiceName()));
							}
							
							voidParameters.put(invokeServiceClass, parameterType);
							
						}
						supperClass = supperClass.getSuperclass();
					} while (supperClass != Object.class && parameterType == null);
					
					if (supperClass == Object.class) {
						throw new NestError(String.format("%s错误的InvokService范型配置", event.value().getInvokeElement()
							.getInvokeService().getClass().getName()));
					}
					
				}
			}
		} catch (Exception e) {
			if (e instanceof SystemException) {
				throw (SystemException) e;
			}
			/*BizException error = new IllegalParameterException(String.format("%s单据检查出错->%s", event.value()
				.getInvokeElement().getInvokeService().getClass().getSimpleName(), e.getMessage()));*/
			// karott  这里精简一下message，不然result.toString方法会限制长度，将多余内容变成"..."符号，另外不把内部细节暴露出去
			BizException error = new IllegalParameterException(String.format("单据错误:%s", e.getMessage()));
			error.setErrorCode(Constants.ERROR_CODE_ILLEGA_PARAMETER);
			throw error;// new IllegalParameterException(String.format("非法的请求参数%s,错误->%s", event.value().getParameter(),e.getMessage()));
		}
	}
	
	@Subscribe(isAsync = false)
	public void initializer(InitEvent event) {
		
	}
}
