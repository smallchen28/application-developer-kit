/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-24 20:14 创建
 *
 */
package com.global.adk.api.service;

import com.global.adk.api.order.FileNotifyOrder;
import com.global.adk.api.result.FileNotifyResult;

/**
 * 文件请求通知服务，请求响应通用
 * 
 * <p />
 * 场景：SystemA->FileCenter，SystemA将文件放到文件存储服务后，通知文件中心去获取文件并解析处理<br/>
 *
 * @author karott
 */
public interface FileNotifyService {
	
	/**
	 * 请求通知.
	 *
	 * @param order 通知订单
	 * @return 请求通知结果: <br />
	 * 1.返回SUCCESS，即业务检查通过，文件检查存在且正常，请求通知成功<br />
	 * 2.返回FAIL，请求通知失败
	 *
	 * <p/>
	 * comn_01_0100-ftp文件不存在;comn_00_0101-ftp文件非法;
	 *
	 */
	FileNotifyResult requestNotify(FileNotifyOrder order);
	
}
