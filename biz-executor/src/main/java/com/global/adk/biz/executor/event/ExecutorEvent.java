package com.yiji.adk.biz.executor.event;

/**
 * 执行器事件
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月16日 上午11:34:23<br>
 */
public interface ExecutorEvent<S, V> {
	
	S source();
	
	V value();
}
