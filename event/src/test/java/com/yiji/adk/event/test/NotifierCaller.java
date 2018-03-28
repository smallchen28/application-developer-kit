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

package com.yiji.adk.event.test;

import javax.annotation.PostConstruct;

import com.yiji.adk.event.NoneEvent;
import com.yiji.adk.event.NotifierBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifierCaller {
	
	@Autowired
	private NotifierBus notifierBus;
	
	@PostConstruct
	public void init() throws Exception {
		notifierBus.register(new TestListener());
		notifierBus.register(new TestListener());
//		notifierBus.register(new TestListener());
//		notifierBus.register(new TestListener());
//		notifierBus.register(new TestListener());
//		notifierBus.register(new TestListener());
	}
	
	public void test1() {
		notifierBus.dispatcher(null);
		notifierBus.dispatcher(12, null);
		notifierBus.dispatcher("ligen");
		notifierBus.dispatcher(12);
		notifierBus.dispatcher((short) 123);
		notifierBus.dispatcher(123L);
		notifierBus.dispatcher(1234F);
		notifierBus.dispatcher(12345D);
		notifierBus.dispatcher(false);
		notifierBus.dispatcher('A');
		notifierBus.dispatcher(NoneEvent.INSTANCE);
		notifierBus.dispatcher(1, 11D, "test");
	}
	
}
