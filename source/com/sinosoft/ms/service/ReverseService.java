package com.sinosoft.ms.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.Reverse;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 反向消息信息的业务逻辑。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class ReverseService {
	
	private final static Logger log = Logger.getLogger(TaskService.class);
	
	private DaoSupport dao;
	private DaoSupport taskReverseDao;
	
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	public void setTaskReverseDao(DaoSupport taskReverseDao) {
		this.taskReverseDao = taskReverseDao;
	}


	/**
	 * 新增一条。
	 * @param reverse
	 */
	public void add(Reverse reverse) {
		reverse.setStatus("1");
		reverse.setCreateTime(new Timestamp(System.currentTimeMillis()));
		dao.insert(reverse);
	}
	
	/**
	 * 删除一条。
	 * @param reverse
	 */
	public void delete(Reverse reverse) {
		try {
			dao.startTransaction();
			dao.delete(reverse);
			taskReverseDao.delete("ReverseId=?", reverse.getReverseId());
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
	 * @param reverse
	 */
	public void update(Reverse reverse) {
		reverse.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		dao.update(reverse);
	}
	
	@SuppressWarnings("unchecked")
	public List<Reverse> queryOfTask(String code) {
		log.debug("暂时为查询所有的Reverse");
		List<Reverse> result = (List<Reverse>) dao.query("1=1");
		return result;
	}
	
	/**
	 * 检查给定的反向消息信息中，是否有不存在的ID，将不存在的ID返回。
	 * @param reverse
	 * @return
	 */
	public String checkReverseId(Reverse reverse) {
		StringBuffer result = new StringBuffer();
		// 这里没有用缓存，考虑到事务问题可能会导致缓存与数据不同步。
		if (dao.read(reverse.getReverseId()) == null) {
			result.append("ID为[" + reverse.getReverseId() + "]的反向消息信息不存在!");
		}
		return result.toString();
	}
}
