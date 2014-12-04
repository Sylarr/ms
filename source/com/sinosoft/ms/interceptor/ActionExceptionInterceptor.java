package com.sinosoft.ms.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理Action异常的拦截器。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
public class ActionExceptionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4745797713694439640L;
	private final static Logger log = Logger.getLogger(ActionExceptionInterceptor.class);

	/**
	 * 统一Action对异常的处理。
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = null;
		ActionContext context = ActionContext.getContext();
		try {
			result = invocation.invoke();
		} catch (BusinessException e) {
			result = StringUtil.ERROR;
			context.put(StringUtil.ERROR_MESSAGE, e.getMessage());
			log.error("Action执行BusinessException异常。", e);
		} catch (Exception e) {
			result = StringUtil.ERROR;
			context.put(StringUtil.ERROR_MESSAGE, "未知异常：" + e.getMessage());
			log.error("Action执行未知异常。", e);
		}
		return result;
	}
}
