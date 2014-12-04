package com.sinosoft.ms.beans;

/**
 * 消息推送方式。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
@SuppressWarnings("serial")
public class Kind implements Bean {

	private String kindId;
	private String kindName;
	private String desc;
	private String status;
	
	public String getPrimaryKey() {
		return kindId;
	}

	public String getTableName() {
		return "MS_KIND";
	}

	public String getKindId() {
		return kindId;
	}
	public void setKindId(String kindId) {
		this.kindId = kindId;
	}
	public String getKindName() {
		return kindName;
	}
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
