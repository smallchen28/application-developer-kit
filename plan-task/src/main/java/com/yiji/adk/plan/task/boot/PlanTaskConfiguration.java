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

package com.yiji.adk.plan.task.boot;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import com.yiji.adk.common.boot.CommonConfiguration;
import com.yiji.adk.plan.task.PlanTaskAdminFactoryBean;
import com.yjf.common.concurrent.MonitoredThreadPool;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/23 下午下午4:00<br>
 * @see
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ PlanTaskProperties.class })
@ConditionalOnProperty(value = CommonConfiguration.ADK_PLAN_TASK, matchIfMissing = false)
public class PlanTaskConfiguration {

    @Autowired
    private PlanTaskProperties planTaskProperties;

    @Bean
    public PlanTaskAdminFactoryBean planTaskAdminFactoryBean(DataSource dataSource , @Qualifier("plan_task_incrementer")OracleSequenceMaxValueIncrementer incrementer , MonitoredThreadPool threadPool){
        PlanTaskAdminFactoryBean planTaskAdminFactoryBean = new PlanTaskAdminFactoryBean(dataSource,incrementer,threadPool);
        planTaskAdminFactoryBean.setDataSource(dataSource);
        planTaskAdminFactoryBean.setLocation(planTaskProperties.getLocation());
        planTaskAdminFactoryBean.setTableNamePre(planTaskProperties.getTableNamePre());
        return planTaskAdminFactoryBean;
    }

    @Bean(name = "plan_task_incrementer")
    public OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer(DataSource dataSource){
        return new OracleSequenceMaxValueIncrementer(dataSource,planTaskProperties.getIncrementName());
    }


    @Bean
    @ConditionalOnMissingBean(MonitoredThreadPool.class)
    public MonitoredThreadPool threadPool(){
        MonitoredThreadPool threadPool = new MonitoredThreadPool();

        threadPool.setCorePoolSize(planTaskProperties.getCorePoolSize());
        threadPool.setKeepAliveSeconds(planTaskProperties.getKeepAliveSeconds());
        threadPool.setMaxPoolSize(planTaskProperties.getMaxPoolSize());
        threadPool.setQueueCapacity(planTaskProperties.getQueueCapacity());

        threadPool.setThreadNamePrefix("ADK-PLAN");
        threadPool.setEnableTimerMetric(true);
        threadPool.setEnableGaugeMetric(true);

        return threadPool;
    }
}
