package com.sinosoft.ms.service;

import javax.servlet.http.HttpSession;

/**
 * 记录在线用户的服务。
 * 
 * @author HanYan
 * @date 2014-11-04
 */
public interface OnlineUserService {
	
	/**
	 * 用户登录，将登录信息注册到在线用户中。
	 * @param userId
	 * @param httpSession
	 */
	public void regist(String userId, HttpSession httpSession);
	
	/**
	 * 用户登出，清除这个用户的在线状态。
	 * @param userId
	 */
	public void writeOff(String userId);
	
	/**
	 * 用户登出，清除这个用户的在线状态。
	 * @param httpSession
	 */
	public void writeOff(HttpSession httpSession);
	
	/**
	 * 判断用户当前是否在线。
	 * @param userId
	 * @return
	 */
	public boolean contains(String userId);
	
	/**
	 * 判断用户当前是否在线。
	 * @param userId
	 * @return
	 */
	public boolean contains(HttpSession httpSession);
	
	/**
	 * 获取当前在线用户数。
	 * @return
	 */
	public int getCount();
}
