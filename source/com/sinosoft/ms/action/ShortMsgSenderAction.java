package com.sinosoft.ms.action;

import java.util.HashMap;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.ShortMsgSender;
import com.sinosoft.ms.service.ShortMsgSenderService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理短信发送器的Action。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class ShortMsgSenderAction {
	private ShortMsgSenderService senderService;

	public void setSenderService(ShortMsgSenderService senderService) {
		this.senderService = senderService;
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
		senderService.add(sender);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 删除的操作
	 * @return
	 */
	public String remove() {
		senderService.delete(sender);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", sender.getSenderId() + "删除成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 修改的操作
	 * @return
	 */
	public String modify() {
		senderService.update(sender);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", sender.getSenderId() + "修改成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	public String query() {
		senderService.queryOfTask("");
		ActionContext context = ActionContext.getContext();
		if (msg == null) {
			msg = "查询成功";
		}
		context.put("msg", msg);
		context.put("senders", senderService.queryOfTask(""));
		return StringUtil.SUCCESS;
	}
	
	/** -------------以下为Action参数------------- */
	
	private ShortMsgSender sender;
	private String msg;

	public ShortMsgSender getSender() {
		return sender;
	}
	public void setSender(ShortMsgSender sender) {
		this.sender = sender;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
