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

package com.yiji.adk.flow.test.deposit;

import com.yiji.adk.common.exception.BizException;
import com.yiji.adk.flow.annotation.Executor;
import com.yiji.adk.flow.engine.Execution;
import org.springframework.stereotype.Component;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/10/16
 */
@Component
public class SettlementFail {

    @Executor
    public void execute(Execution execution){
        throw new BizException("清算处理失败","银行卡余额不足，请致电xxx"){
            @Override
            protected String defaultErrorCode() {
                return "pay_001_0004";
            }
        };
    }
}
