/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-21 20:13 创建
 *
 */
package com.yiji.adk.filefront.test;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.yiji.adk.api.result.FileNotifyResult;
import com.yiji.adk.common.Constants;
import com.yiji.adk.filefront.exceptions.ResponseFileNotifyIdemException;
import com.yiji.adk.filefront.support.client.FileClient;
import com.yiji.adk.filefront.support.client.FileClientFactory;
import com.yiji.adk.filefront.support.function.ConvertFunction;
import com.yiji.adk.filefront.support.function.FunctionParser;
import com.yiji.boot.core.YijiBootApplication;
import com.yjf.common.lang.result.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author karott
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { FileFrontTest.class })
@YijiBootApplication(sysName = "yiji-adk-test", heraEnable = true, httpPort = 0)
@Configuration
public class FileFrontTest extends FilefrontTestBase {
	
	@Autowired
	private ConvertFunction testFunction;
	
	@Test
	public void testFunction() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("a", "b");
		Assert.isTrue("just for test".equals(functionFactory.eval("testFunction", params)));
		Assert.isTrue("just for test".equals(FunctionParser.parse(testFunction, params)));
	}
	
	@Test
	public void testFtp() throws IOException {
		FileClient fileClient = FileClientFactory.client("gydRepayment", "gyd");
		byte[] content = fileClient.downloadBytes("/", "ftptest.txt");
		Assert.notNull(content);
		Assert.isTrue(new String(content, "utf-8").equals("this is a ftp test file,do not delete!"));
		
		File file = fileClient.download("/", "ftptest.txt");
		Assert.notNull(file);
		Assert.isTrue(Files.toString(file, Charset.forName("utf-8")).equals("this is a ftp test file,do not delete!"));
	}
	
	@Test
	public void testRequestNotifySuccess() {
		clear();
		
		FileNotifyResult result = fileNotifyProvider.requestNotify(buildRequestNotifyOrder());
		
		Assert.notNull(result);
		Assert.isTrue(result.isProcessing());
		
		blockTime(10);
	}
	
	@Test
	public void testRequestNotifyIdem() {
		clear();
		jdbcTemplate.update(
			"INSERT INTO FILE_REQ_NOTIFY_LOG(ID, REQ_ID, RSP_ID, TENANT, BIZ_TYPE, FILE_SERVICE, STATUS, STATE, FILE_PATH, FILE_NAME, FILE_TIME, LOCAL_FILE_PATH, LOCAL_FILE_NAME, LOCAL_FILE_TIME, CONFIRM_DATA, DUBBO_GROUP, DUBBO_VERSION, PARTNER_ID, GID, MERCHANT_ORDER_NO, RAW_ADD_TIME, RAW_UPDATE_TIME) VALUES (31, '00000000000000000000', NULL, 'gyd', 'gydRepayment', 'FTP', 'processing', 'fileDownloaded', '/home/karott/appdata/ftp/ftpuser', 'testFile', TO_DATE('2016-09-08 15:27:30','YYYY-MM-DD HH24:MI:SS'), '/home/karott/appdata/ftp/download/testFile', 'testFile', TO_DATE('2016-09-08 15:27:31','YYYY-MM-DD HH24:MI:SS'), NULL, 'test-dubbo', '1.0', '00000000000000000000', '00000000000000000000000000000000000', '00000000000000000000', TO_DATE('2016-09-08 15:27:31','YYYY-MM-DD HH24:MI:SS'), NULL)");
			
		FileNotifyResult result = fileNotifyProvider.requestNotify(buildRequestNotifyOrder());
		
		Assert.notNull(result);
		Assert.isTrue(result.isProcessing());
		Assert.isTrue(result.getCode().equals(Constants.REQUEST_REPEATED));
		
		block();
	}
	
	@Test
	public void testRequestNotifySchedule() {
		clear();
		
		jdbcTemplate.update(
			"INSERT INTO FILE_REQ_NOTIFY_LOG(ID, REQ_ID, RSP_ID, TENANT, BIZ_TYPE, FILE_SERVICE, STATUS, STATE, FILE_PATH, FILE_NAME, FILE_TIME, LOCAL_FILE_PATH, LOCAL_FILE_NAME, LOCAL_FILE_TIME, CONFIRM_DATA, DUBBO_GROUP, DUBBO_VERSION, PARTNER_ID, GID, MERCHANT_ORDER_NO, RAW_ADD_TIME, RAW_UPDATE_TIME) VALUES (1, '00000000000000000000', NULL, 'gyd', 'gydRepayment', 'FTP', 'processing', 'init', '/test/download', 'testparse.txt', TO_DATE('2016-09-08 15:27:30','YYYY-MM-DD HH24:MI:SS'), '/test/download', 'testFile', TO_DATE('2016-09-08 15:27:31','YYYY-MM-DD HH24:MI:SS'), 'a=123', 'test-dubbo', '1.0', '00000000000000000000', '00000000000000000000000000000000000', '00000000000000000000', TO_DATE('2016-09-08 15:27:31','YYYY-MM-DD HH24:MI:SS'), NULL)");
		jdbcTemplate.update(
			"INSERT INTO FILE_REQ_NOTIFY_LOG(ID, REQ_ID, RSP_ID, TENANT, BIZ_TYPE, FILE_SERVICE, STATUS, STATE, FILE_PATH, FILE_NAME, FILE_TIME, LOCAL_FILE_PATH, LOCAL_FILE_NAME, LOCAL_FILE_TIME, CONFIRM_DATA, DUBBO_GROUP, DUBBO_VERSION, PARTNER_ID, GID, MERCHANT_ORDER_NO, RAW_ADD_TIME, RAW_UPDATE_TIME) VALUES (2, '00000000000000000001', NULL, 'gyd', 'gydRepayment', 'FTP', 'processing', 'fileDownloaded', '/test/download', 'testparse.txt', TO_DATE('2016-09-08 15:27:30','YYYY-MM-DD HH24:MI:SS'), '/home/karott/appdata/ftp/download/testFile', 'testFile', TO_DATE('2016-09-08 15:27:31','YYYY-MM-DD HH24:MI:SS'), 'a=123', 'test-dubbo', '1.0', '00000000000000000000', '00000000000000000000000000000000000', '00000000000000000000', TO_DATE('2016-09-08 15:27:31','YYYY-MM-DD HH24:MI:SS'), NULL)");
			
		requestFileNotifySchedule.justDoIT();
		
		blockTime(20);
		
		Long count = jdbcTemplate
			.queryForObject("select count(*) from FILE_REQ_NOTIFY_LOG WHERE STATE='fileDownloaded'", Long.class);
		Assert.isTrue(2 == count);
	}
	
	@Test
	public void testResponseNotifySuccess() {
		clear();
		
		fileEventBus.dispatchEvent(buildResponseNotifyEvent());
		block();
	}
	
	@Test
	public void testResponseNotifyIdem() {
		clear();
		
		jdbcTemplate.update(
			"INSERT INTO ACCOUNTANT.FILE_RSP_NOTIFY_LOG (ID, RSP_ID, REQ_ID, TENANT, BIZ_TYPE, FILE_SERVICE, STATUS, STATE, FILE_PATH, FILE_NAME, FILE_TIME, LOCAL_FILE_PATH, LOCAL_FILE_NAME, LOCAL_FILE_TIME, CONFIRM_DATA, DUBBO_GROUP, DUBBO_VERSION, PARTNER_ID, GID, MERCHANT_ORDER_NO, RAW_ADD_TIME, RAW_UPDATE_TIME) VALUES(89, '00000000000000000000', '00000000000000000000', 'gyd', 'gydRepayment', 'FTP', 'processing', 'init', NULL, NULL, TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'), '/home/karott/appdata/ftp', 'qqUpload.sh', TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'), NULL, 'test-dubbo', '1.0', '00000000000000000000', '00000000000000000000000000000000000', '00000000000000000000', TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'), TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'))");
			
		try {
			fileEventBus.dispatchEvent(buildResponseNotifyEvent());
		} catch (Exception e) {
			Assert.isTrue(e instanceof ResponseFileNotifyIdemException);
			ResponseFileNotifyIdemException re = (ResponseFileNotifyIdemException) e;
			Assert.isTrue(re.getStatus() == Status.PROCESSING);
			Assert.isTrue(re.getErrorCode().equals(Constants.REQUEST_REPEATED));
		}
		
	}
	
	@Test
	public void testResponseNotifySchedule() {
		clear();
		
		jdbcTemplate.update(
			"INSERT INTO ACCOUNTANT.FILE_RSP_NOTIFY_LOG (ID, RSP_ID, REQ_ID, TENANT, BIZ_TYPE, FILE_SERVICE, STATUS, STATE, FILE_PATH, FILE_NAME, FILE_TIME, LOCAL_FILE_PATH, LOCAL_FILE_NAME, LOCAL_FILE_TIME, CONFIRM_DATA, DUBBO_GROUP, DUBBO_VERSION, PARTNER_ID, GID, MERCHANT_ORDER_NO, RAW_ADD_TIME, RAW_UPDATE_TIME) VALUES(89, '00000000000000000000', '00000000000000000000', 'gyd', 'gydRepayment', 'FTP', 'processing', 'init', NULL, NULL, TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'), '/home/karott/appdata/ftp', 'testparse.txt', TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'), NULL, null, null, '00000000000000000000', '00000000000000000000000000000000000', '00000000000000000000', TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'), TO_DATE('2016-09-08 22:58:38','YYYY-MM-DD HH24:MI:SS'))");
			
		responseFileNotifySchedule.justDoIT();
		
		blockTime(10);
		
		Long count = jdbcTemplate.queryForObject("select count(*) from FILE_RSP_NOTIFY_LOG WHERE STATE='notifyIgnore'",
			Long.class);
		Assert.isTrue(1 == count);
	}
	
}
