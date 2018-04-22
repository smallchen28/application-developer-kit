/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/6-19:14 创建
 *
 */

package com.global.adk.flow.state;

import com.global.adk.flow.FlowContext;
import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.RetryNode;
import com.global.adk.flow.state.retry.RetryFailTypeEnum;
import com.global.adk.flow.state.retry.RetryMeta;
import com.global.adk.flow.state.retry.RetryRetreatTimeUnitEnum;
import com.global.adk.flow.state.retry.RetryRetreatTypeEnum;
import com.global.common.id.GID;
import com.global.common.lang.beans.Copier;
import com.global.common.util.StringUtils;
import com.global.common.util.ToString;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 流程实例,这里简化，就不依赖active-record了
 * <p/>
 * exit 条件见 {@link com.global.adk.flow.module.Transition#exitRetry(Execution)}
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class FlowTrace extends FlowTraceEvent {

    private long id;

    /**
     * 流程名称
     */
    private String flowName;
    /**
     * 当前节点/重试节点
     */
    private String node;
    /**
     * 流程版本
     */
    private Integer version;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 流程轨迹id
     */
    private String traceId;

    /**
     * 重试次数
     */
    private Integer retryTimes = 0;

    /**
     * 下次重试时间
     */
    private Date nextRetryTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 重试信息
     */
    private RetryMeta retryMeta = new RetryMeta();

    /**
     * 控制当前调度只做一次处理
     */
    private int flag = 0;

    /**
     * 是否来自调度
     */
    private boolean fromSchedule = false;

    public FlowTrace() {
        this(GID.newGID());
    }

    public FlowTrace(String eventId) {
        this(eventId, DateTime.now().toDate());
    }

    public FlowTrace(String eventId, Date eventTime) {
        super(eventId, eventTime);
    }

    /**
     * 快速失败重试
     */
    public String retryFast() {
        if (limitMax()) {
            if (retryMaxLimitTo()) {
                return RetryNode.RETRY_MAX_LIMIT_EVENT;
            }

            return RetryNode.RETRY_END_EVENT;
        }

        if (!limitMax()) {

            try {
                TimeUnit.MILLISECONDS.sleep(getRetryMeta().getFailFastTimeMills());
            } catch (InterruptedException ignored) {
                //nothing
            }

            ++retryTimes;
            setUpdateTime(DateTime.now().toDate());

            return RetryNode.RETRY_TO_TARGET_EVENT;
        }

        return RetryNode.RETRY_END_EVENT;
    }

    /**
     * 重试到爆
     */
    public String retryBomb() {
        if (-1 == flag) {
            return RetryNode.RETRY_END_NORMAL_EVENT;
        }

        ++retryTimes;
        setUpdateTime(DateTime.now().toDate());

        return RetryNode.RETRY_TO_TARGET_EVENT;
    }

    /**
     * 退避重试
     */
    public String retryRetreat() {
        Assert.notNull(nextRetryTime);

        if (limitMax()) {
            if (retryMaxLimitTo()) {
                return RetryNode.RETRY_MAX_LIMIT_EVENT;
            }

            return RetryNode.RETRY_END_EVENT;
        }

        if (-1 == flag) {
            return RetryNode.RETRY_END_NORMAL_EVENT;
        }

        Date nowDate = getEventTime();
        //这里时间条件稍微有点粗暴,不保证重试重复性(需要额外存储),所以业务需要保证幂等
        if (nowDate.before(nextRetryTime)) {
            //时间未到,正常结束
            return RetryNode.RETRY_END_NORMAL_EVENT;
        }

        if (!limitMax()) {
            ++retryTimes;
            setUpdateTime(DateTime.now().toDate());
            retreatNextTime();

            return RetryNode.RETRY_TO_TARGET_EVENT;
        }

        return RetryNode.RETRY_END_EVENT;
    }

    /**
     * 结束
     */
    public FlowHistoryTrace end(String error) {
        FlowHistoryTrace historyTrace = new FlowHistoryTrace();
        Copier.copy(this, historyTrace, "eventId");
        historyTrace.setEndTime(new Date());
        historyTrace.setError(error);

        return historyTrace;
    }

    public boolean limitMax() {
        return retryTimes >= getRetryMeta().getRetryMax();
    }

    public static FlowTrace convertFromMap(DBDialectSupport.AdaptiveDataMap map) {
        FlowTrace trace = new FlowTrace(map.getString("EVENT_ID"), map.getDate("EVENT_TIME"));

        trace.setId(map.getLong("ID"));
        trace.setOrderId(map.getString("ORDER_ID"));
        trace.setTraceId(map.getString("TRACE_ID"));
        trace.setFlowName(map.getString("FLOW_NAME"));
        trace.setVersion(map.getInteger("VERSION"));
        trace.setNode(map.getString("NODE"));
        trace.setStartTime(map.getDate("START_TIME"));
        trace.setUpdateTime(map.getDate("UPDATE_TIME"));
        trace.setNextRetryTime(map.getDate("NEXT_RETRY_TIME"));
        trace.setRetryTimes(map.getInteger("RETRY_TIMES"));
        trace.getRetryMeta().setNodeName(map.getString("NODE_NAME"));
        trace.getRetryMeta().setTarget(map.getString("TARGET"));
        trace.getRetryMeta().setRetryMax(map.getInteger("RETRY_MAX"));
        trace.getRetryMeta().setRetryFailType(RetryFailTypeEnum.getByCode(map.getString("RETRY_FAIL_TYPE")));
        trace.getRetryMeta().setRetryMaxLimitNode(map.getString("RETRY_MAX_LIMIT_NODE"));

        trace.getRetryMeta().setRetreatUnit(map.getInteger("RETREAT_UNIT"));
        trace.getRetryMeta().setRetreatType(RetryRetreatTypeEnum.getByCode(map.getString("RETREAT_TYPE")));
        trace.getRetryMeta().setRetreatTimeUnit(RetryRetreatTimeUnitEnum.getByCode(map.getString("RETREAT_TIME_UNIT")));

        trace.getRetryMeta().executionTarget(map.getString("EXECUTION_TARGET"));
        trace.getRetryMeta().attachment(map.getString("ATTACHMENT"));

        return trace;
    }


    public void updateMeta(Object target, Map<String, Object> attachment) {
        attachment.remove(FlowContext.FLOW_TRACE);
        clean();

        getRetryMeta().setExecutionTarget(target);
        getRetryMeta().getAttachment().putAll(attachment);
    }

    public void clean() {
        getRetryMeta().getAttachment().remove(FlowContext.FLOW_TRACE);
    }

    public void retreatNextTime() {
        if (null != getRetryMeta().getRetreatType()) {
            getRetryMeta().getRetreatType().retreatNextTime(this);
        }
    }

    private boolean retryMaxLimitTo() {
        return StringUtils.isNotEmpty(getRetryMeta().getRetryMaxLimitNode());
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public RetryMeta getRetryMeta() {
        return retryMeta;
    }

    public void setRetryMeta(RetryMeta retryMeta) {
        this.retryMeta = retryMeta;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isFromSchedule() {
        return fromSchedule;
    }

    public void setFromSchedule(boolean fromSchedule) {
        this.fromSchedule = fromSchedule;
    }

    @Override
    public String toString() {

        return ToString.toString(this);
    }
}
