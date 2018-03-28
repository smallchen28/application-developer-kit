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
package com.yiji.adk.rules.drools.boot;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import com.yiji.adk.common.boot.CommonConfiguration;

/**
 * 内部规则引擎配置项(基于Drools深度封装)
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/20 下午下午6:59<br>
 * @see
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = CommonConfiguration.ADK_RULE_PREFIX)
public class RulesProperties implements BeanClassLoaderAware, InitializingBean {
	
	/** 是否启用此组件(默认:false) */
	private boolean enable = false;

	/** 异步延迟执行(默认：3000毫秒) */
	private long actionDelay;

	/** 对应每个事件的KnowledgeBase对象池分区大小 */
	private int partitionSize = 25;

	/** KownledgeBaseConfiguration（drools配置） */
	private ClassPathResource ruleProps;

	/** Velocity配置 */
	private ClassPathResource velocityProps;

	/** 规则需要序列号配置(默认：seq_app_kit_rule) */
	private String ruleSeqName="seq_app_kit_rule";

	/** 规则表前缀(默认：app_kit_) */
	private String tableNamePre="app_kit_";

	private ClassLoader beanClassLoader;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public long getActionDelay() {
		return actionDelay;
	}

	public void setActionDelay(long actionDelay) {
		this.actionDelay = actionDelay;
	}

	public ClassPathResource getRuleProps() {
		return ruleProps;
	}

	public void setRuleProps(ClassPathResource ruleProps) {
		this.ruleProps = ruleProps;
	}

	public ClassPathResource getVelocityProps() {
		return velocityProps;
	}

	public void setVelocityProps(ClassPathResource velocityProps) {
		this.velocityProps = velocityProps;
	}

	public String getRuleSeqName() {
		return ruleSeqName;
	}

	public void setRuleSeqName(String ruleSeqName) {
		this.ruleSeqName = ruleSeqName;
	}

	public String getTableNamePre() {
		return tableNamePre;
	}

	public void setTableNamePre(String tableNamePre) {
		this.tableNamePre = tableNamePre;
	}

	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}

	public int getPartitionSize() {
		return partitionSize;
	}

	public void setPartitionSize(int partitionSize) {
		this.partitionSize = partitionSize;
	}
}