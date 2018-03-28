/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-02-14 17:10 创建
 *
 */
package com.yiji.adk.flow.state.retry;

import com.yiji.adk.active.record.module.DBPlugin;
import com.yiji.adk.common.exception.FlowException;
import com.yiji.adk.common.exception.KitNestException;
import com.yiji.adk.flow.annotation.Listen;
import com.yiji.adk.flow.engine.Execution;
import com.yiji.adk.flow.module.RetryNode;
import com.yiji.adk.flow.state.FlowHistoryTrace;
import com.yiji.adk.flow.state.FlowHistoryTraceRepository;
import com.yiji.adk.flow.state.FlowTrace;
import com.yiji.adk.flow.state.FlowTraceRepository;
import com.yjf.common.id.GID;
import com.yjf.common.lang.beans.Copier;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 流程重试流转监听
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
@SuppressWarnings("all")
public class RetryTransitionListener {

    private static final Logger logger = LoggerFactory.getLogger(RetryTransitionListener.class);

    @Autowired
    private FlowTraceRepository flowTraceRepository;
    @Autowired
    private FlowHistoryTraceRepository flowHistoryTraceRepository;
    @Autowired
    private TxWrapper txWrapper;
    @Autowired(required = false)
    private DBPlugin dbPlugin;


    @Listen(eventExpression = RetryNode.RETRY_FROM_TARGET_EVENT)
    public void listenRetryFromTarget(Execution execution, String eventName) {
        if (execution.isRetryEnable()) {
            throwErrorIfPossible(execution, RetryNode.RETRY_FROM_TARGET_EVENT);

            FlowTrace flowTrace = execution.getFlowTrace();
            if (null == flowTrace) {
                RetryNode retryNode = execution.getCurrentNodeExecution().currentNode().getRetryNode();

                flowTrace = new FlowTrace(GID.newGID());

                Copier.copy(retryNode, flowTrace);
                flowTrace.setTraceId(GID.newGID());
                flowTrace.setOrderId(execution.orderId());
                if (null != dbPlugin) {
                    flowTrace.setStartTime(dbPlugin.currentTimestamp());
                } else {
                    logger.warn("没有DbPlugin实现，使用应用时间(可能会导致时间不一致)");
                    flowTrace.setStartTime(new Date());
                }
                flowTrace.setEventTime(flowTrace.getStartTime());
                flowTrace.setNextRetryTime(flowTrace.getEventTime());
                flowTrace.setFlowName(execution.getCurrentFlow().getName());
                flowTrace.setVersion(execution.getCurrentFlow().getVersion());
                flowTrace.setNode(retryNode.getName());

                Copier.copy(retryNode, flowTrace.getRetryMeta());
                flowTrace.getRetryMeta().setNodeName(retryNode.getName());
                //计算下次执行时间
                flowTrace.retreatNextTime();
                flowTrace.getRetryMeta().setFailFastTimeMills(retryNode.getFailFastTimeMills());


                execution.setFlowTrace(flowTrace);

                if (needPersist(flowTrace)) {
                    final FlowTrace finalFlowTrace = flowTrace;
                    txWrapper.withNewTx(new TxWrapper.TxCallback() {
                        @Override
                        public void doWithNewTx() {
                            flowTraceRepository.store(finalFlowTrace);
                        }
                    });
                    if (!flowTraceRepository.lock(flowTrace)) {
                        throw new FlowException(String.format("锁定trace异常.%s", flowTrace));
                    }
                }
            }
            flowTrace.setFlag(-1);

            logger.debug("[流转监听-目标到重试,RETRY_FROM_TARGET_EVENT].traceId:{},orderId:{},transition:{}->{}", flowTrace.getTraceId(),
                    flowTrace.getOrderId(), flowTrace.getRetryMeta().getTarget(), flowTrace.getRetryMeta().getNodeName());
        }
    }

    @Listen(eventExpression = RetryNode.RETRY_TO_TARGET_EVENT)
    public void listenRetryToTarget(Execution execution, String eventName) {
        if (execution.isRetryEnable()) {
            RetryNode retryNode = (RetryNode) execution.getCurrentNodeExecution().currentNode();

            logger.warn("[流转监听-重试到目标,RETRY_TO_TARGET_EVENT].traceId:{},orderId:{},transition:{}->{}", execution.getFlowTrace().getTraceId(),
                    execution.getFlowTrace().getOrderId(), retryNode.getName(), retryNode.getTarget());

            if (needPersist(retryNode.getRetryFailType())) {
                flowTraceRepository.restore(execution.getFlowTrace());
            }
        }
    }

