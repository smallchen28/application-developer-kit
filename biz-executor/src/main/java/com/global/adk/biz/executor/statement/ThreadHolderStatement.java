package com.global.adk.biz.executor.statement;

import com.global.adk.biz.executor.ServiceContextHolder;
import com.global.adk.biz.executor.event.ServiceApplyEvent;
import com.global.adk.biz.executor.event.ServiceFinishEvent;
import com.global.adk.event.Subscribe;

/**
 * 线程持有上下文
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 16/8/29
 */
public class ThreadHolderStatement {

    @Subscribe(priority = Integer.MAX_VALUE)
    public void addHolder(ServiceApplyEvent event){
        ServiceContextHolder.set(event.value());
    }

    @Subscribe(priority = Integer.MAX_VALUE)
    public void removeHolder(ServiceFinishEvent event){
        ServiceContextHolder.clear();
    }

}
