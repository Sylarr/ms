package com.sinosoft.ms.beans;

/**
 * 用户信息
 * @author HanYan
 * @date 2014-09-12
 */
@SuppressWarnings("serial")
public class PrpDuser implements Bean {
	
	private String userCode;
	private String userName;
	private String userEName;
	private String password;
	private String comCode;
	private String makeCom;
	private String accountCode;
	private String phone;
	private String email;
	private String validStatus;
	
	/**
	 * 返回映射的表名
	 */
	public String getTableName() {
		return "PRPDUSER";
	}
	
	/**
	 * 主键为userCode
	 */
	public String getPrimaryKey() {
		return userCode;
	}

	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEName() {
		return userEName;
	}
	public void setUserEName(String userEName) {
		this.userEName = userEName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getMakeCom() {
		return makeCom;
	}
	public void setMakeCom(String makeCom) {
		this.makeCom = makeCom;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getValidStatus() {
		return validStatus;
	}
	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}
}
