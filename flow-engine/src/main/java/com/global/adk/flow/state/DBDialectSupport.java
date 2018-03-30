/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-03-08 15:53 创建
 *
 */
package com.global.adk.flow.state;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.global.adk.flow.state.retry.RetryFailTypeEnum;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class DBDialectSupport {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String FIELDS = " ORDER_ID,TRACE_ID,FLOW_NAME,VERSION,NODE,RETRY_TIMES,NEXT_RETRY_TIME,"
            + "NODE_NAME,RETRY_MAX,RETRY_MAX_LIMIT_NODE,TARGET,RETRY_FAIL_TYPE,RETREAT_UNIT,RETREAT_TYPE,"
            + "RETREAT_TIME_UNIT,START_TIME,EVENT_ID,EVENT_TIME,EXECUTION_TARGET,ATTACHMENT ";
    private static final String NEW_ID = "SELECT SEQ_FLOWENGINE.nextVal FROM DUAL";

    private JdbcTemplate jdbcTemplate;
    private String dialect;

    public DBDialectSupport(String dialect, JdbcTemplate jdbcTemplate) {
        this.dialect = dialect;
        this.jdbcTemplate = jdbcTemplate;
    }

    protected boolean forUpdate(FlowTrace flowTrace, String sql, String traceId) {
        try {
            return null != jdbcTemplate.queryForObject(sql(flowTrace, sql), String.class, traceId);
        } catch (DataAccessException ae) {
            logger.error("存储层异常", flowTrace, ae);
            return false;
        } catch (Exception e) {
            logger.error("未知异常", flowTrace, e);
            return false;
        }
    }

    protected boolean insert(FlowTrace flowTrace, String sql, List<Object> params) {
        if (isOracle()) {
            long idSeq = newSequence();
            flowTrace.setId(idSeq);
            params.add(idSeq);
        }

        return update(flowTrace, sql, params.toArray());
    }

    protected boolean delete(FlowTrace flowTrace, String sql, Object[] args) {
        return update(flowTrace, sql, args);
    }

    protected boolean update(FlowTrace flowTrace, String sql, Object[] args) {
        try {
            int result = jdbcTemplate.update(sql(flowTrace, sql), new ArgumentPreparedStatementSetter(args));
            return result != 0;
        } catch (DataAccessException ae) {
            logger.error("存储层异常", flowTrace, ae);
            return false;
        } catch (Exception e) {
            logger.error("未知异常", flowTrace, e);
            return false;
        }
    }

    protected List<FlowTrace> selectList(RetryFailTypeEnum failType, String sql) {
        List<FlowTrace> traces = Lists.newArrayList();

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql(failType, sql));
        traces.addAll(resultList.stream().map(map -> FlowTrace.convertFromMap(new AdaptiveDataMap(map)))
                .collect(Collectors.toList()));

        return traces;
    }

    protected List<Object> toSqlParams(FlowTrace trace) {
        return Lists.newArrayList(trace.getOrderId(), trace.getTraceId(), trace.getFlowName(), trace.getVersion(),
                trace.getNode(), trace.getRetryTimes(), trace.getNextRetryTime(), trace.getRetryMeta().getNodeName(),
                trace.getRetryMeta().getRetryMax(), trace.getRetryMeta().getRetryMaxLimitNode(),
                trace.getRetryMeta().getTarget(), trace.getRetryMeta().getRetryFailType().getCode(),
                trace.getRetryMeta().getRetreatUnit(),
                null != trace.getRetryMeta().getRetreatType() ? trace.getRetryMeta().getRetreatType().getCode() : null,
                null != trace.getRetryMeta().getRetreatTimeUnit() ? trace.getRetryMeta().getRetreatTimeUnit().getCode()
                        : null,
                trace.getStartTime(), trace.getEventId(), trace.getEventTime(), trace.getRetryMeta().executionTargetString(), trace.getRetryMeta().attachmentString());
    }

    protected boolean isMysql() {
        return "mysql".equals(dialect);
    }

    protected boolean isOracle() {
        return "oracle".equals(dialect);
    }

    private long newSequence() {
        return jdbcTemplate.queryForObject(NEW_ID, Long.class);
    }

    private String sql(FlowTrace flowTrace, String baseSql) {
        if (flowTrace instanceof FlowHistoryTrace) {
            return baseSql;
        }

        return sql(flowTrace.getRetryMeta().getRetryFailType(), baseSql);
    }

    private String sql(RetryFailTypeEnum failType, String baseSql) {
        if (RetryFailTypeEnum.FAIL_BOMB == failType) {
            return String.format(baseSql, "_BOMB");
        }

        return String.format(baseSql, "");
    }

    public static class AdaptiveDataMap extends HashMap<String, Object> {
        private Map<String, Object> target = Maps.newHashMap();

        public AdaptiveDataMap(Map<String, Object> map) {
            this.target = map;
        }

        public String getString(String key) {
            Object result = target.get(key);

            if (null == result) {
                return null;
            }

            return (String) result;
        }

        public Integer getInteger(String key) {
            Object result = target.get(key);

            if (null == result) {
                return null;
            }
            return Integer.parseInt(result.toString());
        }

        public Long getLong(String key) {
            Object result = target.get(key);

            if (null == result) {
                return null;
            }

            return Long.parseLong(result.toString());
        }

        public Date getDate(String key) {
            Object result = target.get(key);

            if (null == result) {
                return null;
            }

            return (Date) result;
        }
    }
}
