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
package com.yiji.adk.flow.test.fastpay;

import com.yiji.adk.flow.annotation.Before;
import com.yiji.adk.flow.annotation.Executor;
import com.yiji.adk.flow.engine.Execution;
import org.springframework.stereotype.Component;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-21 下午9:01<br>
 * @see
 * @since 1.0.0
 */
@Component
public class WaitNotify {

    private static final Logger logger = LoggerFactory.getLogger(WaitNotify.class);

    @Before
    public void before(Execution execution){
        logger.info("流程进入等待交易通知节点consumer.node={}", "wait_notify");
        logger.info("决策条件：金额100-150挂起;151-180成功;>180则处理失败");
    }

    @Executor
    public String execute(Execution execution){
        Customer customer = execution.getTarget();
        Consumer consumer = customer.getConsumer();
        logger.info("执行站内转账操作，执行金额={}", consumer.getPrice());
        if(consumer.getPrice() >= 100 && consumer.getPrice() < 150){
            logger.info("站内转账状态未知开始处理transfer_suspend进入事件处理。");
            return "transfer_unknow" ;
        } else if(consumer.getPrice() >= 151 && consumer.getPrice() < 180){
            logger.info("站内转账成功开始处理transfer_success进入事件处理。");
            return "transfer_success" ;
        } else{
            logger.info("站内转账失败开始处理transfer_fail进入事件处理。");
            return "transfer_fail";
        }
    }

}
