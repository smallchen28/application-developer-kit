/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-11-01 15:55 创建
 *
 */
package com.global.adk.biz.executor.statement;

import com.global.adk.biz.executor.event.ServiceApplyEvent;
import com.global.adk.biz.executor.event.ServiceFinishEvent;
import com.global.adk.event.Subscribe;
import com.yjf.common.service.Order;
import org.slf4j.MDC;

/**
 * 非dubbo请求mdc兼容处理.
 * <p/>
 * 考虑到<code>com.yiji.boot.dubbo.MDCFilter</code>在dubbo链路也做了mdc处理,故此兼容一下下
 *
 * @author karott
 */
public class MDCStatement {
	
	private static final String GID = "gid";
	private static final String SHOULE_REMOVE_GID = "shouleRemoveGid";
	
	@Subscribe(priority = -1, isAsync = false)
	public void mdcGidOn(ServiceApplyEvent event) {
		if (null == MDC.get(GID)) {
			Object param = event.value().getParameter();
			
			if (null != param && param instanceof Order) {
				MDC.put(GID, ((Order) param).getGid());
				event.value().putAttribute(SHOULE_REMOVE_GID, true);
			}
		}
	}
	
	@Subscribe(priority = Integer.MAX_VALUE - 1, isAsync = false)
	public void mdcGidOff(ServiceFinishEvent event) {
		if (null != event.value().getAttribute(SHOULE_REMOVE_GID)) {
			MDC.remove(GID);
		}
	}
}
