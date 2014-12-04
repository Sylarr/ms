package com.sinosoft.ms.beans;

/**
 * 邮件信息。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
@SuppressWarnings("serial")
public class MailInfo implements Bean {

	private String mailId;
	private String subject;
	private String recipientTo;
	private String recipientCc;
	private String from;
	private String html;
	private String content;
	
	public String getPrimaryKey() {
		return mailId;
	}

	public String getTableName() {
		return "MS_MAILINFO";
	}

	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRecipientTo() {
		return recipientTo;
	}
	public void setRecipientTo(String recipientTo) {
		this.recipientTo = recipientTo;
	}
	public String getRecipientCc() {
		return recipientCc;
	}
	public void setRecipientCc(String recipientCc) {
		this.recipientCc = recipientCc;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
