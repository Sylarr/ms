package com.sinosoft.ms.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.MailInfo;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 邮件信息的业务逻辑。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
public class MailInfoService {
	
	private final static Logger log = Logger.getLogger(TaskService.class);
	
	private DaoSupport dao;
	private DaoSupport taskMailDao;
	
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	public void setTaskMailDao(DaoSupport taskMailDao) {
		this.taskMailDao = taskMailDao;
	}


	/**
	 * 新增一条。
	 * @param mail
	 */
	public void add(MailInfo mail) {
		dao.insert_cache(mail);
	}
	
	/**
	 * 删除一条。
	 * @param mail
	 */
	public void delete(MailInfo mail) {
		try {
			dao.startTransaction();
			dao.delete_cache(mail);
			taskMailDao.delete("MailId=?", mail.getMailId());
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("删除邮件信息失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 更新一条。
	 * @param mail
	 */
	public void update(MailInfo mail) {
		dao.update_cache(mail);
	}
	
	@SuppressWarnings("unchecked")
	public List<MailInfo> queryOfTask(String code) {
		log.debug("暂时为查询所有的MailInfo");
		List<MailInfo> result = (List<MailInfo>) dao.query("1=1");
		return result;
	}
	
	/**
	 * 检查给定数组的邮件信息中，是否有不存在的ID，将不存在的ID返回。
	 * @param mails
	 * @return
	 */
	public String checkMailId(List<MailInfo> mails) {
		StringBuffer result = new StringBuffer();
		for(MailInfo mail : mails) {
			// 这里没有用缓存，考虑到事务问题可能会导致缓存与数据不同步。
			if (dao.read(mail.getMailId()) == null) {
				result.append("ID为[" + mail.getMailId() + "]的邮件信息不存在!");
			}
		}
		return result.toString();
	}
}
