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

package com.global.adk.biz.executor.test;

import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.common.exception.DomainException;
import com.global.common.util.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;

import java.sql.Timestamp;

public class DBpluginImpl implements DBPlugin {
	
	private SqlSessionTemplate sqlSessionTemplate;
	
	public DBpluginImpl(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	@Override
	public Timestamp currentTimestamp() {
		
		return sqlSessionTemplate.selectOne("CURRENT_TIMESTAMP");
	}
	
	@Override
	public Long nextVar(String sequenceName) {
		if (StringUtils.isBlank(sequenceName)) {
			throw new DomainException("sequence_name为空");
		}
		return (Long) sqlSessionTemplate.selectOne("DEFAULT_SEQUENCE", sequenceName);
	}
	
	@Override
	public void lock(String policy, String module, String lockName) {
		
	}
	
	@Override
	public void lockNoWaite(String policy, String module, String lockName) {
		
	}
	
}
