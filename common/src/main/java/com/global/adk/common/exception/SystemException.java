package com.global.adk.common.exception;

import com.global.adk.common.Constants;
import com.global.common.lang.result.StandardResultInfo;
import com.global.common.lang.result.Status;

/**
 * 应用系统异常定义
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月16日 上午10:52:42<br>
 */
public abstract class SystemException extends KitNestException {
	
	private static final long serialVersionUID = -4387907764416810657L;
	
	/** 执行状态 */
	private Status status;
	
	/** 错误码，可以是自定义，也可以是系统与系统之间的映射，也可以是透传码 ，需要注意，app_kit内部使用错误码将全系统覆盖使用 */
	private String errorCode;

	//base construct
	public SystemException() {
		this(null,null,null,null);
	}
	public SystemException(String message) {
		this(message,null,null,null);
	}
	public SystemException(Throwable throwable){
		this(null,null,null,throwable);
	}
	public SystemException(String message, Throwable throwable) {
		this(message,null,null,throwable);
	}

	//message、status
	public SystemException(String message, Status status) {
		this(message,null,status,null);
	}
	public SystemException(String message, Status status, Throwable throwable) {
		this(message,null,status,throwable);
	}

	//message、errorCode
	public SystemException(String message,String errorCode) {
		this(message,errorCode,null,null);
	}
	public SystemException(String message,String errorCode,Throwable throwable) {
		this(message,errorCode,null,throwable);
	}

	//message、errorCode、status
	public SystemException(String message,String errorCode,Status status) {
		this(message,errorCode,status,null);
	}
	public SystemException(String message,String errorCode,Status status, Throwable throwable) {
		super(message, throwable);
		this.status = status != null ? status : initStatus();
		this.errorCode = errorCode != null ? errorCode : defaultErrorCode();
		checkInit();
	}

	@SuppressWarnings("serial")
	private void checkInit() {
		
		if (status == null) {
			SystemException error = new SystemException("异常初始化失败，请求状态Status=null") {
				
				@Override
				protected String defaultErrorCode() {
					
					return Constants.ERROR_CODE_NEST;
				}
				
				@Override
				protected Status initStatus() {
					
					return Status.FAIL;
				}
			};
			throw error;
		}
	}
	
	public void transport(StandardResultInfo result) {
		result.setCode(errorCode);
		result.setDescription(getClass() == NestError.class ? "系统内部错误……" : getMessage());
		result.setStatus(status);
	}
	
	protected abstract Status initStatus();
	
	protected abstract String defaultErrorCode();
	
	public void setErrorCode(String errorCode) {
		
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		
		return errorCode;
	}
	
	public Status getStatus() {
		
		return status;
	}
}
