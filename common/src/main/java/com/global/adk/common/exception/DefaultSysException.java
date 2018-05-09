/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 21:03 创建
 *
 */
package com.global.adk.common.exception;

import com.yjf.common.lang.result.Status;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class DefaultSysException extends SystemException {

    public DefaultSysException() {
        super();
    }

    public DefaultSysException(String message) {
        super(message);
    }

    public DefaultSysException(Throwable throwable) {
        super(throwable);
    }

    public DefaultSysException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DefaultSysException(String message, Status status) {
        super(message, status);
    }

    public DefaultSysException(String message, Status status, Throwable throwable) {
        super(message, status, throwable);
    }

    public DefaultSysException(String message, String errorCode) {
        super(message, errorCode);
    }

    public DefaultSysException(String message, String errorCode, Throwable throwable) {
        super(message, errorCode, throwable);
    }

    public DefaultSysException(String message, String errorCode, Status status) {
        super(message, errorCode, status);
    }

    public DefaultSysException(String message, String errorCode, Status status, Throwable throwable) {
        super(message, errorCode, status, throwable);
    }

    @Override
    protected Status initStatus() {
        return Status.FAIL;
    }

    @Override
    protected String defaultErrorCode() {
        return null;
    }
}
