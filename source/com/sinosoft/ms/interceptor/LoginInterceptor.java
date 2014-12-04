package com.sinosoft.ms.interceptor;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.service.OnlineUserService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 控制登录用户的拦截器。
 * 这里使用HttpSession存储登录状态的用户，如果是多台服务器可以考虑使用数据库。
 * 
 * @author HanYan
 * @date 2014-11-04
 */
public class LoginInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4272285296000483811L;
	private final static Logger log = Logger.getLogger(LoginInterceptor.class);
	
	private OnlineUserService service;
	
	public void setService(OnlineUserService service) {
		this.service = service;
	}


	/**
	 * 未登录时，将跳转至登录界面。
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		ActionContext context = ActionContext.getContext();
		String result = null;
		boolean online = false;
		try {
			online = service.contains(session);
		} catch (BusinessException e) {
			result = StringUtil.ERROR;
			context.put(StringUtil.ERROR_MESSAGE, "验证用户失败：" + e.getMessage());
			log.error("拦截器验证用户时BusinessException异常。", e);
		} catch (Exception ex) {
			result = StringUtil.ERROR;
			context.put(StringUtil.ERROR_MESSAGE, "未知异常：" + ex.getMessage());
			log.error("拦截器验证用户时未知异常。", ex);
		}
		if (invocation.getAction() instanceof com.sinosoft.ms.action.LoginAction) {
			online = true;//临时处理。
		}
		
		if (result != null) {// 有异常
			return result;
		}
		if (online) {
			return invocation.invoke();
		} else {// 未登录。
//			return "loginInput?msg=Session超时!";
			return "loginInput";
		}
	}
}
