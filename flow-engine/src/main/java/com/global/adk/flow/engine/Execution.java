/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.engine;

import com.global.adk.common.exception.FlowException;
import com.global.adk.common.log.TraceLogFactory;
import com.global.adk.flow.FlowContext;
import com.global.adk.flow.module.Flow;
import com.global.adk.flow.module.FlowNode;
import com.global.adk.flow.state.FlowTrace;
import com.global.common.log.Logger;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-9 下午6:33<br>
 * @see
 * @since 1.0.0
 */
public class Execution {

    private final Logger logger;

    private final FlowEngine engine;

    private final Flow currentFlow;

    private final Map<String, Object> attachment;

    private Object target;

    private Execution subExecution;

    private Timestamp startTime;

    private Timestamp endTime;

    private Throwable error;

    private NodeExecution currentNodeExecution;

    private boolean isRetryEnable = false;
    private FlowTrace flowTrace;


    public Execution(FlowEngine engine, Flow currentFlow, Object target, Map<String, Object> attachment) {
        this.engine = engine;
        this.currentFlow = currentFlow;
        this.target = target;
        this.attachment = attachment;
        this.logger = TraceLogFactory.getLogger(currentFlow.getLogName());

        init();
    }

    private void init() {
        if (null != attachment) {
            if (attachment.containsKey(FlowContext.FLOW_TRACE)) {
                FlowTrace trace = (FlowTrace) attachment.get(FlowContext.FLOW_TRACE);
                setFlowTrace(trace);
            }

            if (attachment.containsKey(FlowContext.RETRY_ENABLE)) {
                setIsRetryEnable(true);
                attachment.remove(FlowContext.RETRY_ENABLE);
            }
        }
    }

    /**
     * 获取orderId
     */
    public String orderId() {
        return getAttribute(FlowContext.ORDER_ID);
    }

    public void execute(Flow flow, FlowNode node) {
        this.startTime = new Timestamp(System.currentTimeMillis());

        if (logger.isInfoEnabled()) {
            logger.info("激活流程Flow={},Version={}，执行节点Node={},Target={},Attachment={}", flow.getName(), flow.getVersion(),
                    flow.equals(node) ? flow.getStartNode().getName() : node.getName(), target, attachment);
        }

        try {
            node.execute(this);
        } catch (Throwable throwable) {

            this.error = throwable;

            //error判断
            if (throwable instanceof Error) {
                if (logger.isErrorEnabled()) {
                    logger.error("流程执行出现ERROR级错误，无法直接处理，忽略异常监视器.Flow={},Version={},执行节点Node=【name={},class={}】",
                            flow.getName(), flow.getVersion(), node.getName(), node.getClass());
                    throw throwable;
                }
            }

            //匹配处理
            Flow.Key key = new Flow.Key(flow.getName(), flow.getVersion());
            ExceptionMonitor monitor = engine.getFlowExceptionMonitorHolder().get(key);

            //异常捕获处理修正
            //if (monitor != null
            //&& flow.getErrorMonitor().getExceptionMapping().getThrowables().contains(throwable.getClass())) {
            if (null != monitor) {
                for (Class<? extends Throwable> ex : flow.getErrorMonitor().getExceptionMapping().getThrowables()) {
                    if (ex.isInstance(throwable)) {
                        monitor.catcher(flow, node, this, throwable);
                        break;
                    }
                }
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn(
                            "执行流程出错,并没有发现错误监视器或对相应错误进行监视,Flow={},Version={},Throwable={},执行节点Node=【name={},class={}】",
                            flow.getName(), flow.getVersion(), throwable.getClass().getName(),
                            currentNodeExecution.currentNode().getName(), currentNodeExecution.currentNode().getClass());
                }
                throw throwable;
            }

        } finally {
            endTime = new Timestamp(System.currentTimeMillis());

            if (logger.isInfoEnabled()) {
                logger.info("流程执行结束结束Flow={},Version={},CurrentNode={},Target={},Attachment={},总耗时：{}毫秒",
                        flow.getName(), flow.getVersion(), currentNodeExecution.currentNode().getName(), target, attachment,
                        endTime.getTime() - startTime.getTime());
            }
        }
    }


    public void setCurrentNodeExecution(NodeExecution currentNodeExecution) {
        this.currentNodeExecution = currentNodeExecution;
    }

    public NodeExecution currentNodeExecution() {
        return currentNodeExecution;
    }

    public FlowEngine getEngine() {
        return engine;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public <T> T getAttribute(String key) {
        if (attachment == null) {
            throw new FlowException("不支持的获取操作，attachment没有初始化。");
        }

        if (null == attachment.get(key)) {
            return null;
        }

        return (T) attachment.get(key);
    }

    public <T> void addAttribute(String key, T attribute) {
        if (attachment == null) {
            throw new FlowException("不支持的添加操作，attachment没有初始化。");
        }
        attachment.put(key, attribute);
    }

    public boolean isRetryEnable() {
        return isRetryEnable;
    }

    public void setIsRetryEnable(boolean isRetryEnable) {
        this.isRetryEnable = isRetryEnable;
    }

    public FlowTrace getFlowTrace() {
        if (null != this.flowTrace) {
            this.flowTrace.updateMeta(getTarget(), getAttachment());
        }

        return this.flowTrace;
    }

    public void setFlowTrace(FlowTrace flowTrace) {
        this.flowTrace = flowTrace;
        if (null != this.flowTrace) {
            this.flowTrace.updateMeta(getTarget(), getAttachment());
        }
    }

    public <T> T getTarget() {
        return (T) target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Execution getSubExecution() {
        return subExecution;
    }

    public Flow getCurrentFlow() {
        return currentFlow;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public NodeExecution getCurrentNodeExecution() {
        return currentNodeExecution;
    }

    public void setSubExecution(Execution subExecution) {
        this.subExecution = subExecution;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.engine.Execution{");
        sb.append("Target=").append(target);
        sb.append(", Attachment=").append(attachment);
        sb.append(", StateMachine=").append(currentFlow.getName());
        sb.append(", Version=").append(currentFlow.getVersion());
        sb.append('}');
        return sb.toString();
    }
}
