/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 15:14 创建
 *
 */
package com.yiji.adk.filefront.dal.mapper;

import com.google.common.collect.Maps;
import com.yiji.adk.filefront.dal.entity.IdentifyTerm;
import com.yiji.adk.filefront.dal.entity.RequestNotify;
import com.yiji.adk.filefront.dal.entity.ResponseNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * mybatis支持的数据实现。 <br/>
 * 存在问题：1、应用端需要额外配置mybatis扫描路径 2、sql类型差异导致需要大部分重复配置且麻杂杂的(无法很好利用通用sql部分).有时间改下用JdbcTemplate来重新怼过
 *
 * @author karott
 */
public class FileDbOperator {
	
	@Autowired
	private FileFrontSystemMapper fileFrontSystemMapper;
	@Autowired
	private RequestNotifyMapper requestNotifyMapper;
	@Autowired
	private ResponseNotifyMapper responseNotifyMapper;
	
	private String dialect;
	
	public FileDbOperator(String dialect) {
		this.dialect = dialect;
	}
	
	public long nextSeq() {
		return fileFrontSystemMapper.getNextSequence();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void storeRequestNotify(RequestNotify notify) {
		notify.buildConfirmDataStr();
		if (isOracle()) {
			requestNotifyMapper.insertOneOfOracle(notify);
		} else {
			requestNotifyMapper.insertOneOfMysql(notify);
		}
		
	}
	
	public void restoreRequestNotify(RequestNotify notify) {
		notify.buildConfirmDataStr();
		requestNotifyMapper.updateOne(notify);
	}
	
	public RequestNotify byReqId(String reqId, String tenant) {
		Map<String, String> params = Maps.newHashMap();
		params.put("reqId", reqId);
		params.put("tenant", tenant);
		
		return requestNotifyMapper.selectByReqId(params).buildConfirmData();
	}
	
	public RequestNotify byReqIdLocked(String reqId, String tenant) {
		Map<String, String> params = Maps.newHashMap();
		params.put("reqId", reqId);
		params.put("tenant", tenant);
		return requestNotifyMapper.selectByReqIdLocked(params).buildConfirmData();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void storeResponseNotify(ResponseNotify notify) {
		notify.buildConfirmDataStr();
		if (isOracle()) {
			responseNotifyMapper.insertOneOfOracle(notify);
		} else {
			responseNotifyMapper.insertOneOfMysql(notify);
		}
	}
	
	public void restoreResponseNotify(ResponseNotify notify) {
		notify.buildConfirmDataStr();
		responseNotifyMapper.updateOne(notify);
	}
	
	public ResponseNotify byRspId(String rspId, String tenant) {
		Map<String, String> params = Maps.newHashMap();
		params.put("rspId", rspId);
		params.put("tenant", tenant);
		return responseNotifyMapper.selectByRspId(params).buildConfirmData();
	}
	
	public ResponseNotify byRspIdLocked(String rspId, String tenant) {
		Map<String, String> params = Maps.newHashMap();
		params.put("rspId", rspId);
		params.put("tenant", tenant);
		return responseNotifyMapper.selectByRspIdLocked(params).buildConfirmData();
	}
	
	public List<IdentifyTerm> queryRequestRetryIds() {
		return requestNotifyMapper.selectRetryIds();
	}
	
	public List<IdentifyTerm> queryResponseRetryIds() {
		return responseNotifyMapper.selectRetryIds();
	}
	
	private boolean isOracle() {
		return "oracle".equals(dialect);
	}
	
	private boolean isMysql() {
		return "mysql".equals(dialect);
	}
	
}
