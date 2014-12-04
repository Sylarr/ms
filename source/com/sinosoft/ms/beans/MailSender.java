package com.sinosoft.ms.beans;

/**
 * 邮件发送器（发件箱）。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
@SuppressWarnings("serial")
public class MailSender implements Bean {

	private String senderId;
	private String senderName;
	private String email;
	private String password;
	private String host;
	
	public String getPrimaryKey() {
		return senderId;
	}

	public String getTableName() {
		return "MS_MAILSENDER";
	}

	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
}
