/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 20:51 创建
 *
 */
package com.global.adk.executor.axon.transx;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class TransactionWrapper {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <V> V txNew(Callable<V> factoryMethod) throws Exception {

        return factoryMethod.call();
    }
}
