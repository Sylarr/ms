package com.sinosoft.ms.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.cache.CacheManager;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.BeanUtil;

/**
 * DaoSupport接口的默认实现。
 * 
 * @author HanYan
 * @date 2014-09-23
 */
public abstract class BaseSupportDao implements DaoSupport {

	/**
	 * 获得一个Bean的实例。
	 */
	protected abstract Bean getBean();
	
	/**
	 * 删除一条记录。
	 */
	public abstract boolean delete(Bean bean);
	
	/**
	 * 更新一条记录。
	 */
	public abstract boolean update(Bean bean);
	
	/**
	 * 更新一条记录。
	 */
	public abstract Bean read(Object... params);
	
	/**
	 * 读出一条记录并缓存。
	 * @param params
	 * @return
	 */
	public abstract Bean read_cache(Object... params);
	
	/**
	 * 根据条件获得记录数。
	 */
	public long getCount(String condition, Object... params) {
		return QueryHelper.count("SELECT COUNT(*) FROM " + getBean().getTableName() + "WHERE " + condition, params);
	}

	/**
	 * 插入一条数据。
	 */
	public boolean insert(Bean bean) {
		Map<String, Method> cols = BeanUtil.getColumns(bean.getClass());// 获取列名及对应取值的方法
		if (cols.isEmpty()) {
			throw new BusinessException("获取inser语句时，参数为空!");
		}
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(getBean().getTableName()).append("(");
		Object[] params = new Object[cols.size()];// SQL的参数
		Iterator<String> i = cols.keySet().iterator();
		int j = 0;
		String col;
		try {// SQL拼接。
			do {
				col = i.next();
				sql.append(col);
				params[j++] = cols.get(col).invoke(bean);// 同步的设置SQL中的参数。
			} while (i.hasNext() && sql.append(",").length() > 0);
			
			sql.append(") VALUES (");
			for (j = 0; j < cols.size(); j++) {
				if (j > 0) {
					sql.append(",");
				}
				sql.append("?");
			}
			sql.append(")");
		} catch (IllegalArgumentException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} catch (IllegalAccessException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} catch (InvocationTargetException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} finally {
		}
		
		return QueryHelper.update(sql.toString(), params) == 1;
	}
	
	/**
	 * 根据条件查询记录。
	 */
	public List<? extends Bean> query(String condition, Object... params) {
		String sql = "SELECT * FROM " + getBean().getTableName() + " WHERE " + condition;
		return QueryHelper.query(getBean().getClass(), sql, params);
	}

	/**
	 * 根据条件分页查询记录。
	 */
	public List<? extends Bean> query(String condition, int page, int size, Object... params) {
		String sql = "SELECT * FROM " + getBean().getTableName() + " WHERE " + condition;
		return QueryHelper.query_slice(getBean().getClass(), getBean().getPrimaryKey(), sql, page, size, params);
	}
	
	/**
	 * 删除一条数据并清除缓存。
	 * @param bean
	 * @return
	 */
	public Bean delete_cache(Bean bean) {
		if (delete(bean)) {
			CacheManager.evict(bean.getClass().getSimpleName(), bean.getPrimaryKey());
			return bean;
		}
		return null;
	}
	
	/**
	 * 插入一条数据并加入到缓存。
	 * @param bean
	 * @return
	 */
	public boolean insert_cache(Bean bean) {
		if (insert(bean)) {
			CacheManager.set(bean.getClass().getSimpleName(), bean.getPrimaryKey(), bean);
			return true;
		}
		return false;
	}

	/**
	 * 更新一条数据并更新缓存。
	 * @param bean
	 * @return
	 */
	public boolean update_cache(Bean bean) {
		if (update(bean)) {
			CacheManager.set(bean.getClass().getSimpleName(), bean.getPrimaryKey(), bean);
			return true;
		}
		return false;
	}
	
	//TODO ...
	public List<? extends Bean> query_cache(String condition, Object... params) {
		return query(condition, params);
	}
	
	//TODO ...
	public List<? extends Bean> query_cache(String condition, int page, int size, Object... params) {
		return query(condition, params, page, size, params);
	}
	
