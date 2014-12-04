package com.sinosoft.ms.dao;

import java.util.List;

import com.sinosoft.ms.beans.Bean;

/**
 * DAO接口的扩展接口，实现了缓存、数据库批处理的功能。
 * @author HanYan
 * @date 2014-09-23
 */
public interface DaoSupport extends DAO {
	
	/**
	 * 删除一条数据并清除缓存。
	 * @param bean
	 * @return
	 */
	public Bean delete_cache(Bean bean);
	
	/**
	 * 插入一条数据并加入到缓存。
	 * @param bean
	 * @return
	 */
	public boolean insert_cache(Bean bean);
	
	/**
	 * 更新一条数据并更新缓存。
	 * @param bean
	 * @return
	 */
	public boolean update_cache(Bean bean);
	
	/**
	 * 读出一条记录并缓存。
	 * @param params
	 * @return
	 */
	public Bean read_cache(Object... params);
	
	/**
	 * 根据条件查询记录，并缓存。
	 * @param condition
	 * @param params
	 * @return
	 */
	public List<? extends Bean> query_cache(String condition, Object... params);
	
	/**
	 * 根据条件分页查询记录，并缓存。
	 * @param condition
	 * @param params
	 * @return
	 */
	public List<? extends Bean> query_cache(String condition, int page, int size, Object... params);
	
	/**
	 * 根据条件更新一条记录，这个方法初衷是减少子类代码，将UPDATE语句的大部分写在这里。
	 * 
	 * @param bean
	 * @param condition
	 * @param params
	 * @return
	 */
	public boolean update(Bean bean, String condition, Object... params);
	
	/**
	 * 多条数据进行批处理插入。
	 * 
	 * @param beans
	 * @return
	 */
	public int[] insert_batch(List<? extends Bean> beans);
	
	/**
	 * 根据条件批量删除数据。
	 * @param condition
	 * @return
	 */
	public int delete(String condition, Object... params);
	/**
	 * 开启一个新的事务。
	 */
	public void startTransaction();
	
	/**
	 * 提交事务。
	 */
	public void commitTransaction();
	
	/**
	 * 回滚事务。
	 */
	public void rollbackTransaction();
	
	/**
	 * 关闭数据库链接。
	 */
	public void closeConnection();
}
