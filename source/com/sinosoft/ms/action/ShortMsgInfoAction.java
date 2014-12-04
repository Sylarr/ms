package com.sinosoft.ms.action;

import java.util.HashMap;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.ShortMsgInfo;
import com.sinosoft.ms.service.ShortMsgInfoService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理短信内容的Action。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class ShortMsgInfoAction {
	private ShortMsgInfoService infoService;

	public void setInfoService(ShortMsgInfoService infoService) {
		this.infoService = infoService;
	}
	
	/**
	 * 访问新增的录入界面。
	 * @return
	 */
	public String addInput() {
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 新增的操作。
	 * @return
	 */
	public String add() {
		infoService.add(shortMsg);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 删除的操作
	 * @return
	 */
	public String remove() {
		infoService.delete(shortMsg);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", shortMsg.getMsgId() + "删除成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 修改的操作
	 * @return
	 */
	public String modify() {
		infoService.update(shortMsg);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", shortMsg.getMsgId() + "修改成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	public String query() {
		infoService.queryOfTask("");
		ActionContext context = ActionContext.getContext();
		if (msg == null) {
			msg = "查询成功!";
		}
		context.put("msg", msg);
		context.put("shortMsgs", infoService.queryOfTask(""));
		return StringUtil.SUCCESS;
	}
	
	/** -------------以下为Action参数------------- */
	
	private ShortMsgInfo shortMsg;
	private String msg;

	public ShortMsgInfo getShortMsg() {
		return shortMsg;
	}
	public void setShortMsg(ShortMsgInfo shortMsg) {
		this.shortMsg = shortMsg;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
