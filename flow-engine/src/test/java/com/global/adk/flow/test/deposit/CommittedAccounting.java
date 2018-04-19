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

import com.global.adk.flow.annotation.*;
import com.global.adk.flow.annotation.Error;
import com.global.adk.flow.engine.Execution;
import com.yjf.common.lang.result.StandardResultInfo;
import com.yjf.common.lang.result.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/10/16
 */
@Component
public class CommittedAccounting {
    @Autowired
    private DepositInstructionDomainService depositInstructionDomainService;
    @Autowired
    private TransportService transportService;

    @Before
    public void before(Execution execution){
        //执行之前
    }

    @Executor
    public void execute(Execution execution){
        //执行业务代码
        AccountDepositOrder order = depositInstructionDomainService.createAccountingDepositOrder(execution.getTarget());
        StandardResultInfo rs = transportService.transport(order);
        execution.addAttribute("call-accounting-rs",rs);
    }

    @After
    public void after(Execution execution){
        //执行之后
    }

    @End
    public void end(Execution execution){
        //最终执行无论出现什么错误
    }

    @Error
    public String error(Execution execution){
        // 节点执行过程中出现错误
        return null;
    }

    @Condition
    public String condition(Execution execution){
        //节点执行完成后的流转条件
        return null;
    }




    @Condition
    public String decision(Execution execution){
        StandardResultInfo rs = execution.getAttribute("call-accounting-rs");
        return rs.getStatus() == Status.SUCCESS ? "":"";
    }
}
