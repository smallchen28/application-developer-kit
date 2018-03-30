/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 15:02 创建
 *
 */
package com.global.adk.filefront.dal.mapper;

import com.global.adk.filefront.dal.entity.IdentifyTerm;
import com.global.adk.filefront.dal.entity.ResponseNotify;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * mybatis支持的数据实现。 <br/>
 * 存在问题：1、应用端需要额外配置mybatis扫描路径 2、sql类型差异导致需要大部分重复配置且麻杂杂的(无法很好利用通用sql部分).有时间改下用JdbcTemplate来重新怼过
 *
 * @author karott
 */
public interface ResponseNotifyMapper {
	
	static final String SELECT_SQL = "SELECT ID,REQ_ID as reqId,RSP_ID as rspId,TENANT,BIZ_TYPE as bizType,FILE_SERVICE as fileService,STATUS,STATE,ERROR_CODE as errorCode,ERROR_MSG as errorMsg,"
										+ "FILE_PATH as filePath,FILE_NAME as fileName,FILE_TIME as fileTime,LOCAL_FILE_PATH as localFilePath,LOCAL_FILE_NAME as localFileName,"
										+ "LOCAL_FILE_TIME as localFileTime,CONFIRM_DATA as confirmDataStr,DUBBO_GROUP as dubboGroup,DUBBO_VERSION as dubboVersion,"
										+ "PARTNER_ID as partnerId,GID,MERCHANT_ORDER_NO as merchantOrderNo,"
										+ "RAW_ADD_TIME as rawAddTime,RAW_UPDATE_TIME as rawUpdateTime ";
										
	@Insert("INSERT INTO FILE_RSP_NOTIFY_LOG VALUES(#{id},#{reqId,jdbcType=VARCHAR},#{rspId},"
			+ "#{tenant,jdbcType=VARCHAR},#{bizType},#{fileService},#{status},#{state},#{errorCode,jdbcType=VARCHAR},#{errorMsg,jdbcType=VARCHAR},"
			+ "#{filePath,jdbcType=VARCHAR},#{fileName,jdbcType=VARCHAR},"
			+ "#{fileTime,jdbcType=TIMESTAMP},#{localFilePath},#{localFileName},#{localFileTime},#{confirmDataStr,jdbcType=VARCHAR},"
			+ "#{dubboGroup,jdbcType=VARCHAR},#{dubboVersion,jdbcType=VARCHAR},#{partnerId},#{gid},#{merchantOrderNo},"
			+ "#{rawAddTime},#{rawUpdateTime,jdbcType=TIMESTAMP})")
	void insertOneOfOracle(ResponseNotify notify);
	
	@Insert("INSERT INTO FILE_RSP_NOTIFY_LOG(REQ_ID,RSP_ID,TENANT,BIZ_TYPE,FILE_SERVICE,STATUS,STATE,ERROR_CODE,ERROR_MSG,FILE_PATH,FILE_NAME,FILE_TIME,"
			+ "LOCAL_FILE_PATH,LOCAL_FILE_NAME,"
			+ "LOCAL_FILE_TIME,CONFIRM_DATA,DUBBO_GROUP,DUBBO_VERSION,PARTNER_ID,GID,MERCHANT_ORDER_NO,RAW_ADD_TIME,RAW_UPDATE_TIME) VALUES(#{reqId,jdbcType=VARCHAR},#{rspId},"
			+ "#{tenant,jdbcType=VARCHAR},#{bizType},#{fileService},#{status},#{state},#{errorCode,jdbcType=VARCHAR},#{errorMsg,jdbcType=VARCHAR},#{filePath,jdbcType=VARCHAR},#{fileName,jdbcType=VARCHAR},"
			+ "#{fileTime},#{localFilePath},#{localFileName},#{localFileTime},#{confirmDataStr,jdbcType=VARCHAR},"
			+ "#{dubboGroup,jdbcType=VARCHAR},#{dubboVersion,jdbcType=VARCHAR},#{partnerId},#{gid},#{merchantOrderNo},"
			+ "#{rawAddTime},#{rawUpdateTime,jdbcType=TIMESTAMP})")
	void insertOneOfMysql(ResponseNotify notify);
	
	@Update("UPDATE FILE_RSP_NOTIFY_LOG SET status=#{status},state=#{state},error_code=#{errorCode,jdbcType=VARCHAR},error_msg=#{errorMsg,jdbcType=VARCHAR},"
			+ "file_path=#{filePath},file_name=#{fileName},file_time=#{fileTime},raw_update_time=#{rawUpdateTime} where rsp_id=#{rspId} and tenant=#{tenant}")
	void updateOne(ResponseNotify notify);
	
	@Select(SELECT_SQL + "FROM FILE_RSP_NOTIFY_LOG WHERE rsp_id=#{rspId} and tenant=#{tenant}")
	ResponseNotify selectByRspId(Map<String, String> params);
	
	@Select(SELECT_SQL + "FROM FILE_RSP_NOTIFY_LOG WHERE rsp_id=#{rspId} and tenant=#{tenant} for update")
	ResponseNotify selectByRspIdLocked(Map<String, String> params);
	
	@Select("SELECT RSP_ID as idempotency,TENANT,STATE FROM FILE_RSP_NOTIFY_LOG WHERE STATE IN ('init','fileUploaded') ORDER BY LOCAL_FILE_TIME DESC")
	List<IdentifyTerm> selectRetryIds();
}