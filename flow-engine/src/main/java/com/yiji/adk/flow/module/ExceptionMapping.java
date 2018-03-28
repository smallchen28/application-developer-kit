/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import com.google.common.collect.Lists;
import com.yiji.adk.common.exception.FlowException;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-17 下午1:00<br>
 * @see
 * @since 1.0.0
 */
public class ExceptionMapping {
	
	@NotEmpty
	public List<Class<? extends Throwable>> throwables = Lists.newArrayList();
	
	public List<Class<? extends Throwable>> getThrowables() {
		return throwables;
	}
	
	public void addThrowable(String throwable) {
		Class<? extends Throwable> throwableClass = null;
		try {
			throwableClass = (Class<? extends Throwable>) Class.forName(throwable);
		} catch (ClassNotFoundException e) {
			throw new FlowException(String.format("解析ExceptionMonitor过程出现错误，throwable=%s初始化失败!",throwable),e);
		}
		throwables.add(throwableClass);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.module.ExceptionMapping{");
		sb.append("throwables=").append(throwables);
		sb.append('}');
		return sb.toString();
	}
}
