/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-01 22:56 创建
 *
 */
package com.yiji.adk.filefront.listeners.events;

import com.yiji.adk.filefront.listeners.events.inner.FileNotifyLocalSupportEvent;

/**
 * 请求文件通知文件已解析事件.业务方完成文件解析后发出该事件
 *
 * <p/>
 * FileFront会以最低优先级接收该事件，用于改变日志状态等处理,可能会抛出{@link com.yiji.adk.filefront.exceptions.RequestFileNotifyException}
 * 异常，业务方可捕获并处理(类似为业务提供的同步回调)
 *
 * @author karott
 */
public class RequestFileNotifyParsedEvent extends FileNotifyLocalSupportEvent {
}
