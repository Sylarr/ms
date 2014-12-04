package com.sinosoft.ms.beans;

import java.sql.Timestamp;

/**
 * 用户登录日志信息，主键LogId为数据库自增字段。
 * 
 * @author HanYan
 * @date 2014-11-05
 */
@SuppressWarnings("serial")
public class UserLoginLog implements Bean {
	
	private Long logId;
	private String userCode;
	private String loginIp;
	private Timestamp loginTime;
	private String status;
	
	public UserLoginLog(){}
	
	public UserLoginLog(String userCode, String loginIp, String status) {
		this.userCode = userCode;
		this.loginIp = loginIp;
		this.status = status;
		this.loginTime = new Timestamp(System.currentTimeMillis());
	}
	
	public String getPrimaryKey() {
		return String.valueOf(logId);
	}
	public String getTableName() {
		return "MS_USERLOGINLOG";
	}
	
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
