package com.yiji.adk.plan.task;

import com.yiji.adk.plan.task.module.PlanTask;
import com.yiji.adk.plan.task.module.PlanTaskStatement;
import com.yiji.adk.plan.task.statement.ExecutorResult;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;

public class AsyncExecutorContext {
	
	private ForkJoinTask<ExecutorResult> future;
	
	private PlanTaskStatement currentStatement;
	
	private PlanTaskScheduler scheduler;
	
	private PlanTask planTask;
	
	public AsyncExecutorContext(ForkJoinTask<ExecutorResult> future, PlanTaskScheduler scheduler, PlanTask plantask,
								PlanTaskStatement currentStatement) {
		
		this.future = future;
		
		this.scheduler = scheduler;
		
		this.currentStatement = currentStatement;
		
		this.planTask = plantask;
	}
	
	public void setFuture(ForkJoinTask<ExecutorResult> future) {
		
		this.future = future;
	}
	
	public Future<ExecutorResult> getFuture() {
		
		return future;
	}
	
	public PlanTaskStatement getCurrentStatement() {
		
		return currentStatement;
	}
	
	public void setCurrentStatement(PlanTaskStatement currentStatement) {
		
		this.currentStatement = currentStatement;
	}
	
	@Override
	public String toString() {
		
		return "AsyncExecutorContext [future=" + future + ", currentStatement=" + currentStatement + ", scheduler="
				+ scheduler + "]";
	}
	
	public PlanTaskScheduler getScheduler() {
		
		return scheduler;
	}
	
	public void setScheduler(PlanTaskScheduler scheduler) {
		
		this.scheduler = scheduler;
	}
	
	public PlanTask getPlanTask() {
		
		return planTask;
	}
	
	public void setPlanTask(PlanTask planTask) {
		
		this.planTask = planTask;
	}
	
}
