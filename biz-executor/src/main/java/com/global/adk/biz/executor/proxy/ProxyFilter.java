package com.yiji.adk.biz.executor.proxy;

import org.aopalliance.intercept.MethodInvocation;

public interface ProxyFilter extends Comparable<ProxyFilter> {
	
	Object proceed(MethodInvocation methodInvocation);
	
	int getOrder();
	
}
