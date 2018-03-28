/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 18:57 创建
 *
 */
package com.yiji.adk.executor.axon.factory;

import org.axonframework.common.lock.Lock;
import org.axonframework.common.lock.LockFactory;

/**
 * 默认聚合锁工厂
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public abstract class AbstractAggregateDBLockFactory implements LockFactory {

    @Override
    public Lock obtainLock(String identifier) {

        lockDB(identifier);

        return new DefaultAggregateLock();
    }


    protected abstract void lockDB(String identifier);


    public static class DefaultAggregateLock implements Lock {

        @Override
        public void release() {
            // 依赖事务提交释放
        }

        @Override
        public boolean isHeld() {
            // 默认true
            return true;
        }
    }

}
