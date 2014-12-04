package com.sinosoft.ms.beans;

/**
 * 任务邮件关联信息。
 * 
 * @author HanYan
 * @date 2014-10-20
 */
@SuppressWarnings("serial")
public class TaskMail implements Bean {

	private String taskId;
	private String senderId;
	private String mailId;
	
	public String getPrimaryKey() {
		return taskId + senderId + mailId;
	}

	public String getTableName() {
		return "MS_TASKMAIL";
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
}
