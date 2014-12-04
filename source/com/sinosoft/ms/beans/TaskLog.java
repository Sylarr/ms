package com.sinosoft.ms.beans;

import java.sql.Timestamp;

/**
 * 任务日志信息，主键LogId为数据库自增字段。
 * 
 * @author HanYan
 * @date 2014-11-03
 */
@SuppressWarnings("serial")
public class TaskLog implements Bean {
	
	private Long logId;
	private String taskId;
	private Timestamp executeTime;
	private String senderId;
	private String senderDesc;
	private String messageInfoId;
	private String messageInfoDesc;
	private String content;
	private String contentDesc;
	private String status;
	
	public String getPrimaryKey() {
		return String.valueOf(logId);
	}
	public String getTableName() {
		return "MS_TASKLOG";
	}
	
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Timestamp getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(Timestamp executeTime) {
		this.executeTime = executeTime;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderDesc() {
		return senderDesc;
	}
	public void setSenderDesc(String senderDesc) {
		this.senderDesc = senderDesc;
	}
	public String getMessageInfoId() {
		return messageInfoId;
	}
	public void setMessageInfoId(String messageInfoId) {
		this.messageInfoId = messageInfoId;
	}
	public String getMessageInfoDesc() {
		return messageInfoDesc;
	}
	public void setMessageInfoDesc(String messageInfoDesc) {
		this.messageInfoDesc = messageInfoDesc;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentDesc() {
		return contentDesc;
	}
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
