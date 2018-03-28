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

import com.yiji.adk.plan.task.CronTaskTrigger;
import com.yiji.adk.plan.task.PlanTaskAdmin;
import com.yiji.adk.plan.task.module.PlanTask;
import com.yiji.adk.plan.task.module.PlanTaskStatement;
import com.yiji.adk.plan.task.statement.ExecutorStatus;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

//整个用例执行时间较长，之后在jenkins上面去跑。
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/plantask.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlanTaskTest {
	
	@Autowired
	public PlanTaskAdmin admin;
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void beforeClass() {
	}
	
	@Before
	public void before() {
		
	}
	
	@After
	public void after() {
		
	}
	
	@Test
	public void test01CheckCronExp() {
		
		Calendar now = new GregorianCalendar();
		int month = now.get(Calendar.MONTH) + 1;
		
		Calendar d1 = new GregorianCalendar();
		d1.setTime(new CronTaskTrigger("* * * 1 " + month + " *", "test1").nextDate(jdbcTemplate.queryForObject(
			" select sysdate from dual", Timestamp.class)));
		
		Calendar d2 = new GregorianCalendar();
		d2.setTime(new CronTaskTrigger("* * * 3/19 " + month + " *", "test2").nextDate(jdbcTemplate.queryForObject(
			" select sysdate from dual", Timestamp.class)));
		
		Calendar d3 = new GregorianCalendar();
		d3.setTime(new CronTaskTrigger("* * * 4,5 " + month + " *", "test3").nextDate(jdbcTemplate.queryForObject(
			" select sysdate from dual", Timestamp.class)));
		
		Calendar d4 = new GregorianCalendar();
		d4.setTime(new CronTaskTrigger("* 2-10 * * " + month + " *", "test4").nextDate(jdbcTemplate.queryForObject(
			" select sysdate from dual", Timestamp.class)));
		
		Calendar d5 = new GregorianCalendar();
		d5.setTime(new CronTaskTrigger("* 2-10 * * " + month + " 2182", "test4").nextDate(jdbcTemplate.queryForObject(
			" select sysdate from dual", Timestamp.class)));
		
		Calendar d6 = new GregorianCalendar();
		d6.setTime(new CronTaskTrigger("1,2,3 2-10 * 2 " + month + " *", "test4").nextDate(jdbcTemplate.queryForObject(
			" select sysdate from dual", Timestamp.class)));
		

		Assert.assertTrue((d1.get(Calendar.MONTH) + 1 == month) && (d1.get(Calendar.DAY_OF_MONTH)) == 1
							&& (d1.get(Calendar.YEAR)) == now.get(Calendar.YEAR) + 1);

		Assert.assertTrue((d2.get(Calendar.DAY_OF_MONTH) == 3) || (d2.get(Calendar.DAY_OF_MONTH) == 22) );

		Assert.assertTrue((d3.get(Calendar.DAY_OF_MONTH) == 4) || (d3.get(Calendar.DAY_OF_MONTH) == 5) );

		Assert.assertTrue((d4.get(Calendar.YEAR) == now.get(Calendar.YEAR)) && (d4.get(Calendar.MONTH) == now.get(Calendar.MONTH))&&
				(d4.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH))  &&
				(d4.get(Calendar.MINUTE) >=2 && d4.get(Calendar.MINUTE) <= 10 ));

		Assert.assertTrue((d5.get(Calendar.YEAR) == 2182) && (d5.get(Calendar.MONTH) == now.get(Calendar.MONTH))&&
				(d5.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) &&
				(d5.get(Calendar.MINUTE) >=2 && d5.get(Calendar.MINUTE) <= 10 ));

		Assert.assertTrue((d6.get(Calendar.DAY_OF_MONTH) == 2) &&
				(d6.get(Calendar.MINUTE) >=2 && d6.get(Calendar.MINUTE) <= 10 ) &&
				(d6.get(Calendar.SECOND) >=1 && d6.get(Calendar.SECOND) <= 3 ));

	}
	
	@Test
	public void test02Create() {
		
		//- 清除已有数据
		jdbcTemplate.update("delete from APP_kit_TASK_STATEMENT");
		jdbcTemplate.update("delete from APP_kit_PLAN_TASK");
		
		//- 准备数据
		PlanTask planTask = admin.getPlanTaskTable().getTasks().get(0);
		
		StringBuilder cron = new StringBuilder();
		
		Calendar createdTime = createdTime();
		cron.append(createdTime.get(Calendar.SECOND) + "," + (createdTime.get(Calendar.SECOND) + 5)).append(" ")
			.append(createdTime.get(Calendar.MINUTE)).append(" ").append(createdTime.get(Calendar.HOUR_OF_DAY))
			.append(" ").append(createdTime.get(Calendar.DAY_OF_MONTH)).append(" ")
			.append(createdTime.get(Calendar.MONTH) + 1).append(" ").append(createdTime.get(Calendar.YEAR));
		
		planTask.setTaskExp(cron.toString());
		
		admin.getTaskEngine().driver(planTask);
		
		//- 激活创建循环两百次，每次间隔一秒，最后检查是否创建成功（任务计算时间为当前数据库时间的下一分钟）
		for (int i = 0, j = 15; i < j; i++) {
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
		Assert.assertTrue(tasks.size() == 2 && tasks.get(0).getTaskName().equals("daily-endprocess-inter"));
		
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
		for (int i = 0, j = 8; i < j; i++) {
			try {
				admin.run();
				TimeUnit.SECONDS.sleep(2L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//检查任务状态
		BeanPropertyRowMapper<PlanTask> rowMapper = BeanPropertyRowMapper.newInstance(PlanTask.class);
		List<PlanTask> tasks = jdbcTemplate.query(
			"select task_identity as identity , exec_status from app_kit_plan_task", rowMapper);
		for (PlanTask task : tasks) {
			//整体任务失败
			Assert.assertEquals(task.getExecStatus(), ExecutorStatus.FAIL);
			
			List<PlanTaskStatement> statements = jdbcTemplate.query(
				"SELECT * FROM app_kit_task_statement WHERE TASK_IDENTITY = ?",
				BeanPropertyRowMapper.newInstance(PlanTaskStatement.class), task.getIdentity());
			
			for (PlanTaskStatement statement : statements) {
				if (statement.getExecPriority() == 3) {
					Assert.assertEquals(statement.getExecStatus(), ExecutorStatus.FAIL.toString());
				} else {
					Assert.assertEquals(statement.getExecStatus(), ExecutorStatus.SUCCESS.toString());
				}
			}
			
		}
	}
	
}
