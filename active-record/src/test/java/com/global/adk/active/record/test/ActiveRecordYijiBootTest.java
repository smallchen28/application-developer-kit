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

package com.global.adk.active.record.test;

import com.global.adk.active.record.DomainFactory;
import com.global.adk.active.record.module.DBPlugin;
import com.global.boot.core.Apps;
import com.global.boot.core.YijiBootApplication;
import org.junit.Assert;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ActiveRecordYijiBootTest.class })
@YijiBootApplication(sysName = "global-adk-test", heraEnable = false, httpPort = 0)
@Configuration
//@ImportResource({ "classpath*:spring/active-record.xml"})
public class ActiveRecordYijiBootTest {
	
	protected static final String PROFILE = "stest";
	
	static {
		Apps.setProfileIfNotExists(PROFILE);
	}
	
	@Bean
	public DBPlugin dbPlugin(SqlSessionTemplate sqlSessionTemplate) {
		return new DBpluginImpl(sqlSessionTemplate);
	}
	
	public static final String SEQ = "seq_app_kit_plan_task";
	
	@Autowired
	private DomainFactory domainFactory;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void insertTest() {
		
		jdbcTemplate.update("delete from app_kit_order_test");
		
		Order order = domainFactory.newInstance(Order.class, true);
		Assert.assertNotNull(order.domainFactory);
		
		order.setProductName("jixianenv");
		order.setPrice(11);
		order.generateBizNo(SEQ, false, "A001");
		order.generateIdentity(SEQ);

		System.out.println(order.insert());
		
		BeanPropertyRowMapper<Order> rowMapper = BeanPropertyRowMapper.newInstance(Order.class);
		Order rs = jdbcTemplate.queryForObject("select * from app_kit_order_test where identity = ?", rowMapper,
			order.getIdentity());
		Assert.assertNotNull(rs);
		Assert.assertTrue(order.getProductName().equals(rs.getProductName()));
		Assert.assertTrue(order.getPrice() == rs.getPrice());
		order.delete();
		long orderCount = jdbcTemplate.queryForObject("select count(*) from app_kit_order_test where identity = ?",
			new Object[] { order.getIdentity() }, Long.class);
		Assert.assertTrue(orderCount == 0);
		
	}
	
}
