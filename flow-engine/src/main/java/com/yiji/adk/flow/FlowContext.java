/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow;

import java.util.Map;

/**
 * 重试能力启用时({@link com.yiji.adk.flow.boot.FlowEngineProperties#enableRetry}
 * ==true)，需要有attachment.put(FlowContext.ORDER_ID,orderId)
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-1-19 下午5:34<br>
 * @see
 * @since 1.0.0
 */
public interface FlowContext extends FlowDefReader, FlowDefRegistry {

    String ORDER_ID = "orderId";
    String FLOW_TRACE = "flowTrace";
    String RETRY_ENABLE = "retryEnable";

    void start(String flowName, int version, Object target);

    void start(String flowName, int version, Map<String, Object> attachment);

    void start(String flowName, int version, Object target, Map<String, Object> attachment);

    void start(String flowName, Object target);

    void start(String flowName, Map<String, Object> attachment);

    void start(String flowName, Object target, Map<String, Object> attachment);

    void execute(String flowName, String activeNode, Map<String, Object> attachment);

    void execute(String flowName, String activeNode, Object target);

    void execute(String flowName, String activeNode, Object target, Map<String, Object> attachment);

    void execute(String flowName, String activeNode, int version, Object target);

    void execute(String flowName, String activeNode, int version, Map<String, Object> attachment);

    void execute(String flowName, String activeNode, int version, Object target, Map<String, Object> attachment);
}
