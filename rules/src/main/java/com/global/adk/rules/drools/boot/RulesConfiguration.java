/*
 * www.global.com Inc.
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

package com.global.adk.rules.drools.boot;

import com.global.adk.common.boot.CommonConfiguration;
import com.global.adk.rules.drools.*;
import com.global.adk.rules.drools.module.DroolsRepository;
import com.global.adk.rules.drools.module.DroolsRepositoryFactoryBean;
import com.global.boot.core.CommonProperties;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import javax.sql.DataSource;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/23 下午下午4:00<br>
 * @see
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ RulesProperties.class, CommonProperties.class })
@ConditionalOnProperty(value = CommonConfiguration.ADK_RULE, matchIfMissing = false)
public class RulesConfiguration {
	
	@Autowired
	private RulesProperties rulesProperties;

	@Autowired
	private CommonProperties commonProperties;
	
	@Bean
	public DroolsProvider droolsProvider(	DroolsRepository droolsRepository,
											DroolsTemplate droolsTemplate,
											StatisticRuleObserver statisticRuleObserver,
											SessionWrapperFactory sessionWrapperFactory,
											MessageZKAccessor messageZKAccessor) {
		DroolsProvider droolsProvider = new DroolsProvider(droolsRepository, droolsTemplate,
			sessionWrapperFactory);
		droolsProvider.setActionDelay(rulesProperties.getActionDelay());
		droolsProvider.setStatisticRuleObserver(statisticRuleObserver);
		droolsProvider.setMessageZKAccessor(messageZKAccessor);
		return droolsProvider;
	}
	
	@Bean
	public SessionWrapperFactory sessionWrapperFactory() {
		return new SessionWrapperFactory(rulesProperties.getPartitionSize());
	}
	
	@Bean
	public DroolsAdmin droolsAdmin(	DroolsTemplate droolsTemplate, DroolsRepository droolsRepository,
									MessageZKAccessor messageZKAccessor) {
		return new DroolsAdmin(droolsRepository, droolsTemplate, messageZKAccessor);
	}
	
	@Bean
	public DroolsRepositoryFactoryBean droolsRepositoryFactoryBean(	JdbcTemplate jdbcTemplate,
																	OracleSequenceMaxValueIncrementer incrementer) {
		DroolsRepositoryFactoryBean droolsRepositoryFactoryBean = new DroolsRepositoryFactoryBean(
			jdbcTemplate, incrementer);
		droolsRepositoryFactoryBean.setTableNamePre(rulesProperties.getTableNamePre());
		return droolsRepositoryFactoryBean;
	}
	
	@Bean
	public DroolsTemplate droolsTemplate() {
		return new DroolsTemplate(rulesProperties.getVelocityProps(),
			rulesProperties.getRuleProps());
	}
	
	@Bean
	public CuratorFrameworkFactoryBean curatorFrameworkFactoryBean() {
		CuratorFrameworkFactoryBean curatorFrameworkFactoryBean = new CuratorFrameworkFactoryBean();
		curatorFrameworkFactoryBean.setZkUrl(commonProperties.getZkUrl());
		return curatorFrameworkFactoryBean;
	}
	
	@Bean
	public MessageZKAccessor messageZKAccessor(CuratorFramework zk) {
		return new MessageZKAccessor(zk);
	}
	
	@Bean
	public OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer(DataSource dataSource) {
		return new OracleSequenceMaxValueIncrementer(dataSource, rulesProperties.getRuleSeqName());
	}
	
	@Bean
	@ConditionalOnMissingBean(StatisticRuleObserver.class)
	public StatisticRuleObserver statisticRuleObserver() {
		return new StatisticRuleObserver() {
			@Override
			public void watch(EvalCaptrueInfo evalCaptrueInfo) {
			}
		};
	}
}
