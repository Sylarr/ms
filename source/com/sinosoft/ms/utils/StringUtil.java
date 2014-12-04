package com.sinosoft.ms.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * 字符串工具类。
 * 
 * @author HanYan
 * @date 2014-09-17
 */
public class StringUtil {
	
	public final static String SUCCESS = "success";
	public final static String FAIL = "fail";
	public final static String ERROR = "error";
	public final static String ERROR_MESSAGE = "errorMessage";
	
	/**
	 * 得到真实的执行SQL
	 * @param sql
	 * @param params
	 * @return
	 */
	public static String getPreparedSQL(String sql, Object[] params) {
		int paramCount = 0;
		if (params != null) {
			paramCount = params.length;
		}
		if (paramCount < 1) {
			return sql;
		}
		StringBuffer returnSQL = new StringBuffer();
		try {
			String[] subSQL = sql.split("\\?");
			for (int i = 0; i < paramCount; i++) {
				if (params[i] instanceof Date) {
					// 这里的日期类型暂时没有处理，待以后扩展
					returnSQL.append(subSQL[i]).append("'").append(params[i]).append("'");
				} else if (params[i] instanceof String) {
					returnSQL.append(subSQL[i]).append("'").append(params[i]).append("'");
				} else {// 其它数字类型
					returnSQL.append(subSQL[i]).append(params[i]);
				}
			}
			if (subSQL.length > params.length) {
				returnSQL.append(subSQL[subSQL.length - 1]);
			}
		} catch (Exception e) {
			returnSQL = new StringBuffer(sql);
		}
		return returnSQL.toString();
	}
	
	/**
	 * 判断给定字符串是否为空，包括NULL、空字符串、空格。
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}
	
	/**
	 * 获取真实IP地址
	 * @param request
	 */
	public static String getRealIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");   
		if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("Proxy-Client-IP");   
		}
		if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("WL-Proxy-Client-IP");   
		}   
		if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getRemoteAddr();   
		}
		return ip;
	}
}
