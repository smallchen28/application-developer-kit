/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-02-13 18:33 创建
 *
 */
package com.yiji.adk.flow.state.retry;

import com.alibaba.dubbo.rpc.RpcContext;
import com.yiji.adk.active.record.module.DBPlugin;
import com.yiji.adk.flow.FlowContext;
import com.yiji.adk.flow.state.FlowTrace;
import com.yiji.adk.flow.state.FlowTraceRepository;
import com.yjf.common.concurrent.MonitoredThreadPoolExecutor;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import com.yjf.scheduler.service.api.ScheduleCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 重试流程服务提供,目前要求应用自行配置schedule的任务
 * <p/>
 * 配置参数: <br/>
 * <pre>
 * 	failType:失败重试类型，必须.failRetreat/failBomb
 * 	orderBy:排序字段，必须.startTime/updateTime
 * 	sort:排序方式，可选，asc/desc.默认asc
 * 	batch:批量大小，可选，默认50
 * 	retryNodes:重试节点，可选，比如 settlement`charge`account`notify
 * </pre>
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class RetryFlowProvider implements ScheduleCallBackService {
    private static final Logger logger = LoggerFactory.getLogger(RetryFlowProvider.class);

    private static final int DEFAULT_BATCH = 50;

    private FlowTraceRepository flowTraceRepository;
    private FlowContext flowContext;
    @Autowired(required = false)
    private DBPlugin dbPlugin;

    /**
     * 重试专用
     */
    private MonitoredThreadPoolExecutor executorService;

    public RetryFlowProvider(FlowTraceRepository flowTraceRepository, FlowContext flowContext) {
        this.flowTraceRepository = flowTraceRepository;
        this.flowContext = flowContext;

        init();
    }

    @Override
    public void justDoIT() {
        Map<String, String> params = RpcContext.getContext().getAttachments();

        executorService.execute(() -> {
            logger.info("收到流程重试请求:{}", params);


            String failType = params.get("failType");
            Assert.isTrue(null != failType && ("failRetreat".equals(failType) || "failBomb".equals(failType)));
            String orderBy = params.get("orderBy");
            Assert.isTrue(null != orderBy && ("startTime".equals(orderBy) || "updateTime".equals(orderBy)));
            orderBy = orderBy.replaceAll("Time", "_time");

            String sort = params.get("sort");
            String retryNodes = params.get("retryNodes");
            String batch = params.get("batch");

            int batchSize = null == batch ? DEFAULT_BATCH : Integer.parseInt(batch);
            sort = null == sort ? "asc" : sort.toUpperCase();

            retry(RetryFailTypeEnum.getByCode(failType), retryNodes, orderBy.toUpperCase(), sort.toUpperCase(),
                    batchSize);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {})
    public void retry(RetryFailTypeEnum failType, String retryNodes, String orderBy, String sort, int batch) {
        try {
            List<FlowTrace> retryTraces;

            if (null == retryNodes) {
                retryTraces = flowTraceRepository.listFlowTracesWithLock(failType, null, orderBy, sort, batch);
            } else {
                String[] nodesArray = retryNodes.split("`");
                retryTraces = flowTraceRepository.listFlowTracesWithLock(failType, Arrays.asList(nodesArray), orderBy, sort,
                        batch);
            }

            retryTraces.stream().forEach(this::retrySingleTrace);
        } catch (Exception e) {
            //保证无论如何，执行次数这些统计信息都会被更新
            logger.error("重试批次异常.", e);
        }
    }

    private void retrySingleTrace(FlowTrace flowTrace) {
        try {
            logger.info("[调度重试流程轨迹开始执行].flowTrace:{}", flowTrace);

            //设置调度标识
            flowTrace.setFromSchedule(true);

            if (null != dbPlugin) {
                flowTrace.setEventTime(dbPlugin.currentTimestamp());
            } else {
                logger.warn("没有DbPlugin实现，使用应用时间(可能会导致时间不一致)");
                flowTrace.setEventTime(new Date());
            }


            Object target = flowTrace.getRetryMeta().getExecutionTarget();
            Map<String, Object> args = flowTrace.getRetryMeta().getAttachment();
            args.put(FlowContext.FLOW_TRACE, flowTrace);
            args.put(FlowContext.ORDER_ID, flowTrace.getOrderId());

            flowContext.execute(flowTrace.getFlowName(), flowTrace.getNode(), flowTrace.getVersion(), target,
                    args);
        } catch (Exception e) {
            logger.error("[调度重试流程轨迹执行异常].flowTrace:{}", flowTrace, e);
        }
    }

    private void init() {
        executorService = new MonitoredThreadPoolExecutor(4, 50, 240, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000));
        executorService.initialize();
    }

}
