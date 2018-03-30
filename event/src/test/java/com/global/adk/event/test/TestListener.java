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


import com.global.adk.event.Subscribe;

public class TestListener {
	
	@Subscribe(isAsync = false, priority = 2)
	public void action(String event) {
		System.out.println(event + " hello ...");
	}
	
	@Subscribe(isAsync = false, priority = 3)
	public void action2(String name) {
		System.out.println(name + " hello2 ...");
	}
	
	@Subscribe(isAsync = false, priority = 4)
	public void action3(String name) {
		System.out.println(name + " hello3 ...");
	}
	
	@Subscribe(isAsync = false, priority = 5)
	public void action5(int i) {
		System.out.println("int i = " + i);
	}
	
	@Subscribe(isAsync = false, priority = 6)
	public void action6(short s) {
		System.out.println("int s = " + s);
	}
	
	@Subscribe(isAsync = false, priority = 7)
	public void action7(long l) {
		System.out.println("int l = " + l);
	}
	
	@Subscribe(isAsync = false, priority = 8)
	public void action8(float f) {
		System.out.println("int f = " + f);
	}
	
	@Subscribe(isAsync = false, priority = 9)
	public void action9(double d) {
		System.out.println("int d = " + d);
	}
	
	@Subscribe(isAsync = true, priority = 8)
	public void action8(boolean b) {
		System.out.println("boolean b = " + b);
	}
	
	@Subscribe(isAsync = true, priority = 9)
	public void action9(char c) {
		System.out.println("c = " + c);
	}
	
	@Subscribe(isAsync = true, priority = 10)
	public void action10() {
		System.out.println("NoneEvent ....");
	}
	
	@Subscribe(isAsync = false, priority = 11)
	public void action11(int i, double j, String str) {
		System.out.println("i=" + i + ",j=" + j + ",str=" + str);
	}
	
}