	/**
	 * 根据条件更新一条记录，这个方法初衷是减少子类代码，将UPDATE语句的大部分写在这里。
	 * 
	 * @param bean
	 * @param condition
	 * @param params
	 * @return
	 */
	public boolean update(Bean bean, String condition, Object... params) {
		Map<String, Method> cols = BeanUtil.getColumns(bean.getClass());// 获取列名及对应取值的方法
		if (cols.isEmpty()) {
			throw new BusinessException("获取update语句时，参数为空!");
		}
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(getBean().getTableName()).append(" SET ");
		Object[] values = new Object[cols.size()];// SQL的参数
		Iterator<String> i = cols.keySet().iterator();
		int j = 0;
		String col;
		try {// SQL拼接。
			do {
				col = i.next();
				sql.append(col).append("=?");
				values[j++] = cols.get(col).invoke(bean);// 同步的设置SQL中的参数。
			} while (i.hasNext() && sql.append(",").length() > 0);
			
			sql.append(" WHERE ").append(condition);
		} catch (IllegalArgumentException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} catch (IllegalAccessException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} catch (InvocationTargetException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} finally {
		}
		Object[] finalParams = new Object[values.length + params.length];
		System.arraycopy(values, 0, finalParams, 0, values.length);
		System.arraycopy(params, 0, finalParams, values.length, params.length);
		return QueryHelper.update(sql.toString(), finalParams) == 1;
	}
	
	/**
	 * 多条数据进行批处理插入。
	 * 
	 * @param beans
	 * @return
	 */
	public int[] insert_batch(List<? extends Bean> beans) {
		if (beans == null || beans.isEmpty()) {
			throw new BusinessException("批量inser时，参数为空!");
		}
		Map<String, Method> cols = BeanUtil.getColumns(beans.get(0).getClass());// 获取列名及对应取值的方法
		if (cols.isEmpty()) {
			throw new BusinessException("获取inser语句时，参数为空!");
		}
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(getBean().getTableName()).append("(");
		Object[][] params = new Object[beans.size()][cols.size()];// SQL的参数
		Iterator<String> i = cols.keySet().iterator();
		int k,j = 0;
		String col;
		try {// SQL拼接。
			do {
				col = i.next();
				sql.append(col);
				for (k = 0; k < beans.size(); k++) {
					params[k][j] = cols.get(col).invoke(beans.get(k));// 同步的设置SQL中的参数
				}
				j++;
			} while (i.hasNext() && sql.append(",").length() > 0);
			
			sql.append(") VALUES (");
			for (j = 0; j < cols.size(); j++) {
				if (j > 0) {
					sql.append(",");
				}
				sql.append("?");
			}
			sql.append(")");
		} catch (IllegalArgumentException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} catch (IllegalAccessException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} catch (InvocationTargetException e) {
			throw new BusinessException("获取inser语句时，异常!", e);
		} finally {
		}
		return QueryHelper.batch(sql.toString(), params);
	}
	
	/**
	 * 根据条件批量删除数据。
	 */
	public int delete(String condition, Object... params) {
		return QueryHelper.update("DELETE FROM " + getBean().getTableName() + " WHERE " + condition, params);
	}

	/**
	 * 提交事务。
	 */
	public void commitTransaction() {
		try {
			QueryHelper.getConnection().commit();
			QueryHelper.getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			throw new BusinessException("提交事务失败!", e);
		}
	}

	/**
	 * 回滚事务。
	 */
	public void rollbackTransaction() {
		try {
			QueryHelper.getConnection().rollback();
			QueryHelper.getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			throw new BusinessException("回滚事务失败!", e);
		}
	}

	/**
	 * 开启一个新的事务。
	 */
	public void startTransaction() {
		try {
			if (QueryHelper.getConnection().getAutoCommit()) {
				QueryHelper.getConnection().setAutoCommit(false);
			}
		} catch (SQLException e) {
			throw new BusinessException("开启事务失败!", e);
		}
	}
	
	/**
	 * 关闭数据库链接。
	 */
	public void closeConnection() {
		QueryHelper.closeConnection();
	}
}
