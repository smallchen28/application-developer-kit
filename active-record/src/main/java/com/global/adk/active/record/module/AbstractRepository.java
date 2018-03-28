/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-05-12 15:09 创建
 *
 */
package com.global.adk.active.record.module;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 仓储基础抽象
 * 
 * @author chenlin@yiji.com
 */
public abstract class AbstractRepository<I, T extends AggregateRoot> {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 加载聚合根
	 * @param identity 标识
	 * @return 聚合根
	 */
	public T load(I identity) {
		Assert.notNull(identity);
		
		return doLoad(identity);
	}
	
	/**
	 * 持久化聚合根
	 * @param root 聚合根
	 */
	public void store(T root) {
		store(root, false);
	}
	
	/**
	 * 持久化聚合根
	 * @param root 聚合根
	 * @param update 是否更新.true-更新，false-存储
	 */
	public void store(T root, boolean update) {
		Assert.notNull(root);
		
		if (update) {
			doRestore(root);
		} else {
			doStore(root);
		}
	}
	
	protected abstract T doLoad(I identity);
	
	protected abstract void doStore(T root);
	
	protected abstract void doRestore(T root);
	
}
