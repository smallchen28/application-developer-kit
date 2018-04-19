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

import com.global.adk.flow.annotation.Executor;
import com.global.adk.flow.engine.Execution;
import com.yjf.common.lang.result.StandardResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/10/16
 */
@Component
public class CommittedSettlement {
    @Autowired
    private DepositInstructionDomainService depositInstructionDomainService;
    @Autowired
    private TransportService transportService;

    @Executor
    public void execute(Execution execution) {
        DepositInstruction depositInstruction = execution.getTarget();
        ApplySettlementOrder applySettlementOrder = depositInstructionDomainService.createApplySettlementOrder(depositInstruction);
        StandardResultInfo rs = transportService.transport(applySettlementOrder);
        execution.addAttribute("call-settlement-rs", rs);
    }


}
