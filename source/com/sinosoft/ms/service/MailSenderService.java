package com.sinosoft.ms.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.MailSender;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 邮件发送器的业务逻辑。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
public class MailSenderService {
	
	private final static Logger log = Logger.getLogger(TaskService.class);
	
	private DaoSupport dao;
	private DaoSupport taskMailDao;
	private SendMailService sendMailService;
	
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	public void setTaskMailDao(DaoSupport taskMailDao) {
		this.taskMailDao = taskMailDao;
	}
	public void setSendMailService(SendMailService sendMailService) {
		this.sendMailService = sendMailService;
	}
	
	
	/**
	 * 新增
	 * @param sender
	 */
	public void add(MailSender sender) {
		dao.insert_cache(sender);
	}
	
	/**
	 * 删除一条
	 * @param sender
	 */
	public void delete(MailSender sender) {
		try {
			dao.startTransaction();
			dao.delete_cache(sender);
			taskMailDao.delete("SenderId=?", sender.getSenderId());
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("删除邮件发送器失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 更新一条
	 * @param sender
	 */
	public void update(MailSender sender) {
		dao.update_cache(sender);
	}
	
	/**
	 * 验证发送器是否链接成功。
	 * @param sender
	 * @return
	 */
	public boolean validate(MailSender sender) {
		return sendMailService.validate(sender);
	}
	
	@SuppressWarnings("unchecked")
	public List<MailSender> queryOfTask(String code) {
		log.debug("暂时为查询所有的MailSender");
		List<MailSender> result = (List<MailSender>) dao.query("1=1 ORDER BY SenderId");
		return result;
	}
}
