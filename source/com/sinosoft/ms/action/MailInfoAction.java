package com.sinosoft.ms.action;

import java.util.HashMap;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.MailInfo;
import com.sinosoft.ms.service.MailInfoService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理邮件内容的Action。
 * 
 * @author HanYan
 * @date 2014-10-15
 */
public class MailInfoAction {
	private MailInfoService infoService;

	public void setInfoService(MailInfoService infoService) {
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
		infoService.add(mail);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 删除的操作
	 * @return
	 */
	public String remove() {
		infoService.delete(mail);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", mail.getMailId() + "删除成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 修改的操作
	 * @return
	 */
	public String modify() {
		infoService.update(mail);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", mail.getMailId() + "修改成功!");
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
		context.put("mails", infoService.queryOfTask(""));
		return StringUtil.SUCCESS;
	}
	
	/** -------------以下为Action参数------------- */
	
	private MailInfo mail;
	private String msg;

	public MailInfo getMail() {
		return mail;
	}
	public void setMail(MailInfo mail) {
		this.mail = mail;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
