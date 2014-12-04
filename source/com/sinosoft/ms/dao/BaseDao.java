package com.sinosoft.ms.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.BeanUtil;

/**
 * DAO接口的默认实现。
 * 
 * @author HanYan
 * @date 2014-09-15
 */
public abstract class BaseDao implements DAO {

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
}
