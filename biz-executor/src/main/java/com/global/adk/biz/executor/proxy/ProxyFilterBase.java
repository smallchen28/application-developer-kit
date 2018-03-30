package com.global.adk.biz.executor.proxy;

import com.global.adk.common.exception.KitNestException;
import com.global.adk.common.exception.NestError;

/**
 * 基础实现，增强排序。
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月19日 下午1:22:17<br>
 */
public abstract class ProxyFilterBase implements ProxyFilter {
	
	private int order;
	
	public ProxyFilterBase(int order) {
		this.order = order;
	}
	
	protected void throwException(Throwable e) {
		if (e instanceof KitNestException) {
			throw (KitNestException) e;
		}
		throw new NestError("系统内部执行出错", e);
	}
	
	@Override
	public int getOrder() {
		return 0;
	}
	
	@Override
	public int compareTo(ProxyFilter o) {
		
		return order > o.getOrder() ? -1 : order == o.getOrder() ? 0 : 1;
	}
	
}
