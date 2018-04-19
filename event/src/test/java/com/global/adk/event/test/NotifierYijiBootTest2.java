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

package com.global.adk.event.test;

import com.global.common.concurrent.MonitoredThreadPool;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.global.boot.core.Apps;
import com.global.boot.core.YijiBootApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { NotifierYijiBootTest2.class })
@YijiBootApplication(sysName = "yiji-adk-test", heraEnable = false, httpPort = 0)
@Configuration
public class NotifierYijiBootTest2 {

	private static final String PROFILE = "xxx";

	@Autowired
	private NotifierCaller notifierCaller;

	@Bean(name="tp")
	public MonitoredThreadPool threadPool(){
		return new MonitoredThreadPool();
	}

	@Bean(name="tp2")
	public MonitoredThreadPool threadPool2(){
		return new MonitoredThreadPool();
	}


	@BeforeClass
	public static void initEnv() {
		Apps.setProfileIfNotExists(PROFILE);
	}

	@Test
	public void test() {
		notifierCaller.test1();
	}
	
}
