/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 20:52 创建
 *
 */
package com.global.adk.executor.axon;

import com.global.adk.common.boot.CommonConfiguration;
import com.global.adk.executor.axon.eventcommand.BusHolder;
import com.global.adk.executor.axon.transx.TransactionWrapper;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
@Configuration
@EnableConfigurationProperties({ExecutorAxonProperties.class})
@ConditionalOnProperty(value = CommonConfiguration.ADK_BIZ_EXECUTOR_AXON, matchIfMissing = false)
public class ExecutorAxonAutoConguration implements InitializingBean {

    @Autowired
    private CommandBus commandBus;
    @Autowired
    private EventBus eventBus;

    @Bean
    public TransactionWrapper transactionWrapper() {
        return new TransactionWrapper();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        BusHolder.commandBus = commandBus;
        BusHolder.eventBus = eventBus;
    }
}
