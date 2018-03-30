package com.global.adk.plan.task.statement;

import com.alibaba.fastjson.JSON;
import com.global.adk.common.exception.PlanTaskException;
import com.global.adk.plan.task.PlanTaskEngine;
import com.global.adk.plan.task.module.PlanTask;
import com.global.adk.plan.task.module.PlanTaskStatement;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.*;

public class ActionStatementDelegate implements ActionStatement{
	
	private PlanTaskStatement currentStatement;
	
	private PlanTask planTask;
	
	private Timestamp currentTime;
	
	private PlanTaskEngine engine;

	public ActionStatementDelegate(PlanTaskEngine engine, PlanTask planTask) {
		
		this.engine = engine;

		this.planTask = planTask;
		
		this.currentTime = engine.getSchedulerRepository().currentTimestamp();
		
		initializer(planTask);
	}
	
	@SuppressWarnings("unchecked")
	private void initializer(PlanTask planTask) {
		
		for (Iterator<PlanTaskStatement> it = planTask.getStatements().iterator(); it.hasNext();) {
			
			PlanTaskStatement statement = it.next();
			
			Timestamp endTime = statement.getEndTime();
			
			ExecutorStatus executorStatus = ExecutorStatus.valueOf(statement.getExecStatus());
			
			//- 没有结束，并且状态为出错和初始化，那么则表示需要执行
			if (currentStatement == null && endTime == null
				&& (executorStatus == ExecutorStatus.ERROR || executorStatus == ExecutorStatus.INIT || executorStatus == ExecutorStatus.PROCESSING)) {
				
				if (executorStatus == null) {
					throw new PlanTaskException(String.format("%s执行状态检测为空", statement.getExecStatus()));
				}
				this.currentStatement = statement;
				break;
			}
		}
		
	}
	
	@Override
	public ExecutorResult execute(ActionContext actionContext) {

		ExecutorResult result = null;

		try {
			if (currentStatement != null) {
				ActionStatement actionStatement = engine.getStatement(currentStatement.getStateClassType());

				if (actionStatement == null) {
					throw new PlanTaskException(String.format("计划任务%s容器bean配置不存在 statement beanName=%s",
							actionContext.getTaskName(), currentStatement.getStateClassType()));
				}

				//-开始时间
				if (currentStatement.getStartTIme() == null) {
					currentStatement.setStartTIme(engine.getSchedulerRepository().currentTimestamp());
				}

				result = actionStatement.execute(actionContext);

			}
		} catch (Exception e) {
			result = new ExecutorResult();
			result.setExecutorStatus(ExecutorStatus.ERROR);

			//- 获取堆栈信息
			StringWriter out = null;
			PrintWriter printWriter = null;
			try {
				out = new StringWriter();
				printWriter = new PrintWriter(out, true);
				e.printStackTrace(printWriter);
				String info = out.toString();
				if (info.length() > 512) {
					info = info.substring(0, 512);
				}
				result.setExecInfo(info);

			} finally {
				if (printWriter != null) {
					printWriter.close();
				}
			}
		}

		return result;
	}


	public ExecutorResult execute() {

		ActionContext actionContext = new ActionContext();

		String taskName = planTask.getTaskName();

		int exeCount = planTask.getExeCount();

		Timestamp startTIme = planTask.getExecStartTime();

		long elapsed = currentTime.getTime() - planTask.getRawAddTime().getTime();

		actionContext.setTaskName(taskName);
		actionContext.setExeCount(exeCount);
		actionContext.setStartTime(startTIme);
		actionContext.setElapsed(elapsed);
		actionContext.setExeContext((Map<String, String>) JSON.parse(planTask.getExeContext()));

		ExecutorResult result = execute(actionContext);

		//- 为空时则直接认为非法调用，予以报错。
		if (result == null || result.getExecutorStatus() == null) {
			throw new PlanTaskException(String.format("执行计划%s当前执行条目%s返回结果为空或者状态为空", planTask.getTaskName(),
					currentStatement.getTaskStateName()));
		}

		result.setExeContext(actionContext.getExeContext());

		return result;
	}
	
	public PlanTaskStatement getCurrentStatement() {
		
		return currentStatement;
	}
	
	public PlanTask getPlanTask() {
		
		return planTask;
	}

}
