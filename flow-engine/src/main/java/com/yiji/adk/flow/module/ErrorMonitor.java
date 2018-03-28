/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-5-9 下午11:53<br>
 * @see
 * @since 1.0.0
 */
public class ErrorMonitor {
	
	@NotNull
	private String errorMonitorClass;
	
	@Valid
	private ExceptionMapping exceptionMapping = new ExceptionMapping();
	
	public String getErrorMonitorClass() {
		return errorMonitorClass;
	}
	
	public void setErrorMonitorClass(String errorMonitorClass) {
		this.errorMonitorClass = errorMonitorClass;
	}
	
	public ExceptionMapping getExceptionMapping() {
		return exceptionMapping;
	}
	
	public void setExceptionMapping(ExceptionMapping exceptionMapping) {
		this.exceptionMapping = exceptionMapping;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.module.ErrorMonitor{");
		sb.append("errorMonitorClass='").append(errorMonitorClass).append('\'');
		sb.append(", exceptionMapping=").append(exceptionMapping);
		sb.append('}');
		return sb.toString();
	}
}
