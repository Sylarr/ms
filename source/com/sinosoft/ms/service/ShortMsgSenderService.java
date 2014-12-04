package com.sinosoft.ms.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.ShortMsgSender;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 短信发送器的业务逻辑。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class ShortMsgSenderService {
	
	private final static Logger log = Logger.getLogger(TaskService.class);
	
	private DaoSupport dao;
	private DaoSupport taskShortMsgDao;
	
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	public void setTaskShortMsgDao(DaoSupport taskShortMsgDao) {
		this.taskShortMsgDao = taskShortMsgDao;
	}

	/**
	 * 新增
	 * @param sender
	 */
	public void add(ShortMsgSender sender) {
		dao.insert_cache(sender);
	}
	
	/**
	 * 删除一条
	 * @param sender
	 */
	public void delete(ShortMsgSender sender) {
		try {
			dao.startTransaction();
			dao.delete_cache(sender);
			taskShortMsgDao.delete("SenderId=?", sender.getSenderId());
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("删除短信发送器失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 更新一条
	 * @param sender
	 */
	public void update(ShortMsgSender sender) {
		dao.update_cache(sender);
	}
	
	@SuppressWarnings("unchecked")
	public List<ShortMsgSender> queryOfTask(String code) {
		log.debug("暂时为查询所有的ShortMsgSender");
		List<ShortMsgSender> result = (List<ShortMsgSender>) dao.query("1=1 ORDER BY SenderId");
		return result;
	}
}
