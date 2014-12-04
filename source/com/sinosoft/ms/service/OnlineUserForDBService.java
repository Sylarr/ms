package com.sinosoft.ms.service;

import javax.servlet.http.HttpSession;

/**
 * 使用数据库中的表来记录在线用户。
 * 因服务器为多个或Session占用内存过大等原因时，可以考虑使用数据库记录。
 * 暂未开发。
 * 
 * @author HanYan
 * @date 2014-11-04
 */
public class OnlineUserForDBService implements OnlineUserService {

	public boolean contains(String userId) {
		return false;
	}

	public boolean contains(HttpSession httpSession) {
		return false;
	}

	public int getCount() {
		return 0;
	}

	public void regist(String userId, HttpSession httpSession) {

	}

	public void writeOff(String userId) {

	}

	public void writeOff(HttpSession httpSession) {

	}
}
