/*
 * www.global.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

package com.global.adk.rules.drools;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 整合curator,这里就不用外部配置了，意义也不大，直接写死。
 * @author hasulee<ligen@yiji.com>
 * @version 1.0.0
 * @see
 * @since 16/11/1
 */
public class CuratorFrameworkFactoryBean	implements FactoryBean<CuratorFramework>, DisposableBean,
											ApplicationListener<ContextRefreshedEvent> {
											
	private Logger logger = LoggerFactory.getLogger(CuratorFrameworkFactoryBean.class);
	
	/** 与dubbo使用相同的zk */
	private String zkUrl;
	
	private static final String NAMESPACE = "adk-rules";
	
	/** 重试间隔时间(ms) */
	private static final int RETRY_INTERVAL = 1000;
	
	/** 连接超时时间(ms) */
	private static final int CONN_TIME_OUT = 3000;
	
	/** 重试3次 */
	private static final int MAX_RETRY_COUNT = 3;
	
	/** 会话超时时间(ms) */
	private static final int SESSION_TIME_OUT = 1000 * 40;
	
	private CuratorFramework zk;
	
	@Override
	public CuratorFramework getObject() throws Exception {
		this.zk = CuratorFrameworkFactory.builder().namespace(NAMESPACE).connectString(zkUrl)
			.sessionTimeoutMs(SESSION_TIME_OUT).connectionTimeoutMs(CONN_TIME_OUT)
			.retryPolicy(new RetryNTimes(MAX_RETRY_COUNT, RETRY_INTERVAL)).build();
		if (logger.isInfoEnabled()) {
			logger.info("ZK({})构建成功，sessionTimeOut={},connTimeOut={},maxRetry={},retryInterval={}",
				zkUrl, SESSION_TIME_OUT, CONN_TIME_OUT, MAX_RETRY_COUNT, RETRY_INTERVAL);
		}
		return zk;
	}
	
	@Override
	public Class<CuratorFramework> getObjectType() {
		return CuratorFramework.class;
	}
	
	@Override
	public boolean isSingleton() {
		return Boolean.TRUE;
	}
	
	@Override
	//- 监听刷新完成后的事情，避免NotifierBus发布事件时，初始化还没完成，由于依赖关系导致发布事件失败……
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		zk.start();
	}
	
	@Override
	public void destroy() throws Exception {
		if (zk != null) {
			zk.close();
			logger.info("curator(zk)客户端已关闭……");
		}
	}
	
	public void setZkUrl(String zkUrl) {
		this.zkUrl = zkUrl;
	}
}
