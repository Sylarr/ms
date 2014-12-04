package com.sinosoft.ms.action;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 用于显示JSP页面的Action。
 * 
 * @author HanYan
 * @date 2014-09-02
 */
public class ShowAction {
	
	public String execute() {
		ActionContext context = ActionContext.getContext();
		if (StringUtil.isEmpty(type)) {
			context.put(StringUtil.ERROR_MESSAGE, "未指定type类型！");
			return StringUtil.ERROR;
		}
		if (userName != null) {
			context.put("userName", userName);
		}
		return type;
	}
	
	/** -------------以下为Action参数------------- */

	private String type;
	private String userName;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
