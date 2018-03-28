/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/7-17:16 创建
 *
 */

package com.yiji.adk.flow.state.retry;

import com.yiji.adk.flow.engine.Execution;

/**
 * 失败重试操作类型
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public enum RetryFailTypeEnum {

    FAIL_RETREAT("failRetreat", "失败后退避调度") {
        @Override
        public String execute(Execution execution) {
            return execution.getFlowTrace().retryRetreat();
        }

        @Override
        public void prepareNext(Execution execution) {
            //nothing
        }
    },

    FAIL_FAST("failFast", "失败后立即") {
        @Override
        public String execute(Execution execution) {
            return execution.getFlowTrace().retryFast();
        }

        @Override
        public void prepareNext(Execution execution) {
            //nothing
        }
    },

    FAIL_BOMB("failBomb", "失败后重试到爆") {
        @Override
        public String execute(Execution execution) {
            return execution.getFlowTrace().retryBomb();
        }

        @Override
        public void prepareNext(Execution execution) {
            //nothing
        }
    };

    /**
     * 枚举值
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String message;

    /**
     * 重试执行
     *
     * @param execution 执行对象
     * @return 执行结果
     */
    public abstract String execute(Execution execution);

    /**
     * 下次准备
     *
     * @param execution 执行对象
     */
    public abstract void prepareNext(Execution execution);

    /**
     * @param code    枚举值
     * @param message 枚举描述
     */
    private RetryFailTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return RetryType
     */
    public static RetryFailTypeEnum getByCode(String code) {
        for (RetryFailTypeEnum _enum : values()) {
            if (_enum.getCode().equals(code)) {
                return _enum;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举
     *
     * @return List<RetryType>
     */
    public static java.util.List<RetryFailTypeEnum> getAllEnum() {
        java.util.List<RetryFailTypeEnum> list = new java.util.ArrayList<RetryFailTypeEnum>(values().length);
        for (RetryFailTypeEnum _enum : values()) {
            list.add(_enum);
        }
        return list;
    }

    /**
     * 获取全部枚举值
     *
     * @return List<String>
     */
    public static java.util.List<String> getAllEnumCode() {
        java.util.List<String> list = new java.util.ArrayList<String>(values().length);
        for (RetryFailTypeEnum _enum : values()) {
            list.add(_enum.getCode());
        }
        return list;
    }

    /**
     * 通过code获取msg
     *
     * @param code 枚举值
     * @return
     */
    public static String getMsgByCode(String code) {
        if (code == null) {
            return null;
        }
        RetryFailTypeEnum _enum = getByCode(code);
        if (_enum == null) {
            return null;
        }
        return _enum.getMessage();
    }

    /**
     * 获取枚举code
     *
     * @param _enum
     * @return
     */
    public static String getCode(RetryFailTypeEnum _enum) {
        if (_enum == null) {
            return null;
        }
        return _enum.getCode();
    }

}
