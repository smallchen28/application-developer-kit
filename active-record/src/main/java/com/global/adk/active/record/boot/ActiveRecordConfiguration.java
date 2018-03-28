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

package com.global.adk.active.record.boot;

import com.global.adk.active.record.DomainFactory;
import com.global.adk.active.record.InternalSeqCreator;
import com.global.adk.active.record.StandardInternalSeqCreator;
import com.global.adk.active.record.ValidatorSupport;
import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.common.boot.CommonConfiguration;
import com.global.adk.event.NotifierBus;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 活动记录集yiji-boot配置项
 * <p/>
 * 默认未启用，启用需要添加如下强制依赖。
 * <p/>
 * 不依赖Spring-Boot启动的应用配置示例如下，详见测试用例.
 * 
 * <pre>
 *  &lt;bean&gt; id="domainFactory" class="com.yiji.adk.active.record.DomainFactory"
 *      c:internalSeqCreator="internalSeqCreator"
 *      c:sqlSessionTemplate-ref="sqlSessionTemplate"
 *      c:notifierBus-ref="notifierBus"/&gt;
 *  ……
 * </pre> 依赖yiji-boot配置项，在application.properties文件中配置。
 * <p/>
 * #开启active-record<br/>
 * yiji.adk.activerecord.enable = true
 * <p/>
 * #配置领域事件异步通知线程池<br/>
 * yiji.adk.activerecord.corePoolSize = 3 yiji.adk.activerecord.maxPoolSize = 10 yiji.adk.activerecord.keepAliveSeconds
 * = 300 yiji.adk.activerecord.queueCapacity = 50
 *
 * @author hasulee
 *
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/20 下午下午7:14<br>
 * @see
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ ActiveRecordProperties.class })
@ConditionalOnProperty(value = CommonConfiguration.ADK_ACTIVE_RECORD, matchIfMissing = false)
public class ActiveRecordConfiguration implements CommonConfiguration {
	
	@Autowired(required = false)
	private NotifierBus notifierBus;
	
	@Autowired(required = false)
	private ValidatorSupport validatorSupport;
	
	@Bean
	public DomainFactory domainFactory(SqlSessionTemplate sqlSessionTemplate, InternalSeqCreator internalSeqCreator) {
		DomainFactory domainFactory = new DomainFactory(sqlSessionTemplate, notifierBus, internalSeqCreator,
			validatorSupport);
		return domainFactory;
	}
	
	@Bean
	@ConditionalOnMissingBean(SqlSessionTemplate.class)
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean
	@ConditionalOnMissingBean(InternalSeqCreator.class)
	public InternalSeqCreator internalSeqCreator(DBPlugin dbPlugin) {
		return new StandardInternalSeqCreator(dbPlugin);
	}
	
}
