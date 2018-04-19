/**
 * 
 */
package com.global.adk.api;

import com.global.common.lang.result.StandardResultInfo;

/**
 * @author Administrator
 *
 */
public class ExecuteResult extends StandardResultInfo{

private static final long serialVersionUID = -3859453106839100192L;
	
	/** "System" */
	private String source;
	
	/**  */
	private String verbose;
	
	public String getSource() {
		
		return source;
	}
	
	public void setSource(String source) {

		this.source = source;
	}
	
	public String getVerbose() {
		
		return verbose;
	}
	
	public void setVerbose(String verbose) {
		
		this.verbose = verbose;
	}
	
}
