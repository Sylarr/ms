package com.sinosoft.ms.beans;

/**
 * 短信发送器。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
@SuppressWarnings("serial")
public class ShortMsgSender implements Bean {

	private String senderId;
	private String senderName;
	private String account;
	private String password;
	private String url;
	private String smsType;
	private String subCode;
	
	public String getPrimaryKey() {
		return senderId;
	}

	public String getTableName() {
		return "MS_SHORTMSGSENDER";
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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
}
