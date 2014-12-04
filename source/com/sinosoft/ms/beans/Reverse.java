package com.sinosoft.ms.beans;

import java.sql.Timestamp;

/**
 * 消息反向推送信息。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
@SuppressWarnings("serial")
public class Reverse implements Bean {

	private String reverseId;
	private String desc;
	private String url;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String status;
	
	public String getPrimaryKey() {
		return reverseId;
	}

	public String getTableName() {
		return "MS_REVERSE";
	}
	
	public String getReverseId() {
		return reverseId;
	}
	public void setReverseId(String reverseId) {
		this.reverseId = reverseId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
