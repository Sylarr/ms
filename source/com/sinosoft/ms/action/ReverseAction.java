package com.sinosoft.ms.action;

import java.util.HashMap;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.Reverse;
import com.sinosoft.ms.service.ReverseService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理反向消息的Action。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class ReverseAction {
	private ReverseService reverseService;

	public void setReverseService(ReverseService reverseService) {
		this.reverseService = reverseService;
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
		reverseService.add(reverse);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 删除的操作
	 * @return
	 */
	public String remove() {
		reverseService.delete(reverse);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", reverse.getReverseId() + "删除成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 修改的操作
	 * @return
	 */
	public String modify() {
		reverseService.update(reverse);
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", reverse.getReverseId() + "修改成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	public String query() {
		reverseService.queryOfTask("");
		ActionContext context = ActionContext.getContext();
		if (msg == null) {
			msg = "查询成功!";
		}
		context.put("msg", msg);
		context.put("reverses", reverseService.queryOfTask(""));
		return StringUtil.SUCCESS;
	}
	
	/** -------------以下为Action参数------------- */
	
	private Reverse reverse;
	private String msg;

	public Reverse getReverse() {
		return reverse;
	}
	public void setReverse(Reverse reverse) {
		this.reverse = reverse;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
