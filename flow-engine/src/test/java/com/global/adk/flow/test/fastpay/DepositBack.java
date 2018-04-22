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

/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.test.fastpay;

import com.global.adk.flow.annotation.Before;
import com.global.adk.flow.annotation.Condition;
import com.global.adk.flow.engine.Execution;
import org.springframework.stereotype.Component;

import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-23 下午12:49<br>
 * @see
 * @since 1.0.0
 */
@Component
public class DepositBack {

    private Logger logger = LoggerFactory.getLogger(DepositBack.class);

    @Before
    public void changeState(Execution execution){
        logger.info("退货完成");
    }

    @Condition
    public String decision(Execution execution){
        return "biz_over";
    }

}
