package com.yiji.adk.plan.task.runnable;

import java.sql.Timestamp;

import com.yiji.adk.plan.task.PlanTaskAdmin;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;

import com.alibaba.fastjson.JSON;
import com.yiji.adk.plan.task.PlanTaskEngine;
import com.yiji.adk.plan.task.PlanTaskScheduler;
import com.yiji.adk.plan.task.module.PlanTask;
import com.yiji.adk.plan.task.module.PlanTaskStatement;
import com.yiji.adk.plan.task.module.SchedulerRepository;
import com.yiji.adk.plan.task.statement.ActionStatementDelegate;
import com.yiji.adk.plan.task.statement.ExecutorResult;
import com.yiji.adk.plan.task.statement.ExecutorStatus;

/**
 * 计划任务执行器
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年1月16日
 * @version 1.0.0
 * @see
 */
public class PlanExecutor implements Runnable {
	
	private Logger logger = LoggerFactory.getLogger(PlanExecutor.class);
	
	private PlanTaskEngine engine;
	
	private SchedulerRepository schedulerRepository;
	
	private PlanTaskScheduler scheduler;
	
	public PlanExecutor(PlanTaskEngine engine, PlanTaskScheduler scheduler) {
		this.engine = engine;
		this.scheduler = scheduler;
		this.schedulerRepository = engine.getSchedulerRepository();
	}
	
