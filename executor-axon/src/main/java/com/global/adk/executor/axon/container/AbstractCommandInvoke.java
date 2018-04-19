/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 20:57 创建
 *
 */
package com.global.adk.executor.axon.container;

import com.global.adk.biz.executor.InvokeServiceAdapter;
import com.global.adk.biz.executor.ServiceContext;
import com.global.adk.common.exception.DefaultSysException;
import com.global.adk.common.exception.KitNestException;
import com.global.common.lang.enums.CommonErrorCode;
import com.global.common.lang.result.StandardResultInfo;
import com.global.common.lang.result.Status;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.model.LockAwareAggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.commandhandling.model.inspection.AnnotatedAggregate;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.springframework.dao.DuplicateKeyException;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * 带幂等处理invoke,查询类直接继承InvokeServiceAdapter
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public abstract class AbstractCommandInvoke<PARAM, RESULT extends StandardResultInfo> extends InvokeServiceAdapter<PARAM, RESULT> {

    private static final String REPEAT = "true";

    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void before(ServiceContext<PARAM, RESULT> serviceContext) {
        doBefore(serviceContext);
    }

    @Override
    public void invoke(ServiceContext<PARAM, RESULT> serviceContext) {
        //固定动作，开启UnitOfWork
        DefaultUnitOfWork.startAndGet(GenericCommandMessage.asCommandMessage(serviceContext.getParameter()));

        try {
            doIdempotence(serviceContext);

            if (goon(serviceContext)) {
                doInvoke(serviceContext);

                //固定动作，保证repository的doSave和doDelete会在UnitOfWork的回调
                CurrentUnitOfWork.get().commit();
                return;
            }

            CurrentUnitOfWork.get().rollback();
        } catch (Throwable e) {
            //固定动作，释放资源对象
            CurrentUnitOfWork.get().rollback(e);

            throw e;
        }
    }

    @Override
    public void after(ServiceContext<PARAM, RESULT> serviceContext) {
        if (goon(serviceContext)) {
            doAfter(serviceContext);
        }
    }

    @Override
    public void end(ServiceContext<PARAM, RESULT> serviceContext) {
        doEnd(serviceContext);
    }

    private void skip(ServiceContext<PARAM, RESULT> serviceContext) {
        serviceContext.putAttribute(REPEAT, REPEAT);
    }

    private boolean goon(ServiceContext<PARAM, RESULT> serviceContext) {
        return null == serviceContext.getAttribute(REPEAT);
    }

    /**
     * 带幂等处理方法.配合{@link #doIdempotence(ServiceContext)}使用
     *
     * @param repository             　当前仓储
     * @param createAggregateFactory 　生成聚合对象工厂
     * @param serviceContext         应用服务上下文
     * @param idempotenceProcessor   　幂等处理器
     * @param <T>                    　聚合对象类型
     * @return　 聚合对象
     */
    protected final <T> T createNewRootWithIdempotence(Repository<T> repository, Callable<T> createAggregateFactory, ServiceContext<PARAM, RESULT> serviceContext, Function<ServiceContext, Boolean> idempotenceProcessor) {
        try {
            LockAwareAggregate<T, AnnotatedAggregate<T>> lockAwareAggregate = (LockAwareAggregate<T, AnnotatedAggregate<T>>) repository.newInstance(createAggregateFactory);

            serviceContext.setAggregate(lockAwareAggregate.getWrappedAggregate().getAggregateRoot());
            return lockAwareAggregate.getWrappedAggregate().getAggregateRoot();
        } catch (DuplicateKeyException de) {
            logger.warn("创建root重复.order:{},msg:{},causeMsg:{}", serviceContext.getParameter(), de.getMessage(), null != de.getCause() ? de.getCause().getMessage() : "null");

            serviceContext.result().setStatus(Status.SUCCESS);
            serviceContext.result().setCode(CommonErrorCode.REQUEST_REPEATED.code());
            serviceContext.result().setDescription(CommonErrorCode.REQUEST_REPEATED.message());
            if (idempotenceProcessor.apply(serviceContext)) {
                skip(serviceContext);
            }
        } catch (Exception e) {
            if (e instanceof KitNestException) {
                throw (KitNestException) e;
            }

            throw new DefaultSysException(String.format("创建root未知异常.order:%s", serviceContext.getParameter()), CommonErrorCode.SYSTEM_ERROR.code(), e);
        }
        return null;
    }

    /**
     * 做些前置处理，不包括创建插入存储操作，无事务
     */
    protected void doBefore(ServiceContext<PARAM, RESULT> serviceContext) {

    }

    /**
     * 做幂等处理,实现可直接调用 {@link #createNewRootWithIdempotence(Repository, Callable, ServiceContext, Function)}实现或者自行实现
     * <p/>
     * 处于invoke事务中
     */
    protected abstract void doIdempotence(ServiceContext<PARAM, RESULT> serviceContext);


    /**
     * 应用处理，处于invoke事务中
     */
    protected abstract void doInvoke(ServiceContext<PARAM, RESULT> serviceContext);

    /**
     * 后置处理，做些转换之类的，无事务
     */
    protected void doAfter(ServiceContext<PARAM, RESULT> serviceContext) {

    }

    /**
     * 结尾处理，无论是否异常都会执行,无事务
     */
    protected void doEnd(ServiceContext<PARAM, RESULT> serviceContext) {

    }

}
