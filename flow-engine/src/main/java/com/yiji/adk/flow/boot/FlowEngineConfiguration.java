/*
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

package com.yiji.adk.flow.boot;

import com.alibaba.dubbo.config.*;
import com.yiji.adk.common.boot.CommonConfiguration;
import com.yiji.adk.flow.FlowContext;
import com.yiji.adk.flow.StandardFlowContext;
import com.yiji.adk.flow.state.FlowHistoryTraceRepository;
import com.yiji.adk.flow.state.FlowTraceRepository;
import com.yiji.adk.flow.state.retry.RetryFlowProvider;
import com.yiji.adk.flow.state.retry.RetryTransitionListener;
import com.yiji.adk.flow.state.retry.TxWrapper;
import com.yjf.common.spring.ApplicationContextHolder;
import com.yjf.scheduler.service.api.ScheduleCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 流程引擎配置项目
 *
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/20 下午下午7:14<br>
 * @see
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({FlowEngineProperties.class})
@ConditionalOnProperty(value = CommonConfiguration.ADK_FLOW_ENGINE, matchIfMissing = false)
public class FlowEngineConfiguration implements CommonConfiguration, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    public FlowEngineProperties flowEngineProperties;

    private volatile boolean exported = false;

    @Bean
    public FlowContext flowContext() {
        return new StandardFlowContext(flowEngineProperties.getLocation(), flowEngineProperties.isEnableRetry());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonConfiguration.ADK_FLOW_ENGINE_PREFIX, name = "enableRetry",
            matchIfMissing = false)
    public RetryFlowProvider retryFlowProvider(FlowTraceRepository flowTraceRepository, FlowContext flowContext) {
        return new RetryFlowProvider(flowTraceRepository, flowContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryTransitionListener retryTransitionListener() {
        return new RetryTransitionListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowTraceRepository flowTraceRepository(FlowEngineProperties properties, JdbcTemplate jdbcTemplate) {
        return new FlowTraceRepository(properties.getDialect(), jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowHistoryTraceRepository flowHistoryTraceRepository(FlowEngineProperties properties,
                                                                 JdbcTemplate jdbcTemplate) {
        return new FlowHistoryTraceRepository(properties.getDialect(), jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public TxWrapper txHolder() {
        return new TxWrapper();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (exported) {
            return;
        }

        if (flowEngineProperties.isEnableRetryProvider()) {
            RetryFlowProvider provider = ApplicationContextHolder.get().getBean(RetryFlowProvider.class);

            ApplicationConfig applicationConfig = ApplicationContextHolder.get().getBean(ApplicationConfig.class);
            RegistryConfig registryConfig = ApplicationContextHolder.get().getBean("registryConfig",
                    RegistryConfig.class);
            ProtocolConfig protocolConfig = ApplicationContextHolder.get().getBean(ProtocolConfig.class);
            ProviderConfig providerConfig = ApplicationContextHolder.get().getBean(ProviderConfig.class);

            ServiceConfig<ScheduleCallBackService> serviceConfig = new ServiceConfig<>();
            serviceConfig.setInterface(ScheduleCallBackService.class);
            serviceConfig.setApplication(applicationConfig);
            serviceConfig.setRegistry(registryConfig);
            serviceConfig.setProtocol(protocolConfig);
            serviceConfig.setProvider(providerConfig);

            serviceConfig.setRef(provider);
            serviceConfig.setGroup(flowEngineProperties.getDubboGroup());
            serviceConfig.setVersion(flowEngineProperties.getDubboVersion());

            serviceConfig.export();

            exported = true;
        }
    }
}
