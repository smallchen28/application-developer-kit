/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.module;

import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.validate.NodeRefConstraint;

import javax.validation.constraints.NotNull;

/**
 * @author hasulee
 * @author karott 添加重试支持 [1.2.21-SNAPSHOT line 44]
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-13 下午3:02<br>
 * @see
 * @since 1.0.0
 */
public class Transition extends AbstractNode {
    @NotNull
    private String event;

    @NotNull
    private String description;

    //这里的节点内容已经进行校验无需再次校验，并且由于存在循环依赖会造成死循环
    @NotNull
    private FlowNode from;

    @NodeRefConstraint
    private NodeRef to;

    @Override
    public void initialize(Flow flow) {
        to.initialize(flow);
    }

    @Override
    public void execute(Execution execution) {
        //重试监听,主要用于重试感知
        retryListen(execution);

        FlowNode flowNode = to.getFlowNode();
        if (flowNode instanceof StandardActivityNode) {
            if (((StandardActivityNode) flowNode).getNodeType() == NodeType.ACTIVE_NODE) {
                return;
            }
        }

        to.execute(execution);
    }

    private void retryListen(Execution execution) {
        //exit retry
        if (exitRetry(execution)) {
            execution.getEngine().getListenerDelegateContext().action(execution, RetryNode.RETRY_EXIT_EVENT);
        }
    }

    private boolean fromTarget() {
        return null != from.getRetryNode() && (to.getFlowNode() instanceof RetryNode);
    }

    private boolean toTarget() {
        return from instanceof RetryNode && null != to.getFlowNode().getRetryNode();
    }

    private boolean exitRetry(Execution execution) {
        //1、flowTrace存在 2、上游节点有重试节点 3、当前流转目标节点非重试节点
        return null != execution.getFlowTrace() && null != from.getRetryNode()
                && !(to.getFlowNode() instanceof RetryNode);
    }

    private boolean endRetry() {
        return RetryNode.RETRY_END_EVENT.equals(event);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FlowNode getFrom() {
        return from;
    }

    public void setFrom(FlowNode from) {
        this.from = from;
    }

    public NodeRef getTo() {
        return to;
    }

    public void setTo(NodeRef to) {
        this.to = to;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("com.global.adk.flow.module.Transition{");
        sb.append("description='").append(description).append('\'');
        sb.append(", event='").append(event).append('\'');
        sb.append(", from=").append(from.getName());
        sb.append(", to=").append(to);
        sb.append('}');
        return sb.toString();
    }
}
