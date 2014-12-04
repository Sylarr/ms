package com.sinosoft.ms.beans;

/**
 * 任务短信关联信息。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
@SuppressWarnings("serial")
public class TaskShortMsg implements Bean {

	private String taskId;
	private String senderId;
	private String msgId;
	
	public String getPrimaryKey() {
		return taskId + senderId + msgId;
	}

	public String getTableName() {
		return "MS_TASKSHORTMSG";
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
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
