/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88"   .   "88
 *                            (|  -_-  |)
 *                            O\  =   /O
 *                         ____/`---'\____
 *                       .'  \\|        |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -   /// |   |
 *                     | \_|  ''\--- /''   |   |
 *                     \  .-\__  `-`   ___/-. /
 *                   ___`. .'  /--.--\   `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/    - ` : | |
 *               \  \ `-.   \_ __\ /__ _/ .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *
 *  www.yiji.com Inc.
 *  Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.monitor;

import com.global.adk.biz.executor.ServiceContext;
import com.global.adk.common.Constants;
import com.global.adk.common.exception.SuspendException;
import com.global.adk.common.exception.SystemException;
import com.yjf.common.lang.result.StandardResultInfo;
import com.yjf.common.lang.result.Status;

/**
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月25日 下午6:06:33<br>
 */
public class KitNestExceptionProcessor extends ExceptionProcessorBase {
	
	@Override
	public void doProceed(Throwable throwable, ServiceContext<?, ?> serviceContext, ExceptionProcessorChain chain) {

		StandardResultInfo result = serviceContext.result();
		
		if (throwable instanceof SystemException) {
			SystemException ser = (SystemException) throwable;
			if (logger().isErrorEnabled()) {
				if (ser instanceof SuspendException) {
					logger().error("服务挂起->{}[param={},status={},message={},source={},systemName={},errorCode={}]",
						serviceContext.getInvokeElement(), serviceContext.getParameter(), ser.getStatus(),
						throwable.getMessage(), getSystemName(), ser.getErrorCode());
				} else {
					logger().error("服务出错->{}[param={},status={},message={},systemName={},errorCode={}]",
							serviceContext.getInvokeElement(), serviceContext.getParameter(), ser.getStatus(),
							throwable.getMessage(), getSystemName(), ser.getErrorCode());
				}
			}
			if (result != null) {
				ser.transport(result);
			}
		} else {
			if (logger().isErrorEnabled()) {
				logger().error("服务出现内部错误->{}[param={},message={}]", serviceContext.getInvokeElement(),
					serviceContext.getParameter(), throwable.getMessage());
			}
			if (result != null) {
				result.setStatus(Status.FAIL);
				result.setDescription(throwable.getMessage());
				result.setCode(Constants.ERROR_CODE_NEST);
			}
		}
		
		if (result == null) {
			logger().error("服务->{}严重错误-> 出错时，应答尚未构建完成.", serviceContext.getInvokeElement());
		}
		
	}
	
}
