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

package com.global.adk.biz.executor.test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.biz.executor.ActivityExecutorContainer;
import com.global.boot.core.Apps;
import com.global.boot.core.YijiBootApplication;
import com.yjf.common.id.GID;
import com.yjf.common.lang.context.OperationContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/23 下午下午3:53<br>
 * @see
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ExecutorYijiBootTest.class})
@YijiBootApplication(sysName = "yiji-adk-test", heraEnable = true, httpPort = 0)
@Configuration
public class ExecutorYijiBootTest {

    private static final String PROFILE = "stest";

    @Autowired
    private ActivityExecutorContainer activityExecutorContainer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Reference(group = "test", version = "1.0")
    private DubboServiceAPITestService dubboServiceAPITestService;

    @Reference(group = "test", version = "1.0")
    //	@Autowired
    private DubboServiceAPITestService1 dubboServiceAPITestService1;

    @Bean
    public DBPlugin dbPlugin(SqlSessionTemplate sqlSessionTemplate) {
        return new DBpluginImpl(sqlSessionTemplate);
    }

    //	@Bean
    //	public ExceptionMonitor exceptionMonitor() {
    //		StandardExceptionMonitor exceptionMonitor = new StandardExceptionMonitor("LOL", "SecurityCore");
    //		List<ExceptionProcessorConfig> configs = Lists.newArrayList();
    //		exceptionMonitor.setConfigs(configs);
    //
    //		ExceptionProcessorConfig kitNestConfig = new ExceptionProcessorConfig(1,
    //			"throwable instanceof com.yiji.adk.common.exception.KitNestException", new KitNestExceptionProcessor());
    //
    //		ExceptionProcessorConfig unkownConfig = new ExceptionProcessorConfig(2,
    //				"1=1", new UnkownExceptionProcessor());
    //
    //		configs.add(kitNestConfig);
    //		configs.add(unkownConfig);
    //
    //		return exceptionMonitor;
    //	}
    //
    //	@Bean
    //	public RegistryCodeVerify registryCodeVerify(){
    //		return new NullRegistryCodeVerify();
    //	}

    @BeforeClass
    public static void initEnv() {
        Apps.setProfileIfNotExists(PROFILE);

        //+++++++++++++++++++++++dubbo provider生成测试用++++++++++++++++++++++
        System.setProperty("yiji.dubbo.provider.port", "20091");
        System.setProperty("yiji.dubbo.owner", "adk");
        System.setProperty("yiji.adk.executor.providerMetaPath", "com/yiji/adk/biz/executor/test");
        //+++++++++++++++++++++++dubbo provider生成测试用++++++++++++++++++++++
    }

    @Test
    public void testDubboExport() throws InterruptedException {
        //防止服务延迟注册
        Thread.sleep(10 * 1000L);

        org.springframework.util.Assert.notNull(dubboServiceAPITestService);
        org.springframework.util.Assert.notNull(dubboServiceAPITestService1);

//        TestApiOrder order = new TestApiOrder();
//        order.setGid(GID.newGID());
//        order.setMerchOrderNo("00000000001111111111");
//        order.setPartnerId("00000000001111111111");
//        order.setReqId("00000000001111111111");
//
//        SubResult result = dubboServiceAPITestService.testService(order, new OperationContext());
//        Assert.assertNotNull(result);
//        Assert.assertEquals("0000", result.getCode());
//
//        result = dubboServiceAPITestService1.testService(order, new OperationContext());
//        Assert.assertNotNull(result);
//        Assert.assertEquals("0000", result.getCode());
    }

    @Test
    public void testTX() {
        jdbcTemplate.update("delete from app_kit_order_test");

        Order order = new Order("jixianenv", 11L);

        OrderResult result = activityExecutorContainer.accept(order, "order_test",
                null/*,
                OperationContext.build("app_kit", "app_kit", OperationContext.OperationTypeEnum.test, OperationContext.BusinessTypeEnum.TEST)*/);

        Assert.assertTrue(result != null);
        Assert.assertTrue(result.isSuccess());

        long identity = result.getIdentity();

        Assert.assertTrue(identity > 0);

        BeanPropertyRowMapper<CustomerOrder> rowMapper = BeanPropertyRowMapper.newInstance(CustomerOrder.class);
        CustomerOrder co = jdbcTemplate.queryForObject("select * from app_kit_order_test where identity = ?", rowMapper,
                identity);

        Assert.assertNotNull(co);
        Assert.assertTrue(order.getProductName().equals(co.getProductName()));
        Assert.assertTrue(order.getPrice() == co.getPrice());
    }

    @Test
    public void testTX2() {
        jdbcTemplate.update("delete from app_kit_order_test");

        Order order = new Order("jixianenv", 11L);

        OrderResult result = activityExecutorContainer.accept(order, "not_rollback_test", OperationContext.build(
                "app_kit", "app_kit", OperationContext.OperationTypeEnum.test, OperationContext.BusinessTypeEnum.TEST));

        Assert.assertTrue(result != null);
        Assert.assertTrue(result.isProcessing());

        long identity = result.getIdentity();

        Assert.assertTrue(identity > 0);

        BeanPropertyRowMapper<CustomerOrder> rowMapper = BeanPropertyRowMapper.newInstance(CustomerOrder.class);
        CustomerOrder co = jdbcTemplate.queryForObject("select * from app_kit_order_test where identity = ?", rowMapper,
                identity);

        Assert.assertNotNull(co);
        Assert.assertTrue(order.getProductName().equals(co.getProductName()));
        Assert.assertTrue(order.getPrice() == co.getPrice());
    }

    @Test
    public void noneEntityTest() {
        Order order = new Order("jixianenv", 11L);
        OrderResult result = activityExecutorContainer.accept(order, "none_entity_invoke_service",
                OperationContext.build("app_kit", "app_kit", OperationContext.OperationTypeEnum.test,
                        OperationContext.BusinessTypeEnum.TEST));
        Assert.assertTrue(result != null);
    }

    //异步测试，名字懒得改了。
    @Test
    public void tradeTest() throws InterruptedException {

        jdbcTemplate.update("delete from app_kit_order_test");

        Order order = new Order("jixianenv", 11L);
        TradeResult result = activityExecutorContainer.accept(order, "trade", OperationContext.build("app_kit",
                "app_kit", OperationContext.OperationTypeEnum.test, OperationContext.BusinessTypeEnum.TEST));

        Assert.assertTrue(result != null);

        Assert.assertTrue(result.isSuccess());

        TimeUnit.SECONDS.sleep(5);

        int counter = jdbcTemplate.queryForObject("select count(*) from app_kit_order_test ", Integer.class);

        Assert.assertTrue(counter == 1);
    }

    @Test
    public void serilaTest() {
        //-FIXME 这个不好测了。
    }

    @Test
    public void innerGenericTest() {
        activityExecutorContainer.accept(null, "test_result_invoke_service", OperationContext.build("app_kit",
                "app_kit", OperationContext.OperationTypeEnum.test, OperationContext.BusinessTypeEnum.TEST));
    }

}
