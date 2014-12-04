package com.sinosoft.ms.dao;

import java.util.List;

import com.sinosoft.ms.beans.Bean;

/**
 * DAO层的接口，实现增删改查。
 * 
 * @author HanYan
 * @date 2014-09-12
 */
public interface DAO {
	
	/**
	 * 根据主键读出一条记录。
	 * @param params
	 * @return
	 */
	public Bean read(Object...params);
	
	/**
	 * 带条件的查询多条记录。
	 * @param condition
	 * @return
	 */
	public List<? extends Bean> query(String condition, Object... params);
	
	/**
	 * 带条件的分页查询。
	 * @param condition
	 * @param page
	 * @param size
	 * @return
	 */
	public List<? extends Bean> query(String condition, int page, int size, Object... params);
	
	/**
	 * 根据条件得到记录数。
	 * @param condition
	 * @return
	 */
	public long getCount(String condition, Object... params);
	
	/**
	 * 插入一条记录。
	 * @param bean
	 * @return
	 */
	public boolean insert(Bean bean);
	
	/**
	 * 更新一条记录。
	 * @param bean
	 * @return
	 */
	public boolean update(Bean bean);
	
	/**
	 * 删除一条记录。
	 * @param bean
	 * @return
	 */
	public boolean delete(Bean bean);
}