	@Override
	public void run() {
		engine.tx(new PlanTaskAdmin.TransactionCallback() {
			@Override
			public void run() {
				try {
					doRun();
				} catch (CannotAcquireLockException e) {
					//并发情况下，不能获取锁是正常的。
					if (logger.isInfoEnabled()) {
						logger.info("执行任务({})获取锁失败(并发执行)", scheduler.getPlanTaskPrototype().getTaskName());
					}
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error("执行计划任务({})过程中出现错误....", scheduler.getPlanTaskPrototype().getTaskName(), e);
					}
				}
			}
		});
	}
	
	public void doRun() {
		
		//抓取计划任务状态为INIT、PROCESSING、ERROR的实例。
		PlanTask planTask = schedulerRepository.active(scheduler.getPlanTaskPrototype().getTaskName());
		//- 任务不为空继续执行，为空则结束。
		if (planTask != null) {
			if (logger.isInfoEnabled()) {
				logger.info("激活计划任务->PlanTask[taskName={},taskExpiration={},taskExp={},maxCount={},exeContext={}]",
					planTask.getTaskName(), planTask.getTaskExpiration(), planTask.getTaskExp(),
					planTask.getExecMaxCount(), planTask.getExeContext());
			}
			
			//- 过期处理，过期则变更为Fail并存储，这里不能变更最后执行时间，因为并没执行。
			if (processExpiration(planTask)) {
				//- 构建ActionStatement委托对象（状态为空、初始化、出错状态下可以执行）
				ActionStatementDelegate actionStatementDelegate = new ActionStatementDelegate(engine, planTask);
				
				PlanTaskStatement statement = actionStatementDelegate.getCurrentStatement();
				
				//- 检查计划任务是否可以执行，完成的
				if (processValid(planTask, statement)) {
					//开始执行任务
					long begin = System.currentTimeMillis();
					PlanTaskStatement currentStatement = actionStatementDelegate.getCurrentStatement();
					
					ExecutorResult result = actionStatementDelegate.execute();
					
					//- 处理执行结果
					if (logger.isInfoEnabled()) {
						logger.info("计划任务({})已执行，完成条目:{}，处理结果:{},耗时：{}", planTask.getTaskName(),
							currentStatement.getTaskStateName(), result, System.currentTimeMillis() - begin);
					}
					
					//处理出错
					processResult(planTask, result, statement);
				}
			}
			
			//try-finally语法在这里会有事物问题，所以就这么着吧。
			schedulerRepository.restore(planTask);
			
		}
		
	}
	
	private void processResult(PlanTask planTask, ExecutorResult result, PlanTaskStatement statement) {
		//- 1. 执行条目
		Timestamp endTime = (result.getExecutorStatus() == ExecutorStatus.SUCCESS || result.getExecutorStatus() == ExecutorStatus.FAIL) ? engine
			.getSchedulerRepository().currentTimestamp() : null;
		statement.setEndTime(endTime);
		statement.setExecCount(statement.getExecCount() + 1);
		statement.setExecInfo(result.getExecInfo());
		statement.setExecStatus(result.getExecutorStatus().toString());
		schedulerRepository.restore(planTask, statement);
		
		//- 2. 计划任务
		planTask.setCurrentStatName(statement.getTaskStateName());
		planTask.setExeContext(JSON.toJSONString(result.getExeContext()));
		planTask.setExeCount(planTask.getExeCount() + 1);
		planTask.setExecMemo(result.getExecInfo());
		planTask.setExecStartTime(statement.getStartTIme());
		Timestamp now = engine.getSchedulerRepository().currentTimestamp();
		planTask.setExecLastTime(now);
		
		//- 3.处理状态
		//指定结果为ERROR、FAIL以及系统报错指定为ERROR，直接赋值即可，除此以外单独判读是否为处理中
		if (result.getExecutorStatus() == ExecutorStatus.ERROR || result.getExecutorStatus() == ExecutorStatus.FAIL) {
			planTask.setExecStatus(result.getExecutorStatus()); // 执行statement出错了，可以是人为指定也可能是系统报错。
		} else {
			planTask.setExecStatus(ExecutorStatus.SUCCESS);
			for (PlanTaskStatement stat : planTask.getStatements()) {
				if (!StringUtils.equals(stat.getExecStatus(), ExecutorStatus.SUCCESS.toString())) {
					planTask.setExecStatus(ExecutorStatus.PROCESSING);
					break;
				}
			}
			
			if (planTask.getExecStatus() == ExecutorStatus.SUCCESS) {
				planTask.setExecMemo(String.format(
					"计划任务执行成功,taskName=%s,execMemo=%s,rawAddTime=%s,start=%s,execLastTime=%s,nextTime=%s",
					planTask.getTaskName(), planTask.getExecMemo(), planTask.getRawAddTime(),
					planTask.getExecStartTime(), planTask.getExecLastTime(), planTask.getExecNextTime()));
				
				planTask.setExecNextTime(scheduler.getTrigger().nextDate(now));
			}
			
		}
		
	}
	
	private boolean processExpiration(PlanTask planTask) {
		
		long expiration = planTask.getTaskExpiration();
		long rawAddTime = planTask.getRawAddTime().getTime();
		long now = schedulerRepository.currentTimestamp().getTime();
		// 过期之后变更为Fail
		if (expiration > 0 && now - rawAddTime < expiration) {
			//- 更新不能执行的计划任务
			if (logger.isInfoEnabled()) {
				logger.info("执行计划({})已过期,expiration={},now={},create={}", planTask.getTaskName(), expiration, now,
					rawAddTime);
			}
			planTask.setExecMemo(String.format("执行计划已过期,expiration=%s,now=%s,create=%s", expiration, now, rawAddTime));
			planTask.setExecStatus(ExecutorStatus.FAIL);
			return false;
		}
		return true;
	}
	
	private boolean processValid(PlanTask planTask, PlanTaskStatement currentStatement) {
		
		int maxCount = planTask.getExecMaxCount();
		boolean repeatFlag = planTask.isRepeatFlag();
		
		//- 为空则意味着全部执行完成，已经没有符合条件的statement了，那么则更新状态为success
		//- 这部分逻辑不应当出现,测试覆盖不到……
		if (currentStatement == null) {
			//- 更新已经完成的计划任务
			if (logger.isInfoEnabled()) {
				logger.info("计划任务({})不存在当前执行条目,置为执行结果：执行完成、执行状态：FAIL", planTask.getTaskName());
			}
			planTask.setExecMemo("计划任务执行完成");
			planTask.setExecStatus(ExecutorStatus.FAIL);
			return false;
		}
		
		int statExeCount = currentStatement.getExecCount();
		//- 执行次数超过一次并且不允许重复执行的，或者执行次数达到最大次数，并且存在最大执行次数的。
		if ((statExeCount > 0 && !repeatFlag) || (maxCount > 0 && statExeCount > 0 && maxCount <= statExeCount)) {
			if (logger.isInfoEnabled()) {
				logger.info("计划任务({})超过最大运行次数,maxCount={},exeCount={},repeatFlag={}", planTask.getTaskName(), maxCount,
					statExeCount, repeatFlag);
			}
			planTask.setExecMemo(String.format("计划任务超过最大运行次数,maxCount=%s,exeCount=%s,repeatFlag=%s", maxCount,
				statExeCount, repeatFlag));
			planTask.setExecStatus(ExecutorStatus.FAIL);
			return false;
		}
		return true;
	}
}
