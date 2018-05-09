/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow;

import com.global.adk.common.exception.FlowException;
import com.yjf.common.util.StringUtils;

import java.util.Map;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-1-22 下午7:55<br>
 * @see
 * @since 1.0.0
 */
public class StandardFlowContext extends AbstractFlowContext {

    private final String location;
    private boolean retryEnable = false;

    public StandardFlowContext(String location, boolean retryEnable) {
        if (StringUtils.isBlank(location)) {
            throw new FlowException(String.format("location=%s不能为空", location));
        }
        this.location = location;
        this.retryEnable = retryEnable;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        loadDefinition(this.location);
    }

    @Override
    public void start(String flowName, Object target) {
        start(flowName, -1, target);
    }

    @Override
    public void start(String flowName, int version, Object target) {
        start(flowName, version, target, null);
    }

    @Override
    public void start(String flowName, Map<String, Object> attachment) {
        start(flowName, null, attachment);
    }

    @Override
    public void start(String flowName, int version, Map<String, Object> attachment) {
        start(flowName, version, null, attachment);
    }

    @Override
    public void start(String flowName, Object target, Map<String, Object> attachment) {
        start(flowName, -1, target, attachment);
    }

    @Override
    public void start(String flowName, int version, Object target, Map<String, Object> attachment) {
        execute(flowName, null, version, target, attachment);
    }

    @Override
    public void execute(String flowName, String activeNode, Map<String, Object> attachment) {
        execute(flowName, activeNode, null, attachment);
    }

    @Override
    public void execute(String flowName, String activeNode, Object target) {
        execute(flowName, activeNode, target, null);
    }

    @Override
    public void execute(String flowName, String activeNode, Object target, Map<String, Object> attachment) {
        execute(flowName, activeNode, -1, target, attachment);
    }

    @Override
    public void execute(String flowName, String activeNode, int version, Object target) {
        execute(flowName, activeNode, version, target, null);
    }

    @Override
    public void execute(String flowName, String activeNode, int version, Map<String, Object> attachment) {
        execute(flowName, activeNode, version, null, attachment);
    }

    @Override
    public void execute(String flowName, String activeNode, int version, Object target,
                        Map<String, Object> attachment) {
        if (retryEnable) {
            if (null == attachment) {
                throw new FlowException("重试启用时，必须要附件");
            }
            if (null == attachment.get(FlowContext.ORDER_ID)) {
                throw new FlowException("重试启用时，附件必须有orderId，请attachment.put(FlowContext.ORDER_ID,orderId)");
            }

            attachment.put(FlowContext.RETRY_ENABLE, true);
        }

        flowEngine.execute(flowName, activeNode, version, target, attachment);
    }

}
