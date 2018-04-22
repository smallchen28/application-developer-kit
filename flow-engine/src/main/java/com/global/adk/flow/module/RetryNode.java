/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/7-18:29 创建
 *
 */

package com.global.adk.flow.module;

import com.global.adk.common.exception.FlowException;
import com.global.adk.flow.state.retry.RetryFailTypeEnum;
import com.global.adk.flow.state.retry.RetryRetreatTimeUnitEnum;
import com.global.adk.flow.state.retry.RetryRetreatTypeEnum;
import com.global.common.util.StringUtils;
import com.global.common.util.ToString;
import org.springframework.util.Assert;

/**
 * 重试任务节点
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class RetryNode extends StandardActivityNode {

    //重试结束，直接到结束节点
    public static final String RETRY_END_EVENT = "retry_end";
    //正常结束
    public static final String RETRY_END_NORMAL_EVENT = "retry_normal_end";
    //重试节点到目标节点
    public static final String RETRY_TO_TARGET_EVENT = "retry_to_target";
    //目标节点到重试节点
    public static final String RETRY_FROM_TARGET_EVENT = "retry_from_target";
    //重试退出，到当前节点的下一个非重试节点
    public static final String RETRY_EXIT_EVENT = "retry_exit";
    //重试超限最大次数事件
    public static final String RETRY_MAX_LIMIT_EVENT = "retry_max_limit_event";

    /**
     * 默认重试最大次数，防止未知问题
     */
    public static final int DEFAULT_RETRY_RETREAT_MAX = 8;
    public static final int DEFAULT_RETRY_FAST_MAX = 3;

    /**
     * 重试节点目标
     */
    private String target;

    /**
     * 重试信息
     */
    private String retryInfo;

    /**
     * 重试最大超限后流向节点
     */
    private String retryMaxLimitNode;

    /**
     * 重试失败类型
     */
    private RetryFailTypeEnum retryFailType = RetryFailTypeEnum.FAIL_RETREAT;

    /**
     * 最大重试次数
     */
    private Integer retryMax;

    /**
     * 退避单元
     */
    private Integer retreatUnit;

    /**
     * 退避单位
     */
    private RetryRetreatTimeUnitEnum retreatTimeUnit;

    /**
     * 退避类型
     */
    private RetryRetreatTypeEnum retreatType;

    /**
     * 沉睡时间,毫秒
     */
    private long failFastTimeMills = 0;

    public void addRetryInit(Flow flow) {
        checkError(flow);

        Condition condition = new Condition();
        this.setCondition(condition);

        //结束流转
        addRetryEndTransition(flow);
        addRetryEndNormalTransition(flow);
        //重试超限流转节点
        addRetryMaxLimitTransition(flow);
        //重试流转到目标节点
        addRetryToTargetTransition(flow);
        //处理目标节点
        addFromTargetTransition(flow);
    }

    private void checkError(Flow flow) {
        //防止死循环
        if (StringUtils.isNotEmpty(retryMaxLimitNode) && StringUtils.equals(retryMaxLimitNode, target)) {
            throw new FlowException(String.format("重试节点[%s]的retryMaxLimitNode不能与target[%s]相同", getName(), target));
        }

        if (flow.notExistNode(target)) {
            throw new FlowException(String.format("重试节点[%s]的target[%s]节点不存在", getName(), target));
        }

        if (StringUtils.isNotEmpty(retryMaxLimitNode) && flow.notExistNode(retryMaxLimitNode)) {
            throw new FlowException(String.format("重试节点[%s]的retryMaxLimitNode[%s]节点不存在", getName(), retryMaxLimitNode));
        }
    }

    private void addRetryEndTransition(Flow flow) {
        Transition endTransition = new Transition();
        endTransition.setEvent(RETRY_END_EVENT);
        endTransition.setFrom(this);
        endTransition.setDescription("重试流程结束");
        endTransition.setTo(new NodeRef(flow.getEndNode().getName()));
        this.getCondition().addTransition(endTransition);
        flow.addEvent(endTransition.getEvent());
    }

    private void addRetryEndNormalTransition(Flow flow) {
        Transition endTransition = new Transition();
        endTransition.setEvent(RETRY_END_NORMAL_EVENT);
        endTransition.setFrom(this);
        endTransition.setDescription("重试流程正常结束(时间未到,不执行重试)");
        endTransition.setTo(new NodeRef(flow.getEndNode().getName()));
        this.getCondition().addTransition(endTransition);
        flow.addEvent(endTransition.getEvent());
    }

    private void addRetryMaxLimitTransition(Flow flow) {
        if (StringUtils.isNotEmpty(retryMaxLimitNode)) {
            Transition maxLimitTransition = new Transition();
            maxLimitTransition.setEvent(RETRY_MAX_LIMIT_EVENT);
            maxLimitTransition.setFrom(this);
            maxLimitTransition.setDescription("重试流程超限");
            NodeRef limitRef = new NodeRef(this.getRetryMaxLimitNode());
            limitRef.initialize(flow);
            maxLimitTransition.setTo(limitRef);
            this.getCondition().addTransition(maxLimitTransition);
            flow.addEvent(maxLimitTransition.getEvent());
        }
    }

    private void addRetryToTargetTransition(Flow flow) {
        Transition retryTransition = new Transition();
        retryTransition.setEvent(RETRY_TO_TARGET_EVENT);
        retryTransition.setDescription("重试到目标节点:" + this.target);
        retryTransition.setFrom(this);
        NodeRef retryRef = new NodeRef(this.getTarget());
        retryRef.initialize(flow);
        retryTransition.setTo(retryRef);
        this.getCondition().addTransition(retryTransition);
        flow.addEvent(retryTransition.getEvent());
    }

    private void addFromTargetTransition(Flow flow) {
        ActivityNode node = flow.nodeByName(this.target);

        //目标流转到重试节点
        Transition retryFromTransition = new Transition();
        retryFromTransition.setFrom(node);
        retryFromTransition.setEvent(RETRY_FROM_TARGET_EVENT);
        retryFromTransition.setDescription("进入重试节点:" + getName());
        NodeRef retryRef = new NodeRef(getName());
        retryRef.initialize(flow);
        retryFromTransition.setTo(retryRef);
        node.getCondition().addTransition(retryFromTransition);
        flow.addEvent(retryFromTransition.getEvent());

        node.setRetryNode(this);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public RetryFailTypeEnum getRetryFailType() {
        return retryFailType;
    }

    public void setRetryFailType(RetryFailTypeEnum retryFailType) {
        this.retryFailType = retryFailType;

        if (RetryFailTypeEnum.FAIL_FAST == retryFailType) {
            String[] params = retryInfo.split(",");
            setRetryMax(Integer.valueOf(params[0]));
            if (params.length == 2) {
                setFailFastTimeMills(Long.parseLong(params[1]));
            }
        }
        if (RetryFailTypeEnum.FAIL_BOMB == retryFailType) {
            setRetryMax(Integer.valueOf(retryInfo));
        }
        if (RetryFailTypeEnum.FAIL_RETREAT == retryFailType) {
            String[] params = retryInfo.split(",");
            Assert.isTrue(4 == params.length);

            setRetryMax(Integer.parseInt(params[0]));
            setRetreatUnit(Integer.parseInt(params[1]));
            setRetreatTimeUnit(RetryRetreatTimeUnitEnum.getByCode(params[2]));
            setRetreatType(RetryRetreatTypeEnum.getByCode(params[3]));
        }
    }

    public String getRetryInfo() {
        return retryInfo;
    }

    public void setRetryInfo(String retryInfo) {
        this.retryInfo = retryInfo;
    }

    public Integer getRetryMax() {
        return retryMax;
    }

    public void setRetryMax(Integer retryMax) {
        if (null == retryFailType) {
            throw new IllegalArgumentException("请先设置retryFail类型");
        }

        if (retryFailType == RetryFailTypeEnum.FAIL_RETREAT) {
            if (retryMax > DEFAULT_RETRY_RETREAT_MAX) {
                this.retryMax = DEFAULT_RETRY_RETREAT_MAX;
                return;
            }
        }

        if (retryFailType == RetryFailTypeEnum.FAIL_FAST) {
            if (retryMax > DEFAULT_RETRY_FAST_MAX) {
                this.retryMax = DEFAULT_RETRY_FAST_MAX;
                return;
            }
        }

        this.retryMax = retryMax;
    }

    public Integer getRetreatUnit() {
        return retreatUnit;
    }

    public void setRetreatUnit(Integer retreatUnit) {
        this.retreatUnit = retreatUnit;
    }

    public RetryRetreatTimeUnitEnum getRetreatTimeUnit() {
        return retreatTimeUnit;
    }

    public void setRetreatTimeUnit(RetryRetreatTimeUnitEnum retreatTimeUnit) {
        this.retreatTimeUnit = retreatTimeUnit;
    }

    public RetryRetreatTypeEnum getRetreatType() {
        return retreatType;
    }

    public void setRetreatType(RetryRetreatTypeEnum retreatType) {
        this.retreatType = retreatType;
    }

    public String getRetryMaxLimitNode() {
        return retryMaxLimitNode;
    }

    public void setRetryMaxLimitNode(String retryMaxLimitNode) {
        this.retryMaxLimitNode = retryMaxLimitNode;
    }

    public long getFailFastTimeMills() {
        return failFastTimeMills;
    }

    public void setFailFastTimeMills(long failFastTimeMills) {
        this.failFastTimeMills = failFastTimeMills;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
