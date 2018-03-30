package com.global.adk.biz.executor.proxy;

import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.biz.executor.InvokeElement;
import com.global.adk.biz.executor.annotation.Invoke;
import org.aopalliance.intercept.MethodInvocation;

public class SerialProxyFilter extends ProxyFilterBase {
	
	private InvokeElement element;
	
	private DBPlugin dbPlugin;
	
	public SerialProxyFilter(int order, InvokeElement element, DBPlugin dbPlugin) {
		
		super(order);
		this.element = element;
		this.dbPlugin = dbPlugin;
	}
	
	@Override
	public Object proceed(MethodInvocation methodInvocation) {
		
		try {
			Invoke.SerialLock serialLock = element.getSerialLock();
			
			boolean isLock = serialLock.isLock();
			boolean isNowaite = serialLock.isNowaite();
			String lockName = serialLock.lock();
			String module = serialLock.module();
			String policy = serialLock.policy();
			if (isLock) {
				if (isNowaite) {
					dbPlugin.lockNoWaite(policy, module, lockName);
				} else {
					dbPlugin.lock(policy, module, lockName);
				}
			}
			
			methodInvocation.proceed();
		} catch (Throwable e) {
			throwException(e);
		}
		return null;
	}
	
}
