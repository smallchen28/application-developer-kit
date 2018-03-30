/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/6-19:26 创建
 *
 */

package com.global.adk.flow.state;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.global.adk.common.exception.FlowException;
import com.global.adk.flow.state.retry.RetryFailTypeEnum;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
@Repository
public class FlowTraceRepository extends DBDialectSupport {

    private static final String INSERT_MYSQL = "INSERT INTO FLOW_TRACE%s (" + FIELDS
            + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_ORACLE = "INSERT INTO FLOW_TRACE%s (" + FIELDS
            + ",ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE FLOW_TRACE%s SET RETRY_TIMES=?,NEXT_RETRY_TIME=?,EXECUTION_TARGET=?,ATTACHMENT=? WHERE TRACE_ID=?";
    private static final String DELETE = "DELETE FROM FLOW_TRACE%s WHERE TRACE_ID=?";
    private static final String LOCK = "SELECT TRACE_ID FROM FLOW_TRACE%s WHERE TRACE_ID=? FOR UPDATE";

    public FlowTraceRepository(String dialect, JdbcTemplate jdbcTemplate) {
        super(dialect, jdbcTemplate);
    }

    public boolean lock(FlowTrace flowTrace) {
        return forUpdate(flowTrace, LOCK, flowTrace.getTraceId());
    }

    public void store(FlowTrace flowTrace) {
        logger.debug("存储流程执行.traceId:{},orderId:{}", flowTrace.getTraceId(), flowTrace.getOrderId());

        if (isMysql()) {
            insert(flowTrace, INSERT_MYSQL, toSqlParams(flowTrace));
        }
        if (isOracle()) {
            insert(flowTrace, INSERT_ORACLE, toSqlParams(flowTrace));
        }
    }

    public void remove(FlowTrace flowTrace) {
        logger.debug("移除流程执行.,traceId:{},orderId:{}", flowTrace.getTraceId(), flowTrace.getOrderId());

        delete(flowTrace, DELETE, new Object[]{flowTrace.getTraceId()});
    }

    public void restore(FlowTrace flowTrace) {
        logger.debug("更新流程执行.traceId:{},orderId:{}", flowTrace.getTraceId(), flowTrace.getOrderId());

        update(flowTrace, UPDATE,
                new Object[]{flowTrace.getRetryTimes(), flowTrace.getNextRetryTime(), flowTrace.getRetryMeta().executionTargetString(), flowTrace.getRetryMeta().attachmentString(), flowTrace.getTraceId()});
    }

    public List<FlowTrace> listFlowTracesWithLock(RetryFailTypeEnum failType, List<String> nodes, String orderBy,
                                                  String sort, int batch) {
        StringBuilder sqlBuilder = new StringBuilder();

        if (isMysql()) {
            sqlBuilder.append("SELECT ID," + FIELDS + "FROM FLOW_TRACE%s");
            listSqlCondition(sqlBuilder, nodes, orderBy, sort);
            sqlBuilder.append(" LIMIT 0,").append(batch);
            sqlBuilder.append(" FOR UPDATE");

            return selectList(failType, sqlBuilder.toString());
        }

        if (isOracle()) {
            sqlBuilder.append("SELECT * FROM (");
            sqlBuilder.append(" SELECT T.*,ROWNUM RN");
            sqlBuilder.append(" FROM (SELECT ID," + FIELDS + "FROM FLOW_TRACE%s");
            listSqlCondition(sqlBuilder, nodes, orderBy, sort);
            sqlBuilder.append(") T");
            sqlBuilder.append(" WHERE ROWNUM <=" + batch + ")");
            sqlBuilder.append(" WHERE RN >=0");

            return selectList(failType, sqlBuilder.toString());
        }

        throw new FlowException("不可能的配置");
    }

    private void listSqlCondition(StringBuilder sqlBuilder, List<String> nodes, String orderBy, String sort) {
        if (null != nodes) {
            List<String> withQuoteList = Lists.newArrayList();
            nodes.stream().forEach(el -> withQuoteList.add("'" + el + "'"));

            sqlBuilder.append(" WHERE NODE IN (").append(Joiner.on(",").join(withQuoteList.iterator())).append(")");
        }
        sqlBuilder.append(" ORDER BY ").append(orderBy.toUpperCase());
        sqlBuilder.append(" ").append(sort.toUpperCase());
    }

}
