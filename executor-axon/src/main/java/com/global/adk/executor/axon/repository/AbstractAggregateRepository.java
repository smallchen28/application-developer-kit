/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-19 20:07 创建
 *
 */
package com.global.adk.executor.axon.repository;

import com.global.adk.executor.axon.factory.AbstractAggregateDBLockFactory;
import com.global.adk.executor.axon.transx.TransactionWrapper;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;
import com.global.common.spring.ApplicationContextHolder;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.LockingRepository;
import org.axonframework.commandhandling.model.inspection.AnnotatedAggregate;
import org.axonframework.common.lock.LockFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.annotation.DefaultParameterResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * 针对聚合根状态变更实现的仓储,非事件溯源类场景继承此
 * <p/>
 * 带锁仓储
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public abstract class AbstractAggregateRepository<T> extends LockingRepository<T, AnnotatedAggregate<T>> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionWrapper transactionWrapper;
    @Autowired
    private EventBus eventBus;

    /**
     * 子类想在其它地方灵活控制锁时，可以使用这个默认构造函数不变
     */
    public AbstractAggregateRepository(Class<T> aggregateType) {
        this(aggregateType, () -> new AbstractAggregateDBLockFactory() {
            @Override
            protected void lockDB(String identifier) {
                //通过lockMapper方法获取相关mapper，执行锁动作
                //lockMapper(XXMapper.class).lock(identifier);
            }
        });
    }


    public AbstractAggregateRepository(Class<T> aggregateType, Supplier<LockFactory> supplierFactory) {
        this(aggregateType, supplierFactory.get());
    }


    public AbstractAggregateRepository(Class<T> aggregateType, LockFactory lockFactory) {
        super(aggregateType, lockFactory, new DefaultParameterResolverFactory());
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~自定义方法~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public T newInstanceX(Supplier<T> supplier) {
        return newInstanceX(supplier.get());
    }

    public T newInstanceX(T root) {
        Assert.notNull(root);

        logger.info("创建并保存聚合对象.root:{}", root);

        doCreateRoot(root);

        return root;
    }

    public T loadX(String aggregateIdentifier, Long expectedVersion) {
        T root = doLoadRoot(aggregateIdentifier, expectedVersion);

        logger.info("加载聚合对象.identifier:{},version:{},root:{}", aggregateIdentifier, expectedVersion, root);

        return root;
    }

    public void saveX(T root) {
        Assert.notNull(root);

        logger.info("更新聚合对象.root:{}", root);

        doSaveRoot(root);
    }

    public void deleteX(T root) {
        Assert.notNull(root);
        logger.info("清除聚合对象.root:{}", root);

        doDeleteRoot(root);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~自定义方法~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 标识当前聚合对象为删除
     */
    public static void markCurrentRootDeleted() {
        AggregateLifecycle.markDeleted();
    }

    @Override
    protected AnnotatedAggregate<T> doCreateNewForLock(Callable<T> factoryMethod) throws Exception {
        T root = factoryMethod.call();
        return transactionWrapper.txNew(() -> {

            newInstanceX(root);

            return AnnotatedAggregate.initialize(root, aggregateModel(), eventBus);
        });

    }

    @Override
    protected void doSaveWithLock(AnnotatedAggregate<T> aggregate) {
        Assert.notNull(aggregate);

        saveX(aggregate.getAggregateRoot());
    }

    @Override
    protected AnnotatedAggregate<T> doLoadWithLock(String aggregateIdentifier, Long expectedVersion) {
        T root = loadX(aggregateIdentifier, expectedVersion);

        if (null == root) {
            throw new AggregateNotFoundException(aggregateIdentifier,
                    format("没找到type:%s,identifier:%s的聚合根",
                            getAggregateType().getSimpleName(), aggregateIdentifier));
        }

        return AnnotatedAggregate.initialize(root, aggregateModel(), eventBus);
    }

    @Override
    protected void doDeleteWithLock(AnnotatedAggregate<T> aggregate) {
        Assert.notNull(aggregate);

        deleteX(aggregate.getAggregateRoot());
    }

    /**
     * 创建聚合对象
     */
    protected abstract T doCreateRoot(T root);

    /**
     * 保存聚合
     */
    protected abstract void doSaveRoot(T root);

    /**
     * 　加载聚合
     */
    protected abstract T doLoadRoot(String aggregateIdentifier, Long expectedVersion);

    /**
     * 　删除聚合
     */
    protected abstract void doDeleteRoot(T root);

    protected static <M> M lockMapper(Class<M> mapperClass) {
        M mapper = ApplicationContextHolder.get().getBean(mapperClass);
        Assert.notNull(mapper);

        return mapper;
    }

}
