/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.common.exception;

import com.yjf.common.util.StringUtils;

/**
 * 内部异常定义
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/18 下午下午4:38<br>
 * @see
 * @since 1.0.0
 */
public class KitNestException extends RuntimeException {

	private static final long serialVersionUID = 2486491445505536970L;

	public KitNestException() {
		
	}
	
	public KitNestException(String arg0, Throwable arg1) {
		
		super(arg0, arg1);
	}
	
	public KitNestException(String arg0) {
		
		super(arg0);
	}
	
	public KitNestException(Throwable arg0) {
		
		super(arg0);
	}
	
	@Override
	public String getMessage() {
		
		Throwable cause = getCause();
		String msg = super.getMessage();
		if (cause != null) {
			StringBuilder sb = new StringBuilder();
			if (this.getStackTrace() != null && this.getStackTrace().length > 0) {
				sb.append(this.getStackTrace()[0]).append("\t");
				if (StringUtils.isNotBlank(msg)) {
					sb.append(msg);
					sb.append("; ");
				}
				sb.append("内联异常信息：").append(cause);
			}
			
			return sb.toString();
		} else {
			return msg;
		}
	}

}