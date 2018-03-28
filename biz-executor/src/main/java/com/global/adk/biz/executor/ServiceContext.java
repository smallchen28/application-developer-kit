/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.biz.executor;

import com.yiji.adk.active.record.module.EntityObject;
import com.yiji.adk.biz.executor.regcode.RegistryCodeVerify;
import com.yjf.common.lang.context.OperationContext;
import com.yiji.adk.common.log.TraceLogFactory;
import com.yjf.common.lang.result.StandardResultInfo;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务上下文
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月16日 下午3:01:50<br>
 * @see
 * @since 1.0.0
 */
public class ServiceContext<PARAM, RESULT extends StandardResultInfo> implements Serializable {

    private static final long serialVersionUID = -7996169895294878828L;

    /**
     * 当前数据库时间戳 <code>currentTimestamp</code>
     */
    private Timestamp currentTimestamp;

    /**
     * 系统开始时间
     */
    private long begin;

    /**
     * 操作元素
     */
    private InvokeElement invokeElement;

    /**
     * 操作上下文
     */
    private OperationContext operationContext;

    /**
     * 请求参数 <code>param</code>
     */
    private PARAM param;

    /**
     * 业务相关聚合根,实体对象 <code>entityObject</code>
     */
    private EntityObject entityObject;
    /**
     * executor-axon使用
     */
    private Object aggregate;

    /**
     * 应答返回 <code>result</code>
     */
    private RESULT result;

    /**
     * 注册码验证
     */
    private RegistryCodeVerify registryCodeVerify;

    /**
     * 冗余字段，用于系统流转 <code>attribute</code>
     */
    private Map<String, Object> attributes = new HashMap<String, Object>();

    private Map<String, String> activeTrace = new HashMap<>();

    private Exception error;

    private Logger logger;

    /**
     * 构建一个<code>ServiceContext.java</code>
     */
    public ServiceContext(OperationContext context, PARAM param) {
        this.operationContext = context;
        this.param = param;
    }

    public Timestamp currentTimestamp() {

        return currentTimestamp;
    }

    public InvokeElement getInvokeElement() {

        return invokeElement;
    }

    public Logger getLogger() {
        if (logger == null) {
            logger = TraceLogFactory.getLogger(this.getInvokeElement().logName());
        }
        return logger;
    }

    public <T extends EntityObject> T convertEntity(String... ignore) {
        if (entityObject == null) {
            return null;
        }
        entityObject.convertFrom(param, ignore);
        return (T) entityObject;
    }

    public Object getAggregate() {
        return aggregate;
    }

    public void setAggregate(Object aggregate) {
        this.aggregate = aggregate;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public void setInvokeElement(InvokeElement invokeElement) {

        this.invokeElement = invokeElement;
    }

    public OperationContext operationContext() {

        return operationContext;
    }

    public PARAM getParameter() {

        return param;
    }

    public void setParameter(PARAM param) {

        this.param = param;
    }

    public EntityObject getEntityObject() {

        return entityObject;
    }

    public void setEntityObject(EntityObject entityObject) {

        this.entityObject = entityObject;
    }

    public RESULT result() {

        return result;
    }

    @SuppressWarnings("unchecked")
    public void setResult(StandardResultInfo result) {

        this.result = (RESULT) result;
    }

    public void setCurrentTimestamp(Timestamp currentTimestamp) {

        this.currentTimestamp = currentTimestamp;
    }

    /**
     * 新增属性设置方法
     *
     * @param key    key，非空
     * @param object 属性对象，非空
     */
    public void putAttribute(String key, Object object) {
        Assert.notNull(key);
        Assert.notNull(object);

        attributes.put(key, object);
    }

    public <T> T getAttribute(String key) {

        return (T) attributes.get(key);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Exception getError() {

        return error;
    }

    public void setError(Exception error) {

        this.error = error;
    }

    public Map<String, String> getActiveTrace() {
        return activeTrace;
    }

    public void addActiveTrace(String active, String trace) {
        activeTrace.put(active, trace);
    }

    public void setRegistryCodeVerify(RegistryCodeVerify registryCodeVerify) {

        this.registryCodeVerify = registryCodeVerify;
    }

    public RegistryCodeVerify getRegistryCodeVerify() {

        return registryCodeVerify;
    }
}
