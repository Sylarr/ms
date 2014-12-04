package com.sinosoft.ms.service;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.PrpDuser;
import com.sinosoft.ms.beans.UserLoginLog;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.MD5Util;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 提供用户信息相关业务的服务。
 * 
 * @author HanYan
 * @date 2014-11-03
 */
public class UserService {
	private final static Logger log = Logger.getLogger(UserService.class);
	
	private DaoSupport userDao;
	private DaoSupport userLoginLogDao;
	
	public void setUserDao(DaoSupport userDao) {
		this.userDao = userDao;
	}
	public void setUserLoginLogDao(DaoSupport userLoginLogDao) {
		this.userLoginLogDao = userLoginLogDao;
	}


	/**
	 * 登录服务。
	 * @param userCode
	 * @param password
	 * @return
	 */
	public PrpDuser login(String userCode, String password, String loginIp) {
		if (StringUtil.isEmpty(userCode) || StringUtil.isEmpty(password)) {
			throw new BusinessException("用户名或密码不能为空!");
		}
		PrpDuser user = (PrpDuser)userDao.read(userCode);
		if (user == null) {
			throw new BusinessException("用户不存在!");
		}
		
		String encryption = "";// 加密后的密文。
		String realPwd = user.getPassword();// 真实的密文
		if (!StringUtil.isEmpty(realPwd) && realPwd.length() == 64) {
			//引用平台系统的逻辑，对密码进行补位。
			encryption = MD5Util.md5(password + realPwd.substring(32, 64)).toUpperCase() + realPwd.substring(32, 64);
		} else {
			encryption = MD5Util.md5(password).toUpperCase();
		}
		
		if (encryption.equals(realPwd)) {//登录成功，返回用户信息。
			log.info("用户[" + userCode + "]登录成功!");
			userLoginLogDao.insert(new UserLoginLog(userCode, loginIp, "1"));
			return user;
		} else {
			log.info("用户[" + userCode + "]登录失败，密码错误!");
			userLoginLogDao.insert(new UserLoginLog(userCode, loginIp, "0"));
			return null;
		}
	}
}
