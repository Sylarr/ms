package com.sinosoft.ms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.Kind;
import com.sinosoft.ms.beans.MailSender;
import com.sinosoft.ms.beans.ShortMsgSender;
import com.sinosoft.ms.dao.DaoSupport;

/**
 * Ajax请求的一些业务逻辑。
 * 
 * @author HanYan
 * @date 2014-10-21
 */
public class AjaxService {
	
	private final static Logger log = Logger.getLogger(TaskService.class);
	
	private DaoSupport kindDao;
	private DaoSupport mailSenderDao;
	private DaoSupport mailInfoDao;
	private DaoSupport shortMsgSenderDao;
	private DaoSupport shortMsgInfoDao;
	private DaoSupport reverseDao;
	
	public void setKindDao(DaoSupport kindDao) {
		this.kindDao = kindDao;
	}
	public void setMailSenderDao(DaoSupport mailSenderDao) {
		this.mailSenderDao = mailSenderDao;
	}
	public void setMailInfoDao(DaoSupport mailInfoDao) {
		this.mailInfoDao = mailInfoDao;
	}
	public void setShortMsgSenderDao(DaoSupport shortMsgSenderDao) {
		this.shortMsgSenderDao = shortMsgSenderDao;
	}
	public void setShortMsgInfoDao(DaoSupport shortMsgInfoDao) {
		this.shortMsgInfoDao = shortMsgInfoDao;
	}
	public void setReverseDao(DaoSupport reverseDao) {
		this.reverseDao = reverseDao;
	}
	
	
	/**
	 * 获取推送方式的集合。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> findAllKind() {
		List<Kind> kinds = (List<Kind>)kindDao.query_cache("Status='1' ORDER BY KindId");
		Map<String, String> options = new HashMap<String, String>();
		for (int i = 0; i < kinds.size(); i++) {
			options.put(kinds.get(i).getKindId(), kinds.get(i).getKindName());
		}
		if (options.isEmpty()) {
			log.warn("获取推送方式的集合为空。");
		}
		return options;
	}
	
	/**
	 * 获取所有的邮件发送器，以Map形式返回。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> findAllMailSender() {
		List<MailSender> senders = (List<MailSender>)mailSenderDao.query("1=1 ORDER BY SenderId");
		Map<String, String> options = new HashMap<String, String>();
		for (int i = 0; i < senders.size(); i++) {
			options.put(senders.get(i).getSenderId(), senders.get(i).getSenderName() + "-" + senders.get(i).getEmail());
		}
		if (options.isEmpty()) {
			log.warn("获取邮件发送器的集合为空。");
		}
		return options;
	}
	
	/**
	 * 校验给定的邮件ID是否存在。
	 * @param mailId
	 * @return
	 */
	public String checkMailInfoId(String mailId) {
		if (mailInfoDao.read_cache(mailId) == null) {
			return "ID为[" + mailId + "]的邮件信息不存在!";
		} else {
			return "";
		}
	}
	
	/**
	 * 获取所有的短信发送器，以Map形式返回。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> findAllShortMsgSender() {
		List<ShortMsgSender> senders = (List<ShortMsgSender>)shortMsgSenderDao.query("1=1 ORDER BY SenderId");
		Map<String, String> options = new HashMap<String, String>();
		for (int i = 0; i < senders.size(); i++) {
			options.put(senders.get(i).getSenderId(), senders.get(i).getSenderName());
		}
		if (options.isEmpty()) {
			log.warn("获取短信发送器的集合为空。");
		}
		return options;
	}
	
	/**
	 * 校验给定的短信ID是否存在。
	 * @param msgId
	 * @return
	 */
	public String checkShortMsgInfoId(String msgId) {
		if (shortMsgInfoDao.read(msgId) == null) {
			return "ID为[" + msgId + "]的短信内容不存在!";
		} else {
			return "";
		}
	}
	
	/**
	 * 校验给定的反向请求信息ID(ReverseId)是否存在。
	 * @param reverseId
	 * @return
	 */
	public String checkReverseId(String reverseId) {
		if (reverseDao.read(reverseId) == null) {
			return "ID为[" + reverseId + "]的反向请求不存在!";
		} else {
			return "";
		}
	}
}
