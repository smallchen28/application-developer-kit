/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/7-17:28 创建
 *
 */

package com.yiji.adk.flow.state;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class FlowHistoryTraceRepository extends DBDialectSupport {

    private static final String INSERT_MYSQL = "INSERT INTO FLOW_TRACE_HISTORY (" + FIELDS
            + ",END_TIME,ERROR) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_ORACLE = "INSERT INTO FLOW_TRACE_HISTORY (" + FIELDS
            + ",END_TIME,ERROR,ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public FlowHistoryTraceRepository(String dialect, JdbcTemplate jdbcTemplate) {
        super(dialect, jdbcTemplate);
    }

    public void store(FlowHistoryTrace flowHistoryTrace) {
        logger.debug("存储历史执行.traceId:{},orderId:{}", flowHistoryTrace.getTraceId(), flowHistoryTrace.getOrderId());

        List<Object> params = toSqlParams(flowHistoryTrace);
        params.add(flowHistoryTrace.getEndTime());
        params.add(flowHistoryTrace.getError());

        if (isMysql()) {
            insert(flowHistoryTrace, INSERT_MYSQL, params);
        }
        if (isOracle()) {
            insert(flowHistoryTrace, INSERT_ORACLE, params);
        }
    }

}
