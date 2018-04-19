/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 21:02 创建
 *
 */
package com.global.adk.common.exception;

import com.global.common.lang.result.Status;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class DefaultBizException extends BizException {

    public DefaultBizException() {
        super();
    }

    public DefaultBizException(String message) {
        super(message);
    }

    public DefaultBizException(Throwable throwable) {
        super(throwable);
    }

    public DefaultBizException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DefaultBizException(String message, Status status) {
        super(message, status);
    }

    public DefaultBizException(String message, Status status, Throwable throwable) {
        super(message, status, throwable);
    }

    public DefaultBizException(String message, String errorCode) {
        super(message, errorCode);
    }

    public DefaultBizException(String message, String errorCode, Throwable throwable) {
        super(message, errorCode, throwable);
    }

    public DefaultBizException(String message, String errorCode, Status status) {
        super(message, errorCode, status);
    }

    public DefaultBizException(String message, String errorCode, Status status, Throwable throwable) {
        super(message, errorCode, status, throwable);
    }

    @Override
    protected String defaultErrorCode() {
        return null;
    }
}
