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
package com.global.adk.biz.executor.regcode;

/**
 * 请求码
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月26日 下午5:54:47<br>
 */
public interface RegistryCodeVerify {
	boolean validate(String serviceName, Object parameter);
}
