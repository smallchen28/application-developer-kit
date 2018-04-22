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

import com.global.adk.flow.annotation.Listen;
import com.global.adk.flow.engine.Execution;
import com.global.common.lang.result.Status;
import org.springframework.stereotype.Component;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/10/16
 */
@Component
public class StatusOnChangeListener {

    @Listen(eventExpression = "committed_settlement_fail", priority = Integer.MAX_VALUE)
    public void settlementFail(Execution execution, String eventName) {
        onChange(execution,"SettlementFail",Status.FAIL);
    }


    @Listen(eventExpression = "committed_settlement_processing", priority = Integer.MAX_VALUE)
    public void settlementSuspend(Execution execution, String eventName) {
        onChange(execution,"SettlementSuspend",null);

    }
    @Listen(eventExpression = "committed_settlement_processing", priority = Integer.MAX_VALUE)
    public void accountingSuccess(Execution execution, String eventName) {
        onChange(execution,"AccountSuccess",Status.SUCCESS);
    }

    @Listen(eventExpression = "committed_settlement_processing", priority = Integer.MAX_VALUE)
    public void accountingUnkown(Execution execution, String eventName) {
        onChange(execution,"AccountUnkown",null);
    }


    private void onChange(Execution execution , String state,Status status){
        DepositInstruction depositInstruction = execution.getTarget();
        depositInstruction.setState(state);
        if(status != null){
            depositInstruction.setStatus(status);
        }
        depositInstruction.update();
    }

}