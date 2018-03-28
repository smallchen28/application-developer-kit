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

package com.yiji.adk.rules.drools;

import java.util.Observable;
import java.util.Observer;

/**
 * 规则命中观察者
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/25 下午下午6:20<br>
 * @see
 * @since 1.0.0
 */
public abstract class StatisticRuleObserver implements Observer {

    @Override
    public void update(Observable o, Object data) {
        if (o instanceof CompilerDynamicExecutor) {
            EvalCaptrueInfo evalCaptrueInfo = (EvalCaptrueInfo) data;
            watch(evalCaptrueInfo);
        }
    }

    public abstract void watch(EvalCaptrueInfo evalCaptrueInfo);

}
