/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-05-24 11:15 创建
 *
 */
package com.yiji.adk.biz.executor.test;

import com.yiji.adk.biz.executor.InvokeServiceAdapter;
import com.yiji.adk.biz.executor.ServiceContext;
import com.yiji.adk.biz.executor.annotation.Invoke;
import com.yiji.adk.common.exception.BizException;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
@Invoke(serviceName = "DubboServiceAPITestService.testService")
public class TestServiceInvoke extends InvokeServiceAdapter<TestApiOrder, SubResult> {

    @Override
    public void invoke(ServiceContext<TestApiOrder, SubResult> serviceContext) {

        throw new BizException("测试结果") {
            @Override
            protected String defaultErrorCode() {
                return "0000";
            }
        };
    }
}
