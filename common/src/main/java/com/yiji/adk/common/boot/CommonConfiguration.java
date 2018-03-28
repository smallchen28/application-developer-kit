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

package com.yiji.adk.common.boot;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/20 下午下午7:21<br>
 * @see
 * @since 1.0.0
 */
public interface CommonConfiguration {
	
	/**
	 * 活动记录集
	 */
	String ADK_ACTIVE_RECORD_PREFIX = "yiji.adk.activerecord";
	String ADK_ACTIVE_RECORD = ADK_ACTIVE_RECORD_PREFIX + ".enable";
	
	/**
	 * 活动执行容器
	 */
	String ADK_BIZ_EXECUTOR_PREFIX = "yiji.adk.executor";
	String ADK_BIZ_EXECUTOR = ADK_BIZ_EXECUTOR_PREFIX + ".enable";

	/**
	 * 容器-axon
	 */
	String ADK_BIZ_EXECUTOR_AXON_PREFIX="yiji.adk.executor.axon";
	String ADK_BIZ_EXECUTOR_AXON =ADK_BIZ_EXECUTOR_AXON_PREFIX+".enable";
	
	/**
	 * 事件总线
	 */
	String ADK_EVENT_PREFIX = "yiji.adk.event";
	String ADK_EVENT = ADK_EVENT_PREFIX + ".enable";
	
	/**
	 * 流程引擎
	 */
	String ADK_FLOW_ENGINE_PREFIX = "yiji.adk.flowengine";
	String ADK_FLOW_ENGINE = ADK_FLOW_ENGINE_PREFIX + ".enable";
	
	/**
	 * 计划任务
	 */
	String ADK_PLAN_TASK_PREFIX = "yiji.adk.plantask";
	String ADK_PLAN_TASK = ADK_PLAN_TASK_PREFIX + ".enable";
	
	/**
	 * 规则引擎
	 */
	String ADK_RULE_PREFIX = "yiji.adk.rule";
	String ADK_RULE = ADK_RULE_PREFIX + ".enable";
	
	/**
	 * 环境支持
	 */
	String ADK_ENV_PREFIX = "yiji.adk.env";
	String ADK_ENV = ADK_ENV_PREFIX + ".enable";
	
	/**
	 * 文件前置
	 */
	String ADK_FILEFRONT_PREFIX = "yiji.adk.filefront";
	String ADK_FILEFRONT = ADK_FILEFRONT_PREFIX + ".enable";
}
