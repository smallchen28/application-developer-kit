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
package com.yiji.adk.flow.test.fastpay.listener;

import com.yiji.adk.flow.annotation.Listen;
import com.yiji.adk.flow.engine.Execution;
import com.yiji.adk.flow.engine.NodeExecution;
import com.yiji.adk.flow.module.Flow;
import org.springframework.stereotype.Component;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;

/**
 * 对状态
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-28 上午11:42<br>
 * @see
 * @since 1.0.0
 */
@Component
public class FastPayLogListener {

    private Logger logger = LoggerFactory.getLogger(FastPayLogListener.class);

    @Listen(eventExpression = "\\S+",priority = 1)
    public void before(Execution execution , String eventName){
        logger.info("------------------------------------"+execution.currentNodeExecution().currentNode().getName()+"---------------------------------------");
    }

    @Listen(eventExpression = "*" , priority = 2)
    public void log(Execution execution , String eventName){
        NodeExecution nodeExecution = execution.currentNodeExecution();
        Flow flow = execution.getCurrentFlow();
        logger.info("Flow={},Version={},执行节点{}完成,Decision={},耗时：{}", flow.getName(), flow.getVersion(),nodeExecution.currentNode().getName(),eventName,nodeExecution.getEndTime().getTime() - nodeExecution.getStartTime().getTime());
    }

    @Listen(eventExpression = "\\S+",priority = 3)
    public void after(Execution execution , String eventName){
        logger.info("-------------------------------------"+execution.currentNodeExecution().currentNode().getName()+"---------------------------------------");
    }

}
