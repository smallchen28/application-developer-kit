/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 15:41 创建
 *
 */
package com.yiji.adk.filefront.listeners.events;

import com.yiji.adk.filefront.listeners.events.inner.FileNotifyEvent;

/**
 * 请求文件幂等处理事件,业务方可捕获此事件,用于业务方做逻辑操作，但不会影响响应结果
 * 
 * @author karott
 */
public class RequestFileNotifyIdempotencedEvent extends FileNotifyEvent {

}
