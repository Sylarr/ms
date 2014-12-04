package com.sinosoft.ms.service;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 使用HttpSession来记录在线用户。
 * 这个类的实例有Spring容器控制成单例。
 * TODO:需要一个定时任务，定期的清除usersCache中过期的session
 * 
 * @author HanYan
 * @date 2014-11-04
 */
public class OnlineUserForSessionService implements OnlineUserService {

	private final static Logger log = Logger.getLogger(OnlineUserForSessionService.class);
	
	private static Hashtable<String, HttpSession> usersCache = new Hashtable<String, HttpSession>();
	private final static String USER_CODE = "userCode";
	
	/**
	 * 判断用户当前是否在线。
	 * @param userId
	 * @return
	 */
	public synchronized boolean contains(String userId) {
		if (StringUtil.isEmpty(userId)) {
			throw new BusinessException("用户ID不能为空!");
		}
		boolean flag = usersCache.containsKey(userId);
		if (flag) {
			if (checkSession(usersCache.get(userId))) {
				flag = true;
			} else {// session已经过期
				flag = false;
				usersCache.remove(userId);
			}
		}
		return flag;
	}

	/**
	 * 判断用户当前是否在线。
	 * @param httpSession
	 * @return
	 */
	public synchronized boolean contains(HttpSession httpSession) {
		if (httpSession == null) {
			return false;
		}
		String userId = (String)httpSession.getAttribute(USER_CODE);
		return !StringUtil.isEmpty(userId) && contains(userId);
	}

	/**
	 * 获取当前在线用户数。
	 * @return
	 */
	public int getCount() {
		return usersCache.size();
	}

	/**
	 * 用户登录，将登录信息注册到在线用户中。
	 * @param userId
	 * @param httpSession
	 */
	public synchronized void regist(String userId, HttpSession httpSession) {
		if (StringUtil.isEmpty(userId) || httpSession == null) {
			throw new BusinessException("用户ID或传入的Session为空!");
		}
		if (contains(userId)) {// 已经登录
			HttpSession oldSession = usersCache.get(userId);
			if (httpSession == oldSession) {// 同一次登录
				if (!userId.equals(oldSession.getAttribute(USER_CODE))) {// 同次登录多个用户
					usersCache.remove(oldSession.getAttribute(USER_CODE));
					httpSession.setAttribute(USER_CODE, userId);
					usersCache.put(userId, httpSession);
					log.debug("用户[" + userId + "]同次登录多个用户。");
				}
			} else {// 不同次登录（异地登录）
				httpSession.setAttribute(USER_CODE, oldSession.getAttribute(USER_CODE));
				oldSession.invalidate();
				usersCache.put(userId, httpSession);
				log.debug("用户[" + userId + "]异地登录。");
			}
		} else {// 未登录
			httpSession.setAttribute(USER_CODE, userId);
			usersCache.put(userId, httpSession);
			log.debug("用户[" + userId + "]未登录，进行登录。");
		}
	}

	/**
	 * 用户登出，清除这个用户的在线状态。
	 * @param userId
	 */
	public synchronized void writeOff(String userId) {
		if (StringUtil.isEmpty(userId)) {
			throw new BusinessException("用户ID或传入的Session为空!");
		}
		usersCache.get(userId).invalidate();
		usersCache.remove(userId);
		log.debug("用户[" + userId + "]登出。");
	}

	/**
	 * 用户登出，清除这个用户的在线状态。
	 * @param httpSession
	 */
	public synchronized void writeOff(HttpSession httpSession) {
		if (httpSession == null) {
			return;
		}
		String userId = (String)httpSession.getAttribute(USER_CODE);
		httpSession.invalidate();
		usersCache.remove(userId);
		log.debug("用户[" + userId + "]登出。");
	}
	
	/**
	 * 校验session是否过期。
	 * @param userId
	 * @return
	 */
	private boolean checkSession(HttpSession httpSession) {
		boolean flag = true;
		try {
			httpSession.getCreationTime();
		} catch (IllegalStateException ise) {
			flag = false;
		}
		return flag;
	}
}
