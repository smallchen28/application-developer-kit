/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 依赖spring生命周期
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-1-22 下午7:54<br>
 * @see
 * @since 1.0.0
 */
public interface Lifecycle extends InitializingBean, DisposableBean {
}
