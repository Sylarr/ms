package com.sinosoft.ms.beans;

/**
 * 任务反向推送关联信息。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
@SuppressWarnings("serial")
public class TaskReverse implements Bean {

	private String taskId;
	private String reverseId;
	
	public String getPrimaryKey() {
		return taskId + reverseId;
	}

	public String getTableName() {
		return "MS_TASKREVERSE";
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getReverseId() {
		return reverseId;
	}
	public void setReverseId(String reverseId) {
		this.reverseId = reverseId;
	}
}
