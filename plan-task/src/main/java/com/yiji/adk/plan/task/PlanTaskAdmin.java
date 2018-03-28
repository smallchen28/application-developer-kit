/**
 * www.yiji.com Inc. Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.plan.task;

import com.yiji.adk.common.exception.PlanTaskException;
import com.yiji.adk.plan.task.module.PlanTask;
import com.yiji.adk.plan.task.module.PlanTaskTable;
import com.yiji.adk.plan.task.module.SchedulerRepository;
import com.yjf.common.concurrent.MonitoredThreadPool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import java.util.List;

/**
 * 计划任务管理器
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 *
 * @history hasuelee创建于2014年9月30日 下午12:42:24<br>
 */
public class PlanTaskAdmin {

	private JdbcTemplate jdbcTemplate ;

	private OracleSequenceMaxValueIncrementer incrementer;

	private String tableNamePrefix ;

	private MonitoredThreadPool threadPool;

	private PlanTaskEngine taskEngine;

	private PlanTaskTable planTaskTable;

	public PlanTaskAdmin(JdbcTemplate jdbcTemplate, OracleSequenceMaxValueIncrementer incrementer, String tableNamePre, MonitoredThreadPool threadPool) {
		this.jdbcTemplate = jdbcTemplate;
		this.incrementer = incrementer;
		this.tableNamePrefix = tableNamePre;
		this.threadPool = threadPool;
		this.taskEngine = new PlanTaskEngine(new SchedulerRepository(jdbcTemplate,incrementer,tableNamePrefix),threadPool,this);
	}

	public void initializer(PlanTaskTable planTaskTable) {

		if(jdbcTemplate == null){
			throw new PlanTaskException("计划任务管理器PlanTaskAdmin初始化失败，jdbcTemplate不可为空……");
		}

		if(incrementer == null){
			throw new PlanTaskException("计划任务管理器PlanTaskAdmin初始化失败，OracleSequenceMaxValueIncrementer不可为空……");
		}

		if(tableNamePrefix == null){
			throw new PlanTaskException("计划任务管理器PlanTaskAdmin初始化失败，数据库表前缀不可为空……");
		}

		if(threadPool == null){
			throw new PlanTaskException("计划任务管理器PlanTaskAdmin初始化失败，MonitoredThreadPool不可为空……");
		}

		this.planTaskTable = planTaskTable;

		List<PlanTask> tasks = planTaskTable.getTasks();
		
		for (PlanTask planTask : tasks) {
			taskEngine.driver(planTask);
		}
	}

	public void active() {
		tx(new TransactionCallback() {
			@Override
			public void run() {
				taskEngine.active();
			}
		});
	}
	
	public void run() {
		taskEngine.run();
	}

	protected void tx(TransactionCallback transactionCallback){
		transactionCallback.run();
	}

	public static interface TransactionCallback{
		void run();
	}

	public PlanTaskEngine getTaskEngine() {

		return taskEngine;
	}
	
	public PlanTaskTable getPlanTaskTable() {
		
		return planTaskTable;
	}
}
