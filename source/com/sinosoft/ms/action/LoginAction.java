package com.sinosoft.ms.action;

import java.util.HashMap;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.PrpDuser;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.service.OnlineUserService;
import com.sinosoft.ms.service.UserService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理登录相关请求的Action。
 * 
 * @author HanYan
 * @date 2014-11-04
 */
public class LoginAction {
	
	private UserService userService;
	private OnlineUserService onlineUserService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setOnlineUserService(OnlineUserService onlineUserService) {
		this.onlineUserService = onlineUserService;
	}
	
	/**
	 * 显示登录界面
	 * @return
	 */
	public String loginInput() {
		ActionContext context = ActionContext.getContext();
		if (StringUtil.isEmpty(msg)) {
			context.put("msg", msg);
		}
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 登录请求
	 * @return
	 */
	public String login() {
		ActionContext context = ActionContext.getContext();
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		if (prpDuser == null || StringUtil.isEmpty(prpDuser.getUserCode()) || StringUtil.isEmpty(prpDuser.getPassword())) {
			hm.put("msg", "用户名或密码不能为空!");
			context.setParameters(hm);
			return StringUtil.FAIL;
		}
		PrpDuser loginUser = null;
		try {
			loginUser = userService.login(prpDuser.getUserCode(), prpDuser.getPassword(), 
					StringUtil.getRealIpAddr(ServletActionContext.getRequest()));
			if (loginUser == null) {// 登录失败
				hm.put("msg", "密码错误!");
				context.setParameters(hm);
				return StringUtil.FAIL;
			}
			prpDuser = loginUser;//登录成功，将用户信息响应给页面
			onlineUserService.regist(prpDuser.getUserCode(), ServletActionContext.getRequest().getSession());
		} catch (BusinessException e) {
			hm.put("msg", e.getMessage());
			context.setParameters(hm);
			return StringUtil.FAIL;
		}
		context.put("prpDuser", prpDuser);
		return StringUtil.SUCCESS;
	}
	
	/** -------------以下为Action参数------------- */
	
	private PrpDuser prpDuser;
	private String msg;
	
	public PrpDuser getPrpDuser() {
		return prpDuser;
	}
	public void setPrpDuser(PrpDuser prpDuser) {
		this.prpDuser = prpDuser;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
