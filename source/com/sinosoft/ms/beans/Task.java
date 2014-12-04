package com.sinosoft.ms.beans;

import java.sql.Timestamp;

/**
 * 任务信息。
 * 
 * @author HanYan
 * @date 2014-10-09
 */
@SuppressWarnings("serial")
public class Task implements Bean {
	
	private String taskId;
	private String taskName;
	private String desc;
	private String cronExpression;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String kindId;
	private String status;
	
	public String getPrimaryKey() {
		return taskId;
	}
	public String getTableName() {
		return "MS_TASK";
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getKindId() {
		return kindId;
	}
	public void setKindId(String kindId) {
		this.kindId = kindId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
