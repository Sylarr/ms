package com.sinosoft.ms.dto;

/**
 * 调用远程服务时，返回的提示信息。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class ResponseMessage {
	
	private String responseCode;
	private String message;
	
	public ResponseMessage(String responseCode, String message) {
		super();
		this.responseCode = responseCode;
		this.message = message;
	}
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
