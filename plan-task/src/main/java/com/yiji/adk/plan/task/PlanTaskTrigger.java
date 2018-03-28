/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.yiji.adk.plan.task;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6
 * @version 1.0.0
 * @see
 * @history
 */
public interface PlanTaskTrigger {
	
	String getTaskName();
	
	Timestamp nextDate(Date now);
}
