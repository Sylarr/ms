package com.sinosoft.ms.action;

import java.util.HashMap;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.MailSender;
import com.sinosoft.ms.service.MailSenderService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理邮件发送器的Action。
 * 
 * @author HanYan
 * @date 2014-10-15
 */
public class MailSenderAction {
	private MailSenderService senderService;

	public void setSenderService(MailSenderService senderService) {
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
	
	/**
	 * 验证的操作
	 * @return
	 */
	public String validate() {
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		if (senderService.validate(sender)) {
			hm.put("msg", "验证成功!");
		} else {
			hm.put("msg", "验证失败，请检查配置信息。");
		}
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
	
	private MailSender sender;
	private String msg;

	public MailSender getSender() {
		return sender;
	}
	public void setSender(MailSender sender) {
		this.sender = sender;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
