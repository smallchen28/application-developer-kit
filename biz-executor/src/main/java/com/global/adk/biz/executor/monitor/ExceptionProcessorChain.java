/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88"   .   "88
 *                            (|  -_-  |)
 *                            O\  =   /O
 *                         ____/`---'\____
 *                       .'  \\|        |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -   /// |   |
 *                     | \_|  ''\--- /''   |   |
 *                     \  .-\__  `-`   ___/-. /
 *                   ___`. .'  /--.--\   `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/    - ` : | |
 *               \  \ `-.   \_ __\ /__ _/ .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *
 *  www.yiji.com Inc.
 *  Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.biz.executor.monitor;

import com.yiji.adk.biz.executor.ServiceContext;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Arrays;

/**
 * 错误处理器链路
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月25日 下午3:06:01<br>
 */

@NotThreadSafe
public class ExceptionProcessorChain implements Cloneable {
	
	private int index = 0;
	
	private int length = 10;
	
	private int increment = 10;
	
	private int position = 0;
	
	private ExceptionProcessorConfig[] configs = new ExceptionProcessorConfig[length];
	
	public synchronized void addAll(ExceptionProcessorConfig[] configs) {
		for (ExceptionProcessorConfig config : configs) {
			add(config);
		}
	}
	
	public synchronized ExceptionProcessorChain add(ExceptionProcessorConfig config) {
		
		//- 索引达到上限，开始扩容
		if (index == length) {
			ExceptionProcessorConfig[] newConfigs = new ExceptionProcessorConfig[length + increment];
			
			System.arraycopy(configs, 0, newConfigs, 0, length);
			
			configs = newConfigs;
		}
		configs[index++] = config;
		
		return this;
	}
	
	public boolean run(Throwable e, ServiceContext<?, ?> serviceContext) {
		
		if (configs != null && configs.length > 0) {
			if (position < index) {
				ExceptionProcessorConfig config = configs[position++];
				return config.getProcessor().proceed(e, serviceContext, this);
			}
		}
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		ExceptionProcessorChain clone = new ExceptionProcessorChain();
		clone.configs = this.configs;
		clone.increment = this.increment;
		clone.index = this.index;
		clone.length = this.length;
		clone.position = this.position;
		return clone;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ExceptionProcessorChain{");
		sb.append("configs=").append(Arrays.toString(configs));
		sb.append('}');
		return sb.toString();
	}
}
