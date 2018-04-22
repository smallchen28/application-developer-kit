/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-03-01 15:56 创建
 *
 */
package com.global.adk.flow.state.retry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.global.common.util.StringUtils;
import com.global.common.util.ToString;

import java.util.Map;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class RetryMeta {
    /**
     * 重试名称
     */
    private String nodeName;
    /**
     * 最大重试次数
     */
    private Integer retryMax;
    /**
     * 重试超限流转节点
     */
    private String retryMaxLimitNode;
    /**
     * 重试节点目标
     */
    private String target;
    /**
     * 重试失败类型
     */
    private RetryFailTypeEnum retryFailType = RetryFailTypeEnum.FAIL_RETREAT;
    /**
     * 退避单元
     */
    private Integer retreatUnit = 1;
    /**
     * 退避类型
     */
    private RetryRetreatTypeEnum retreatType = RetryRetreatTypeEnum.BY_DOUBLE;
    /**
     * 退避单位
     */
    private RetryRetreatTimeUnitEnum retreatTimeUnit = RetryRetreatTimeUnitEnum.HOUR;

    /**
     * 重试当前状态下的聚合业务对象
     */
    private Object executionTarget;
    /**
     * 重试当前状态下的聚合业务扩展信息
     */
    private Map<String, Object> attachment = Maps.newHashMap();

    private long failFastTimeMills = 0;

    public String executionTargetString() {
        if (null == executionTarget) {
            return "";
        }
        return JSON.toJSONString(executionTarget, SerializerFeature.WriteClassName);
    }

    public void executionTarget(String json) {
        if (StringUtils.isNotBlank(json)) {
            setExecutionTarget(JSON.parse(json));
        }
    }


    public String attachmentString() {
        if (null == attachment) {
            return "";
        }

        return JSON.toJSONString(attachment, SerializerFeature.WriteClassName);
    }


    public void attachment(String json) {
        if (StringUtils.isNotBlank(json)) {
            setAttachment(JSON.parseObject(json));
        }
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getRetryMax() {
        return retryMax;
    }

    public void setRetryMax(Integer retryMax) {
        this.retryMax = retryMax;
    }

    public String getRetryMaxLimitNode() {
        return retryMaxLimitNode;
    }

    public void setRetryMaxLimitNode(String retryMaxLimitNode) {
        this.retryMaxLimitNode = retryMaxLimitNode;
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
    }

    public Integer getRetreatUnit() {
        return retreatUnit;
    }

    public void setRetreatUnit(Integer retreatUnit) {
        this.retreatUnit = retreatUnit;
    }

    public RetryRetreatTypeEnum getRetreatType() {
        return retreatType;
    }

    public void setRetreatType(RetryRetreatTypeEnum retreatType) {
        this.retreatType = retreatType;
    }

    public RetryRetreatTimeUnitEnum getRetreatTimeUnit() {
        return retreatTimeUnit;
    }

    public void setRetreatTimeUnit(RetryRetreatTimeUnitEnum retreatTimeUnit) {
        this.retreatTimeUnit = retreatTimeUnit;
    }

    public Object getExecutionTarget() {
        return executionTarget;
    }

    public void setExecutionTarget(Object executionTarget) {
        this.executionTarget = executionTarget;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
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
