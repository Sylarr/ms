package com.sinosoft.ms.beans;

@SuppressWarnings("serial")
public class Recipient implements Bean {

	private String recipientId;
	private String email;
	
	public String getPrimaryKey() {
		return recipientId+email;
	}

	public String getTableName() {
		return "MS_RECIPIENT";
	}

	public String getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
