/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.engine;

import com.yiji.adk.common.log.TraceLogFactory;
import com.yiji.adk.flow.annotation.After;
import com.yiji.adk.flow.annotation.Before;
import com.yiji.adk.flow.annotation.End;
import com.yiji.adk.flow.annotation.Executor;
import com.yiji.adk.flow.delegate.InvokeDelegateContext;
import com.yiji.adk.flow.module.ActivityNode;
import com.yiji.adk.flow.module.Flow;
import com.yiji.adk.flow.module.FlowNode;
import com.yiji.adk.flow.module.RetryNode;
import com.yjf.common.log.Logger;

import java.sql.Timestamp;

/**
 * 标准节点实现
 *
 * @author hasulee
 * @author karott 添加重试尝试处理[1.21.1-SNAPSHOT line 88]
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-8-11 下午5:24<br>
 * @see
 * @since 1.0.0
 */
public class StandardNodeExecution implements NodeExecution {

    private final Logger logger;

    private FlowNode currentFlowNode;

    private final Execution execution;

    private ExecutionStatus status;

    private String decision;

    private Timestamp startTime;

    private Timestamp endTime;

    private Throwable error;

    public StandardNodeExecution(Execution execution, FlowNode flowNode) {
        this.execution = execution;
        this.currentFlowNode = flowNode;
        execution.setCurrentNodeExecution(this);
        this.logger = TraceLogFactory.getLogger(execution.getCurrentFlow().getLogName());
    }

    public void execute() {
        this.startTime = new Timestamp(System.currentTimeMillis());
        Flow flow = execution.getCurrentFlow();

        //args[0]=StateMachine.Key,args[1]=nodeName，args[2]=Annotation , args[3]=Execution
        InvokeDelegateContext invokeDelegateContext = execution.getEngine().getInvokeDelegateContext();
        Flow.Key flowKey = new Flow.Key(flow.getName(), flow.getVersion());
        String nodeName = currentFlowNode.getName();

        try {
            //-1. Before
            invokeDelegateContext.invoke(new Object[]{flowKey, nodeName, Before.class, execution});
            //-2. Executor
            Object executorResult = invokeDelegateContext
                    .invoke(new Object[]{flowKey, nodeName, Executor.class, execution});
            //-3. After
            invokeDelegateContext.invoke(new Object[]{flowKey, nodeName, After.class, execution});

            if (executorResult != null) {
                decision = executorResult.toString();
            }
            //-4. 变更状态
            status = ExecutionStatus.SUCCESS;

        } catch (Throwable throwable) {
            status = ExecutionStatus.FAIL;
            error = throwable;
            Object result = invokeDelegateContext
                    .invoke(new Object[]{flowKey, nodeName, com.yiji.adk.flow.annotation.Error.class, execution});

            //不存在Error处理器,尝试重试
            if (null == result && execution.isRetryEnable()) {
                execution.setError(throwable);
                result = tryToFindTheFirstRetryNodeIfPossible(currentFlowNode, execution);
            }

            //无重试处理动作,异常上抛出
            if (null == result) {
                throw throwable;
            }

            decision = result.toString();
        } finally {
            //-4. End
            invokeDelegateContext.invoke(new Object[]{flowKey, nodeName, End.class, execution});
            this.endTime = new Timestamp(System.currentTimeMillis());
            if (currentFlowNode.isTraceLog() && logger.isInfoEnabled()) {
                logger.info(
                        "执行完成NodeName={},StateMachine={},Version={},NodeExecutionStatus={},Target={},Attachment={},耗时：{}毫秒",
                        currentFlowNode.getName(), flow.getName(), flow.getVersion(), status, execution.getTarget(),
                        execution.getAttachment(), endTime.getTime() - startTime.getTime());
            }
        }
        //通过Condition进行流转
        decision(currentFlowNode, execution);
    }

    private void decision(FlowNode flowNode, Execution execution) {
        if (flowNode instanceof ActivityNode) {
            ActivityNode activityNode = (ActivityNode) flowNode;

            com.yiji.adk.flow.module.Condition condition = activityNode.getCondition();
            condition.execute(execution);

        } //其他不会出现Flow的状况，EndNode则再次结束，忽略其他。。。。。。。。。。
    }

    /**
     * 尝试查找重试节点，找不到意味着不需要重试，从设计上来说应该失败或者不合理
     */
    private String tryToFindTheFirstRetryNodeIfPossible(FlowNode flowNode, Execution execution) {
        if (execution.isRetryEnable()) {
            if (flowNode instanceof ActivityNode) {
                if (null != flowNode.getRetryNode()) {
                    return RetryNode.RETRY_FROM_TARGET_EVENT;
                }
            }
        }

        return null;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public FlowNode getCurrentFlowNode() {
        return currentFlowNode;
    }

    public FlowNode currentNode() {
        return currentFlowNode;
    }

    public String decision() {
        return this.decision;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.engine.StandardNodeExecution{");
        sb.append(", endTime=").append(endTime);
        sb.append(", execution=").append(execution);
        sb.append(", error=").append(error);
        sb.append(", startTime=").append(startTime);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

}
