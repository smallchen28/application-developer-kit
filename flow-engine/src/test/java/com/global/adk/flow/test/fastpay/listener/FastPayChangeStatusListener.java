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
package com.global.adk.flow.test.fastpay.listener;

import com.global.adk.flow.test.fastpay.Consumer;
import com.global.adk.flow.test.fastpay.Customer;
import com.global.adk.flow.annotation.Listen;
import com.global.adk.flow.engine.Execution;

import org.springframework.stereotype.Component;

import com.global.common.lang.result.Status;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-31 上午11:14<br>
 * @see
 * @since 1.0.0
 */
@Component
public class FastPayChangeStatusListener {

    private Logger logger = LoggerFactory.getLogger(FastPayChangeStatusListener.class);

    @Listen(eventExpression = "!error,!start_processor,!biz_over,!trade_finished",priority = Integer.MAX_VALUE)
    public void standard(Execution execution , String eventName){
        Customer customer = execution.getTarget();
        Consumer consumer = customer.getConsumer();
        logger.info("eventExpression = \"!error,!start_processor,!biz_over,!trade_finished\"流程发生状态变更……{}",consumer);
        consumer.setNode(eventName);
//        consumer.setStatus(Status.PROCESSING); 此步骤非必要。
    }

    @Listen(eventExpression = "error",priority = Integer.MAX_VALUE)
    public void error(Execution execution , String eventName){
        Customer customer = execution.getTarget();
        Consumer consumer = customer.getConsumer();
        consumer.setNode(eventName);
        consumer.setStatus(Status.FAIL);
        logger.info("流程失败勒……{}",consumer);
    }

    @Listen(eventExpression = "trade_finished",priority = Integer.MAX_VALUE)
    public void finished(Execution execution , String eventName){
        Customer customer = execution.getTarget();
        Consumer consumer = customer.getConsumer();
        consumer.setNode(eventName);
        consumer.setStatus(Status.SUCCESS);
        logger.info("流程结束勒，交易成功了……{}",consumer);
    }


}
