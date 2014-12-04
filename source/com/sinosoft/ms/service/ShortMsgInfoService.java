package com.sinosoft.ms.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.ShortMsgInfo;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 短信信息的业务逻辑。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class ShortMsgInfoService {
	
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
	 * 新增一条。
	 * @param shortMsg
	 */
	public void add(ShortMsgInfo shortMsg) {
		dao.insert(shortMsg);
	}
	
	/**
	 * 删除一条。
	 * @param shortMsg
	 */
	public void delete(ShortMsgInfo shortMsg) {
		try {
			dao.startTransaction();
			dao.delete(shortMsg);
			taskShortMsgDao.delete("MsgId=?", shortMsg.getMsgId());
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("删除短信信息失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 更新一条。
	 * @param shortMsg
	 */
	public void update(ShortMsgInfo shortMsg) {
		dao.update(shortMsg);
	}
	
	@SuppressWarnings("unchecked")
	public List<ShortMsgInfo> queryOfTask(String code) {
		log.debug("暂时为查询所有的ShortMsgInfo");
		List<ShortMsgInfo> result = (List<ShortMsgInfo>) dao.query("1=1");
		return result;
	}
	
	/**
	 * 检查给定数组的短信信息中，是否有不存在的ID，将不存在的ID返回。
	 * @param shortMsgs
	 * @return
	 */
	public String checkMsgId(List<ShortMsgInfo> shortMsgs) {
		StringBuffer result = new StringBuffer();
		for(ShortMsgInfo shortMsg : shortMsgs) {
			if (dao.read(shortMsg.getMsgId()) == null) {
				result.append("ID为[" + shortMsg.getMsgId() + "]的短信信息不存在!");
			}
		}
		return result.toString();
	}
}
