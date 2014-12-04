package com.sinosoft.ms.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.TaskLog;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.BeanUtil;

/**
 * 任务日志信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-11-03
 */
public class TaskLogDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE LogId=?", cast(bean).getLogId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "LogId=?", cast(bean).getLogId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(TaskLog.class, "SELECT * FROM " + getBean().getTableName() + " WHERE LogId=?", params);
	}
	
	/**
	 * 不需要缓存。
	 */
	@Override
	public Bean read_cache(Object... params) {
		return read(params);
	}

	@Override
	protected Bean getBean() {
		return new TaskLog();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private TaskLog cast(Bean bean) {
		if (bean == null || !(bean instanceof TaskLog)) {
			throw new BusinessException("TaskLog强转参数异常!");
		}
		return (TaskLog)bean;
	}
	
	/**
	 * insert时，不需要LogId字段（数据库自增字段），所以覆盖父类方法，处理特殊逻辑。
	 */
	public boolean insert(Bean bean) {
		Map<String, Method> cols = BeanUtil.getColumns(bean.getClass());// 获取列名及对应取值的方法
		cols.remove("logId");//去除数据库自增主键。
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
	 * 日志表不应该有更新操作。
	 */
	public boolean update(Bean bean, String condition, Object... params) {
		return false;
	}
	
	/**
	 * insert时，不需要LogId字段（数据库自增字段），所以覆盖父类方法，处理特殊逻辑。
	 */
	public int[] insert_batch(List<? extends Bean> beans) {
		if (beans == null || beans.isEmpty()) {
			throw new BusinessException("批量inser时，参数为空!");
		}
		Map<String, Method> cols = BeanUtil.getColumns(beans.get(0).getClass());// 获取列名及对应取值的方法
		cols.remove("logId");//去除数据库自增主键。
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
}
