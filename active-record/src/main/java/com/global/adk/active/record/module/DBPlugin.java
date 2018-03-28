package com.global.adk.active.record.module;

import java.sql.Timestamp;

public interface DBPlugin {
	
	/**
	 * 获取数据库时间
	 * @return
	 */
	Timestamp currentTimestamp();
	
	/**
	 * 获取下一个sequence值
	 * @param sequenceName
	 * @return
	 */
	Long nextVar(String sequenceName);
	
	/**
	 * 阻塞锁，简单使用可以是db,接耦实现采取zookeeper、
	 * @param policy 策略定义，特殊情况下可以采用多策略实现同模块＋同名锁
	 * @param module 模块
	 * @param lockName 锁名称
	 */
	void lock(String policy, String module, String lockName);
	
	/**
	 * 非阻塞锁，直接报告数据库no waite错误，简单使用可以是db,接耦实现采取zookeeper、
	 * @param policy 策略定义，特殊情况下可以采用多策略实现同模块＋同名锁
	 * @param module 模块
	 * @param lockName 锁名称
	 */
	void lockNoWaite(String policy, String module, String lockName);
}
