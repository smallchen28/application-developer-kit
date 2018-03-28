/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 22:15 创建
 *
 */
package com.yiji.adk.executor.axon;

import com.yiji.adk.common.boot.CommonConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
@ConfigurationProperties(prefix = CommonConfiguration.ADK_BIZ_EXECUTOR_AXON_PREFIX)
public class ExecutorAxonProperties {
}
