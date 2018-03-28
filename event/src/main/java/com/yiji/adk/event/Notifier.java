/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * ligen@yiji.com  创建
 * qzhanbo@yiji.com 2014-11-07 01:48 修改
 *
 */
package com.yiji.adk.event;

import javax.annotation.Nonnull;

/**
 * 通知器
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 *
 * @history hasuelee创建于2014年9月30日 下午1:54:07<br>
 */
public interface Notifier {
	
	<L> void register(L listener);
	
	<L> void unregister(L listener);
	
	/**
	 * 事件分发 <br/>
	 * 注意下面三点:
	 * <p>
	 * <li>1.事件不能为null,多个事件时,其中任意一个事件也不能为空.若有有null的情况,框架会忽略此事件分发,并且会打印详细的调用方法</li>
	 * <li>2.如果监听器方法为无参,请分发{@link NoneEvent}事件</li>
	 * <li>3.不支持分发数组,需要使用数组,请改为集合</li>
	 * </p>
	 * @param events
	 * @return
	 */
	boolean dispatcher(@Nonnull Object... events);
	
}
