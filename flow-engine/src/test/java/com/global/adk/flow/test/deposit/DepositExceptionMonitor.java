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

package com.global.adk.flow.test.deposit;

import com.global.adk.flow.engine.ExceptionMonitor;
import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.Flow;
import com.global.adk.flow.module.FlowNode;
import org.springframework.stereotype.Component;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/10/16
 */
@Component
public class DepositExceptionMonitor implements ExceptionMonitor {
    @Override
    public void catcher(Flow flow, FlowNode node, Execution execution, Throwable throwable) {
        //如果使用ActivityExecutorContainer则此步骤非必要
    }
}