    @Listen(eventExpression = RetryNode.RETRY_EXIT_EVENT)
    public void listenRetryExit(Execution execution, String eventName) {
        if (execution.isRetryEnable()) {
            FlowTrace currentTrace = execution.getFlowTrace();

            logger.debug("[流转监听-重试退出,RETRY_EXIT_EVENT].traceId:{},orderId:{},transition:{}->{}", currentTrace.getTraceId(),
                    currentTrace.getOrderId(), execution.getCurrentNodeExecution().currentNode().getName(), "?");

            if (needPersist(currentTrace)) {
                FlowHistoryTrace historyTrace = currentTrace.end(String.format("重试退出[RETRY_EXIT_EVENT].max:%s,times:%s",
                        currentTrace.getRetryMeta().getRetryMax(), currentTrace.getRetryTimes()));
                flowTraceRepository.remove(currentTrace);
                flowHistoryTraceRepository.store(historyTrace);
            }

            execution.setFlowTrace(null);
        }
    }

    @Listen(eventExpression = RetryNode.RETRY_MAX_LIMIT_EVENT)
    public void listenRetryMaxLimitTo(Execution execution, String eventName) {
        if (execution.isRetryEnable()) {
            FlowTrace currentTrace = execution.getFlowTrace();

            logger.info("[流转监听-重试到超限节点,RETRY_MAX_LIMIT_EVENT].traceId:{},orderId:{},transition:{}->{}", currentTrace.getTraceId(),
                    currentTrace.getOrderId(), execution.getCurrentNodeExecution().currentNode().getName(),
                    currentTrace.getRetryMeta().getRetryMaxLimitNode());

            if (needPersist(currentTrace)) {
                FlowHistoryTrace historyTrace = currentTrace.end(
                        String.format("重试到超限节点[RETRY_MAX_LIMIT_EVENT].max:%s,times:%s,limitNode:%s", currentTrace.getRetryMeta().getRetryMax(),
                                currentTrace.getRetryTimes(), currentTrace.getRetryMeta().getRetryMaxLimitNode()));
                flowTraceRepository.remove(currentTrace);
                flowHistoryTraceRepository.store(historyTrace);
            }

            execution.setFlowTrace(null);
        }
    }

    @Listen(eventExpression = RetryNode.RETRY_END_EVENT)
    public void listenRetryEnd(Execution execution, String eventName) {
        if (execution.isRetryEnable()) {
            FlowTrace currentTrace = execution.getFlowTrace();

            logger.warn("[流转监听-重试结束,RETRY_END_EVENT].traceId:{},orderId:{},transition:{}->{}", currentTrace.getTraceId(),
                    currentTrace.getOrderId(), currentTrace.getRetryMeta().getNodeName(),
                    execution.getCurrentFlow().getEndNode().getName());

            if (needPersist(currentTrace)) {
                FlowHistoryTrace historyTrace = currentTrace.end(String.format("重试结束[RETRY_END_EVENT].max:%s,times:%s",
                        currentTrace.getRetryMeta().getRetryMax(), currentTrace.getRetryTimes()));

                if (currentTrace.limitMax()) {
                    flowTraceRepository.remove(currentTrace);
                    flowHistoryTraceRepository.store(historyTrace);
                }
            }

            execution.setFlowTrace(null);
        }
    }

    private void throwErrorIfPossible(Execution execution, String event) {
        if (null != execution.getError()) {
            if (execution.getError() instanceof KitNestException) {
                logger.warn("重试诱因.flow:{},version:{},node:{},cause:{}", execution.getCurrentFlow().getName(),
                        execution.getCurrentFlow().getVersion(), execution.getCurrentNodeExecution().currentNode().getName(), execution.getError().getMessage());
            } else {
                logger.error("重试诱因.flow:{},version:{},node:{}", execution.getCurrentFlow().getName(),
                        execution.getCurrentFlow().getVersion(), execution.getCurrentNodeExecution().currentNode().getName(), execution.getError());
            }
        }
    }

    //快速失败不需要存储
    private boolean needPersist(FlowTrace flowTrace) {
        return needPersist(flowTrace.getRetryMeta().getRetryFailType());
    }

    private boolean needPersist(RetryFailTypeEnum type) {
        return RetryFailTypeEnum.FAIL_FAST != type;
    }

}
