/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/7-18:16 创建
 *
 */

package com.global.adk.flow.state.retry;

import com.global.adk.flow.annotation.After;
import com.global.adk.flow.annotation.Before;
import com.global.adk.flow.annotation.Executor;
import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.RetryNode;
import com.global.adk.flow.state.FlowTrace;

import java.util.Date;
import java.util.Map;

/**
 * 重试服务触发类.需要子类继承.(因为重试必然需要回调得到target及attachment，不想引入别的负担，所以直接顺势利用trigger_class特性了).
 * <p/>
 * <p/>
 * <p/>
 * 如果业务在实现的时候，各个节点不需要target/attachment，则可以配置同一个trigger即可
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public abstract class FlowRetryTrigger {

    @Before
    public final void before(Execution execution) {
        //额外target、attachment处理
        moreTargetAndAttachment(execution);

        //前置回调
        FlowTrace flowTrace = execution.getFlowTrace();
        beforeRetry(flowTrace.getOrderId(), flowTrace.getFlowName(), flowTrace.getVersion(), flowTrace.getNode(),
                flowTrace.getRetryMeta().getTarget(), flowTrace.getRetryMeta().getRetryFailType(),
                flowTrace.getRetryMeta().getRetryMax(), flowTrace.getRetryTimes(), flowTrace.getStartTime(),
                flowTrace.getUpdateTime());
    }

    @Executor
    public final String execute(Execution execution) {

        RetryNode retryNode = (RetryNode) execution.getCurrentNodeExecution().currentNode();

        String result = retryNode.getRetryFailType().execute(execution);
        retryNode.getRetryFailType().prepareNext(execution);

        return result;

    }

    @After
    public final void after(Execution execution) {
        //后置回调
        FlowTrace flowTrace = execution.getFlowTrace();
        afterRetry(flowTrace.getOrderId(), flowTrace.getFlowName(), flowTrace.getVersion(), flowTrace.getNode(),
                flowTrace.getRetryMeta().getTarget(), flowTrace.getRetryMeta().getRetryFailType(),
                flowTrace.getRetryMeta().getRetryMax(), flowTrace.getRetryTimes(), flowTrace.getStartTime(),
                flowTrace.getUpdateTime());
    }

    /**
     * 重试前回调，业务自行处置
     *
     * @param orderId       订单
     * @param flowName      流程名
     * @param version       流程版本
     * @param retryNode     重试节点名
     * @param targetNode    重试目标节点名
     * @param retryFail     重试失败类型
     * @param retryMax      重试最大次数
     * @param retryTimes    已重试次数
     * @param startTime     重试开始时间
     * @param lastRetryTime 上次重试时间
     */
    protected void beforeRetry(String orderId, String flowName, int version, String retryNode,
                               String targetNode, RetryFailTypeEnum retryFail, int retryMax, int retryTimes,
                               Date startTime, Date lastRetryTime) {

    }

    /**
     * 重试后回调，业务自行处置
     *
     * @param orderId       订单
     * @param flowName      流程名
     * @param version       流程版本
     * @param retryNode     重试节点名
     * @param targetNode    重试目标节点名
     * @param retryFail     重试失败类型
     * @param retryMax      重试最大次数
     * @param retryTimes    已重试次数
     * @param startTime     重试开始时间
     * @param lastRetryTime 上次重试时间
     */
    protected void afterRetry(String orderId, String flowName, int version, String retryNode,
                              String targetNode, RetryFailTypeEnum retryFail, int retryMax, int retryTimes,
                              Date startTime, Date lastRetryTime) {

    }

    /**
     * 根据orderId加载聚合目标.可以不用处理,自动返回裂化设置了
     * <p/>
     * 这里提供一个覆盖业务对象的机会
     *
     * @param orderId 订单
     * @return 聚合目标
     */
    protected Object target(String orderId) {
        return null;
    }

    /**
     * 根据orderId加载附属信息.可以不用处理,自动返回裂化设置了
     * <p/>
     * 这里提供一个获取额外信息的机会
     *
     * @param orderId 订单
     * @return 附属信息
     */
    protected Map<String, Object> attachment(String orderId, Object target) {
        return null;
    }

    private void moreTargetAndAttachment(Execution execution) {
        if (isFromSchedule(execution)) {
            String orderId = execution.orderId();

            Object target = target(orderId);
            if (null != target) {
                execution.setTarget(target);
            }

            Map<String, Object> attachment = attachment(orderId, target);
            if (null != attachment && attachment.size() > 0) {
                execution.getAttachment().putAll(attachment);
            }

            execution.getFlowTrace().updateMeta(execution.getTarget(), execution.getAttachment());
        }
    }

    private boolean isFromSchedule(Execution execution) {
        return execution.getFlowTrace().isFromSchedule();
    }

}
