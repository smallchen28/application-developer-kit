/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/7-11:04 创建
 *
 */

package com.global.adk.flow.state;

import com.google.common.collect.Maps;
import com.yjf.common.id.GID;
import com.yjf.common.util.ToString;

import java.util.Date;
import java.util.Map;

/**
 * 出现异常，抛出{@link com.yiji.adk.common.exception.FlowException}
 * <p/>
 * 1、事务required 2、事件同步处理
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class FlowTraceEvent {

    /**
     * 事件id
     */
    private String eventId;

    /**
     * 事件时间,可由业务事件覆盖,影响流程恢复优先级
     */
    private Date eventTime;

    //+++++++++++++++++++++++++++++辅助用++++++++++++++++++++++++++
    @ToString.Invisible
    private Map<String, String> context = Maps.newHashMap();
    //+++++++++++++++++++++++++++++辅助用++++++++++++++++++++++++++

    public FlowTraceEvent() {
        this(GID.newGID());
    }

    public FlowTraceEvent(String eventId) {
        this(eventId, new Date());
    }

    public FlowTraceEvent(String eventId, Date eventTime) {
        this.eventId = eventId;
        this.eventTime = eventTime;
    }

    public String getEventId() {
        return eventId;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }

}
