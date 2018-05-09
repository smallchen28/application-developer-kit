/*
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

package com.global.adk.flow.test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.global.adk.flow.test.fastpay.Consumer;
import com.global.adk.flow.test.fastpay.Customer;
import com.google.common.collect.Maps;
import com.global.adk.flow.FlowContext;
import com.global.boot.core.Apps;
import com.global.boot.core.YijiBootApplication;
import com.yjf.common.lang.result.Status;
import com.yjf.scheduler.service.api.ScheduleCallBackService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/24 下午下午2:02<br>
 * @see
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {FastPayYijiBootWithRetryMysqlTest.class})
@YijiBootApplication(sysName = "yiji-adk-test", heraEnable = false, httpPort = 0)
@Configuration
public class FastPayYijiBootWithRetryMysqlTest {

    private static final String FLOW_RETRY_FAIL_FAST = "fast_pay_retry_fail_fast";
    private static final String FLOW_RETRY_FAIL_FAST_MAX_LIMIT = "fast_pay_retry_fail_fast_max_limit";
    private static final String FLOW_RETRY_FAIL_BOMB = "fast_pay_retry_fail_bomb";
    private static final String FLOW_RETRY_FAIL_RETREAT = "fast_pay_retry_fail_retreat";

    private static final int FAST_PAY_STATE_MACHINE_VERSION = 1;

    private static final String PROFILE = "stest";

    @Autowired
    protected FlowContext flowContext;

    @Reference(group = "retry.group", version = "1.0")
    protected ScheduleCallBackService retryFlowProvider;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void initEnv() {
        Apps.setProfileIfNotExists(PROFILE);

        System.setProperty("retry", "true");
        System.setProperty("yiji.adk.flowengine.dialect", "mysql");
        System.setProperty("yiji.ds.url", "jdbc:mysql://192.168.45.84:3306/bxjf_adk_sdev");
        System.setProperty("yiji.ds.username", "adk");
        System.setProperty("yiji.ds.password", "adk@123");
    }

    @Test
    public void testFastPayWithRetryAndFailFast() {
        Customer customer = buildRetryCustomer();
        Map<String, Object> args = Maps.newHashMap();
        args.put(FlowContext.ORDER_ID, "001");

        //failFast
        clean();
        flowContext.start(FLOW_RETRY_FAIL_FAST, FAST_PAY_STATE_MACHINE_VERSION, customer, args);
        int traceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE", Integer.class);
        Assert.isTrue(0 == traceCount);

        //failFast+maxLimitTo
        clean();
        flowContext.start(FLOW_RETRY_FAIL_FAST_MAX_LIMIT, FAST_PAY_STATE_MACHINE_VERSION, customer, args);
        traceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE", Integer.class);
        Assert.isTrue(0 == traceCount);
    }

    @Test
    public void testFastPayWithRetryAndFailBomb() {
        Customer customer = buildRetryCustomer();
        Map<String, Object> args = Maps.newHashMap();
        args.put(FlowContext.ORDER_ID, "001");

        clean();
        flowContext.start(FLOW_RETRY_FAIL_BOMB, FAST_PAY_STATE_MACHINE_VERSION, customer, args);
        int traceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_BOMB", Integer.class);
        Assert.isTrue(1 == traceCount);
        int traceCountHis = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_HISTORY", Integer.class);
        Assert.isTrue(0 == traceCountHis);
    }

    @Test
    public void testFastPayWithRetryAndFailRetreat() {
        Customer customer = buildRetryCustomer();
        Map<String, Object> args = Maps.newHashMap();
        args.put(FlowContext.ORDER_ID, "001");

        clean();
        flowContext.start(FLOW_RETRY_FAIL_RETREAT, FAST_PAY_STATE_MACHINE_VERSION, customer, args);
        int traceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE", Integer.class);
        Assert.isTrue(1 == traceCount);
        int traceCountHis = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_HISTORY", Integer.class);
        Assert.isTrue(0 == traceCountHis);
    }

    @Test
    public void testRetryProvider() throws InterruptedException {
        Map<String, String> params = Maps.newHashMap();
        params.put("failType", "failBomb");
        params.put("orderBy", "updateTime");
        params.put("retryNodes", "retryValidate");

        //bomb <max
        clean();
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE_BOMB VALUES ('1', '001', '005v00000c0000000001703151914050001', 'fast_pay_retry_fail_bomb', '1', 'retryValidate', '1', '2017-03-15 19:14:05', 'retryValidate', '3', '', 'BizError', 'failBomb', NULL, NULL, NULL, '2017-03-15 19:14:05', '2017-03-15 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE_BOMB VALUES ('2', '002', '005v00000c0000000001703151914050002', 'fast_pay_retry_fail_bomb', '1', 'retryValidate', '1', '2017-03-15 19:14:05', 'retryValidate', '3', '', 'BizError', 'failBomb', NULL, NULL, NULL, '2017-03-15 19:14:05', '2017-03-14 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE_BOMB VALUES ('3', '003', '005v00000c0000000001703151914050003', 'fast_pay_retry_fail_bomb', '1', 'xx', '1', '2017-03-15 19:14:05', 'retryValidate', '2', '', 'BizError', 'failBomb', NULL, NULL, NULL, '2017-03-15 19:14:05', '2017-03-15 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        RpcContext.getContext().setAttachments(params);
        retryFlowProvider.justDoIT();

        TimeUnit.SECONDS.sleep(2);
        int traceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_BOMB", Integer.class);
        Assert.isTrue(3 == traceCount);
        int traceCountHis = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_HISTORY", Integer.class);
        Assert.isTrue(0 == traceCountHis);

        //bomb >=max
        clean();
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE_BOMB VALUES ('1', '001', '005v00000c0000000001703151914050001', 'fast_pay_retry_fail_bomb', '1', 'retryValidate', '2', '2017-03-15 19:14:05', 'retryValidate', '2', '', 'BizError', 'failBomb', NULL, NULL, NULL, '2017-03-15 19:14:05', '2017-03-15 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE_BOMB VALUES ('2', '002', '005v00000c0000000001703151914050002', 'fast_pay_retry_fail_bomb', '1', 'retryValidate', '2', '2017-03-15 19:14:05', 'retryValidate', '2', '', 'BizError', 'failBomb', NULL, NULL, NULL, '2017-03-15 19:14:05', '2017-03-14 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE_BOMB VALUES ('3', '003', '005v00000c0000000001703151914050003', 'fast_pay_retry_fail_bomb', '1', 'xx', '2', '2017-03-15 19:14:05', 'retryValidate', '2', '', 'BizError', 'failBomb', NULL, NULL, NULL, '2017-03-15 19:14:05', '2017-03-15 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        RpcContext.getContext().setAttachments(params);
        retryFlowProvider.justDoIT();

        TimeUnit.SECONDS.sleep(2);
        traceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_BOMB", Integer.class);
        Assert.isTrue(3 == traceCount);
        traceCountHis = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_HISTORY", Integer.class);
        Assert.isTrue(0 == traceCountHis);

        //retreat >=max
        params = Maps.newHashMap();
        params.put("failType", "failRetreat");
        params.put("orderBy", "startTime");
        params.put("retryNodes", "retryValidate");

        clean();
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE VALUES ('1', '001', '005v00000c0000000001703151914050001', 'fast_pay_retry_fail_retreat', '1', 'retryValidate', '2', '2010-03-15 19:14:05', 'retryValidate', '2', '', 'BizError', 'failRetreat', 1,'byDouble', 'hour',  '2017-03-15 19:14:05', '2017-03-15 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE VALUES ('2', '002', '005v00000c0000000001703151914050002', 'fast_pay_retry_fail_retreat', '1', 'retryValidate', '2', '2010-03-15 19:14:05', 'retryValidate', '2', '', 'BizError', 'failRetreat',1, 'byDouble','hour', '2017-03-15 19:14:05', '2017-03-14 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        jdbcTemplate.execute(
                "INSERT INTO FLOW_TRACE VALUES ('3', '003', '005v00000c0000000001703151914050003', 'fast_pay_retry_fail_retreat', '1', 'xx', '2', '2010-03-15 19:14:05', 'retryValidate', '2', '', 'BizError', 'failRetreat', 1,'byDouble', 'hour', '2017-03-15 19:14:05', '2017-03-15 19:14:05', '005v00000c0000000001703151914050000', '2017-03-15 19:14:05','{\"balance\":0,\"userName\":\"李根\",\"consumer\":{\"node\":\"error\",\"price\":90,\"productName\":\"极限恶女剑\",\"status\":\"FAIL\"}}','{\"orderId\":\"001\"}')");
        RpcContext.getContext().setAttachments(params);
        retryFlowProvider.justDoIT();

        TimeUnit.SECONDS.sleep(2);
        traceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE", Integer.class);
        Assert.isTrue(1 == traceCount);
        traceCountHis = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FLOW_TRACE_HISTORY", Integer.class);
        Assert.isTrue(2 == traceCountHis);
    }


    @Test
    public void testRetryProviderTemp() throws InterruptedException {
        Map<String, String> params = Maps.newHashMap();
        params.put("failType", "failBomb");
        params.put("orderBy", "updateTime");
        params.put("retryNodes", "retryValidate");
        RpcContext.getContext().setAttachments(params);

        retryFlowProvider.justDoIT();
        TimeUnit.SECONDS.sleep(2);
    }

    private Customer buildRetryCustomer() {
        Consumer consumer = new Consumer("极限恶女剑", 90);
        consumer.setStatus(Status.PROCESSING);
        consumer.setNode("initialize");
        Customer customer = new Customer("李根", consumer);
        Map<String, Object> args = Maps.newHashMap();
        args.put(FlowContext.ORDER_ID, "001");

        return customer;
    }

    private void clean() {
        jdbcTemplate.execute("delete from FLOW_TRACE");
        jdbcTemplate.execute("delete from FLOW_TRACE_BOMB");
        jdbcTemplate.execute("delete from FLOW_TRACE_HISTORY");
    }

}
