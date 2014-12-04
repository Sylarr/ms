package com.sinosoft.ms.beans;

/**
 * 短信信息。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
@SuppressWarnings("serial")
public class ShortMsgInfo implements Bean {

	private String msgId;
	private String phoneNumber;
	private String content;
	private String constant;
	
	public String getPrimaryKey() {
		return msgId;
	}

	public String getTableName() {
		return "MS_SHORTMSGINFO";
	}

	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getConstant() {
		return constant;
	}
	public void setConstant(String constant) {
		this.constant = constant;
	}
}
