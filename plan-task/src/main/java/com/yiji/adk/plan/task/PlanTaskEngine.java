/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.yiji.adk.plan.task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;

import com.yiji.adk.common.exception.PlanTaskException;
import com.yiji.adk.plan.task.module.PlanTask;
import com.yiji.adk.plan.task.module.PlanTaskStatement;
import com.yiji.adk.plan.task.module.SchedulerRepository;
import com.yiji.adk.plan.task.runnable.PlanExecutor;
import com.yiji.adk.plan.task.statement.ActionStatement;
import com.yjf.common.concurrent.MonitoredThreadPool;

/**
 * 执行任务引擎
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6
 * @version 1.0.0
 * @see
 */
public class PlanTaskEngine {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Set<PlanTaskScheduler> planTaskSchedulers = new HashSet<>();
	
	private SchedulerRepository schedulerRepository;
	
	private MonitoredThreadPool executor;
	
	private Map<String, ActionStatement> statementCache = new ConcurrentHashMap<>();

	private PlanTaskAdmin admin;

	public PlanTaskEngine(SchedulerRepository schedulerRepository, MonitoredThreadPool threadPool, PlanTaskAdmin admin) {
		
		this.schedulerRepository = schedulerRepository;
		
		this.executor = threadPool;

		this.admin = admin;
	}
	
	public void active() {
		
		try {
			for (PlanTaskScheduler scheduler : planTaskSchedulers) {
				if (match(scheduler)) {
					if (logger.isInfoEnabled()) {
						PlanTask planTask = scheduler.getPlanTaskPrototype();
						logger.info(
							"任务创建匹配成功->PlanTask[taskName={},taskExpiration={},taskExp={},maxCount={},exeContext={}]",
							planTask.getTaskName(), planTask.getTaskExpiration(), planTask.getTaskExp(),
							planTask.getExecMaxCount(), planTask.getExeContext());
					}
					schedulerRepository.store(scheduler);
				}
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("激活计划任务过程中出现错误....", e);
			}
		}
		
	}
	
	public void run() {
		
		for (PlanTaskScheduler scheduler : planTaskSchedulers) {
			executor.execute(new PlanExecutor(this, scheduler));
		}
	}
	
	private boolean match(PlanTaskScheduler scheduler) {
		
		try {
			String taskName = scheduler.getPlanTaskPrototype().getTaskName();
			
			//- 查询最后一条创建的计划任务
			PlanTask planTask = schedulerRepository.loadLastCerate(taskName);
			
			//- 最后创建时间
			Date lastCreateTime = planTask == null ? null : planTask.getRawAddTime();
			
			//- 当前时间
			Date now = schedulerRepository.currentTimestamp();
			
			//- 计算执行时间
			Date calculateExecutionTime = scheduler.getTriggerContext().getCalculateExecutionTime();
			
			if (calculateExecutionTime == null) {
				//- 不存在则计算
				calculateExecutionTime = scheduler.getTrigger().nextDate(now);
				scheduler.getTriggerContext().setCalculateExecutionTime(calculateExecutionTime);
			} else {
				//- 根据最后执行时间，决定是否更新计算时间
				if (lastCreateTime != null) {
					if (lastCreateTime.after(calculateExecutionTime) || lastCreateTime.equals(calculateExecutionTime)) {
						//- 最后执行时间晚于计算时间，则表示已经执行。
						calculateExecutionTime = scheduler.getTrigger().nextDate(now);
						scheduler.getTriggerContext().setCalculateExecutionTime(calculateExecutionTime);
					}
				}//else{ 为空表示只进行了计算，并没有执行 }
			}
			
			//- 无论何时计算结果为null时都应当直接返回false
			if (calculateExecutionTime == null)
				return false;
			
			//- now , lastCreateTime , calculateExecutionTime判断匹配
			if (lastCreateTime == null) {
				//-  没有完成过，那么满足即可执行 now >= calculate
				if (now.after(calculateExecutionTime) || now.equals(calculateExecutionTime)) {
					return true;
				}
			} else {
				//- 最后执行时间 <= 计算时间
				if (lastCreateTime.before(calculateExecutionTime)
					&& (now.after(calculateExecutionTime) || now.equals(calculateExecutionTime))) {
					return true;
				}
			}
			
			return false;
			
		} catch (CannotAcquireLockException e) {
			//并发情况下，不能获取锁是正常的。
			if (logger.isInfoEnabled()) {
				logger.info("创建任务({})匹配获取锁失败(并发创建)", scheduler.getPlanTaskPrototype().getTaskName());
			}
			return false;
		}
		
	}
	
	public void driver(PlanTask planTask) {
		
		Set<PlanTaskStatement> statements = planTask.getStatements();
		
		if (statements == null || statements.size() == 0) {
			throw new PlanTaskException(String.format("PlantaskStatements为空或长度为0...."));
		}
		
		for (Iterator<PlanTaskStatement> it = statements.iterator(); it.hasNext();) {
			
			PlanTaskStatement statement = it.next();
			
			String actionStatementType = statement.getStateClassType();
			
			try {
				
				Class<?> superType = Class.forName(actionStatementType);
				
				ActionStatement actionStatement = statementCache.get(superType.getName());
				
				if (actionStatement == null) {
					throw new PlanTaskException(
						String.format("ActionStatement Bean配置不存在或错误的statement类型＝> %s implements ActionStatement",
							actionStatementType));
				}
				
			} catch (ClassNotFoundException e) {
				throw new PlanTaskException(String.format("类加载失败,actionStatementType = %s", actionStatementType));
			}
		}
		
		PlanTaskScheduler planTaskScheduler = buildScheduler(planTask);
		if (planTaskSchedulers.contains(planTaskScheduler)) {
			planTaskSchedulers.remove(planTaskScheduler);
		}
		planTaskSchedulers.add(planTaskScheduler);
		if (logger.isInfoEnabled()) {
			logger.info("初始化完成->PlanTask[taskName={},taskExpiration={},taskExp={},maxCount={},exeContext={}]",
				planTask.getTaskName(), planTask.getTaskExpiration(), planTask.getTaskExp(),
				planTask.getExecMaxCount(), planTask.getExeContext());
		}
	}
	
	private PlanTaskScheduler buildScheduler(PlanTask planTask) {
		
		PlanTask.TaskType taskType = planTask.getTaskType();
		
		if (taskType == null) {
			throw new PlanTaskException("非法得任务触发器类型");
		}
		
		PlanTaskTrigger trigger = TaskTriggerFactory.code(taskType).newInstance(planTask);
		
		PlanTaskScheduler planTaskScheduler = new PlanTaskScheduler(planTask, trigger);
		
		return planTaskScheduler;
	}
	
	public void addStatementCache(ActionStatement actionStatement) {
		
		statementCache.put(actionStatement.getClass().getName(), actionStatement);
	}
	
	public SchedulerRepository getSchedulerRepository() {
		
		return schedulerRepository;
	}

	public ActionStatement getStatement(String key) {
		
		return statementCache.get(key);
	}

	public void tx(PlanTaskAdmin.TransactionCallback transactionCallback){
		admin.tx(transactionCallback);
	}
}
