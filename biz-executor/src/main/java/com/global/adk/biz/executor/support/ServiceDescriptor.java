/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-04-19 15:13 创建
 *
 */
package com.global.adk.biz.executor.support;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class ServiceDescriptor {
	
	private Class<?> implemention;
	private Class<?> service;
	private String group;
	private String version;
	
	private String type;
	
	public ServiceDescriptor(Class<?> implemention, Class<?> service, String group, String version, String type) {
		this.implemention = implemention;
		this.service = service;
		this.group = group;
		this.version = version;
		this.type = type;
	}
	
	public Class<?> getImplemention() {
		return implemention;
	}
	
	public Class<?> getService() {
		return service;
	}
	
	@Override
	public String toString() {
		return "ServiceDescriptor{"+ "service='" + service + '\'' + ", group='" + group + '\'' + ", version='"
				+ version + '\'' + ", type='" + type + '\'' + '}';
	}
}
