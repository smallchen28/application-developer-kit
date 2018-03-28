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


package com.yiji.adk.plan.task.test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Sets;
import com.yiji.adk.plan.task.PlanTaskAdmin;
import com.yiji.adk.plan.task.module.PlanTask;
import com.yiji.adk.plan.task.module.PlanTaskStatement;
import com.yiji.adk.plan.task.statement.ExecutorStatus;
import com.yiji.boot.core.Apps;
import com.yiji.boot.core.YijiBootApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { PlanTaskYijiBootTest.class })
@YijiBootApplication(sysName = "yiji-adk-test", heraEnable = false, httpPort = 0)
@Configuration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlanTaskYijiBootTest {
	
	private static final String PROFILE = "stest";
	
	@BeforeClass
	public static void initEnv() {
		Apps.setProfileIfNotExists(PROFILE);
	}
	
	@Autowired
	public PlanTaskAdmin admin;
	
	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Bean
	public ActionStatement1 actionStatement1() {
		return new ActionStatement1();
	}

	@Bean
	public ActionStatement2 actionStatement2() {
		return new ActionStatement2();
	}

	@Bean
	public ActionStatement3 actionStatement3() {
		return new ActionStatement3();
	}

	@Bean
	public ActionStatement4 actionStatement4() {
		return new ActionStatement4();
	}
	
	@Bean
	public ActionStatement5 actionStatement5() {
		return new ActionStatement5();
	}

	@Test
	public void test02Create() {

		//- 清除已有数据
		jdbcTemplate.update("delete from APP_kit_TASK_STATEMENT");
		jdbcTemplate.update("delete from APP_kit_PLAN_TASK");

		//- 准备数据
		PlanTask planTask = admin.getPlanTaskTable().getTasks().get(0);
		planTask.setTaskName("test");

		//- 替换为ActionStatement4和5
		TreeSet statements = Sets.newTreeSet();
		PlanTaskStatement statement4 = new PlanTaskStatement();
		PlanTaskStatement statement5 = new PlanTaskStatement();
		statements.add(statement4);
		statements.add(statement5);
		planTask.setStatements(statements);

		statement4.setExecPriority(1);
		statement4.setStateClassType(ActionStatement4.class.getName());
		statement4.setTaskStateName("测试4");

		statement5.setExecPriority(2);
		statement5.setStateClassType(ActionStatement5.class.getName());
		statement5.setTaskStateName("测试5");

		StringBuilder cron = new StringBuilder();

		Calendar createdTime = createdTime();
		cron.append(createdTime.get(Calendar.SECOND) + 3).append(" ")
				.append(createdTime.get(Calendar.MINUTE)).append(" ").append(createdTime.get(Calendar.HOUR_OF_DAY))
				.append(" ").append(createdTime.get(Calendar.DAY_OF_MONTH)).append(" ")
				.append(createdTime.get(Calendar.MONTH) + 1).append(" ").append(createdTime.get(Calendar.YEAR));

		planTask.setTaskExp(cron.toString());

		admin.getTaskEngine().driver(planTask);

		//- 激活创建循环两百次，每次间隔一秒，最后检查是否创建成功（任务计算时间为当前数据库时间的下一分钟）
		for (int i = 0, j = 10; i < j; i++) {
			admin.active();
			try {
				TimeUnit.SECONDS.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//- 数据检查
		//- 1. 必须存在两条任务，名叫daily-endprocess-inter
		BeanPropertyRowMapper<PlanTask> rowMapper = BeanPropertyRowMapper.newInstance(PlanTask.class);
		List<PlanTask> tasks = jdbcTemplate.query("select * from app_kit_plan_task", rowMapper);
		Assert.assertTrue(tasks.size() == 1 && tasks.get(0).getTaskName().equals("test"));

	}

	private Calendar createdTime() {
		Timestamp timestamp = jdbcTemplate.queryForObject(" select sysdate from dual", Timestamp.class);
		Calendar createdTime = new GregorianCalendar();
		createdTime.setTime(timestamp);
		//- 两笔需要创建的计划任务，间隔5秒，15秒等待循环检测。

		if (createdTime.get(Calendar.SECOND) + 5 > 60) {
			try {
				TimeUnit.SECONDS.sleep(7);
				return createdTime();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return createdTime;
	}

	@Test
	public void test03Run() {
		//循环六次，执行后查看结果，由于是异步执行，所以这里等待两秒
		for (int i = 0, j = 2; i < j; i++) {
			try {
				admin.run();
				TimeUnit.SECONDS.sleep(2L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//检查任务状态
		BeanPropertyRowMapper<PlanTask> rowMapper = BeanPropertyRowMapper.newInstance(PlanTask.class);
		List<PlanTask> tasks = jdbcTemplate.query("select task_identity as identity , exec_status from app_kit_plan_task", rowMapper);

		for (PlanTask task : tasks) {
			//整体任务成功
			Assert.assertEquals(task.getExecStatus(), ExecutorStatus.SUCCESS);

			List<PlanTaskStatement> statements = jdbcTemplate.query(
					"SELECT * FROM app_kit_task_statement WHERE TASK_IDENTITY = ?",
					BeanPropertyRowMapper.newInstance(PlanTaskStatement.class), task.getIdentity());

			Assert.assertTrue(statements.size() == 1);
			Assert.assertEquals(statements.get(0).getExecStatus() , ExecutorStatus.SUCCESS.toString());

		}
	}

}
