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
package com.yiji.adk.biz.executor.monitor;

import com.yiji.adk.biz.executor.InvokeElement;
import com.yiji.adk.biz.executor.ServiceContext;
import com.yiji.adk.common.Constants;
import com.yjf.common.lang.result.StandardResultInfo;
import com.yjf.common.lang.result.Status;

/**
 * 未知异常处理器
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月25日 下午6:06:33<br>
 */
public class UnkownExceptionProcessor extends ExceptionProcessorBase {
	
	@Override
	public void doProceed(Throwable throwable, ServiceContext<?, ?> serviceContext, ExceptionProcessorChain chain) {

		StandardResultInfo result = serviceContext.result();
		
		if (logger().isErrorEnabled()) {
			InvokeElement element = serviceContext.getInvokeElement() ;
			if(element == null){
				logger().error(throwable.getMessage());
			}else{
				logger().error("服务->{}出现未知错误[param={}]",element.getServiceName(), serviceContext.getParameter());
			}
		}
		
		if (result != null) {
			result.setStatus(Status.FAIL);
			result.setDescription(String.format("系统未知错误，请联系%s系统owner",getSystemName()));
			result.setCode(Constants.ERROR_CODE_UNKOWN);
		} else {
			if(logger().isErrorEnabled()){
				logger().error("服务->{}严重错误-> 出错时，应答尚未构建完成.", serviceContext.getInvokeElement());
			}
		}
	}
}
