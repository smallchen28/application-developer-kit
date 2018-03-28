/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

package com.yiji.adk.rules.drools;

import com.yiji.adk.common.exception.NestError;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.Charset;

/**
 * 基于curator的zk访问者实现，用于操作对 {@link }
 * @author hasulee<ligen@yiji.com>
 * @version 1.0.0
 * @see
 * @since 16/10/31
 */
public class MessageZKAccessor {
	
	public static final String REFRESH_DATA = "refresh";
	
	public static final String PATH = "/watcher";
	
	private NodeCache currentNodeCache;
	
	private CuratorFramework zk;
	
	public MessageZKAccessor(CuratorFramework curatorFramework) {
		this.zk = curatorFramework;
	}
	
	public synchronized void createNodeIfNecessary(final NodeCacheListener listener) {
		
		try {
			//- 事务处理
			transaction(transaction -> {
				
				CuratorTransactionFinal curatorTransactionFinal = null;
				
				if (!checkExists(PATH)) {
					curatorTransactionFinal = transaction.create().withMode(CreateMode.PERSISTENT)
						.forPath(PATH).and();
				}
				// 监听节点内容变化
				NodeCache nodeCache = new NodeCache(zk, PATH);
				if (listener != null) {
					nodeCache.getListenable().addListener(listener);
				}
				
				try {
					if (currentNodeCache != null) {
						currentNodeCache.close();
					}
				} catch (Exception e) {
					//ignore……
				}
				
				currentNodeCache = nodeCache;
				currentNodeCache.start(true);
				
				return curatorTransactionFinal;
			});
		} catch (Exception e) {
			throw new NestError(String.format("创建或监听ZK({})节点时出错，path=({})……",
				zk.getZookeeperClient().getCurrentConnectionString(), PATH), e);
		}
	}
	
	private boolean checkExists(String path) throws Exception {
		return zk.checkExists().forPath(path) != null;
	}
	
	private void transaction(TransactionCallback callback) throws Exception {
		if (zk.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
			
			CuratorTransactionFinal transaction = callback.execute(zk.inTransaction());
			
			if (transaction != null) {
				transaction.commit();
			}
		}
	}
	
	interface TransactionCallback {
		CuratorTransactionFinal execute(CuratorTransaction transaction) throws Exception;
	}
	
	public synchronized void notifyZk() {
		
		try {
			// 事务处理
			transaction(transaction -> {
				CuratorTransactionFinal curatorTransactionFinal = null;
				
				if (!checkExists(PATH)) {
					curatorTransactionFinal = transaction.create().withMode(CreateMode.PERSISTENT)
						.forPath(PATH, REFRESH_DATA.getBytes(Charset.forName("utf-8"))).and();
				} else {
					curatorTransactionFinal = transaction.setData()
						.forPath(PATH, REFRESH_DATA.getBytes(Charset.forName("utf-8"))).and();
				}
				
				return curatorTransactionFinal;
			});
		} catch (Exception e) {
			throw new NestError(String.format("通知ZK节点时出错，path=(%s)……", PATH), e);
		}
	}
	
	public NodeCache getCurrentNodeCache() {
		return currentNodeCache;
	}
	
	public CuratorFramework getZk() {
		return zk;
	}
}
