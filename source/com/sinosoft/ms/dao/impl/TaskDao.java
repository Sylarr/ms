package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 任务信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-10-09
 */
public class TaskDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE TaskId=?", cast(bean).getTaskId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "TaskId=?", cast(bean).getTaskId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(Task.class, "SELECT * FROM " + getBean().getTableName() + " WHERE TaskId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(Task.class, Task.class.getSimpleName(), String.valueOf(params[0]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE TaskId=?", params);
	}

	@Override
	protected Bean getBean() {
		return new Task();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private Task cast(Bean bean) {
		if (bean == null || !(bean instanceof Task)) {
			throw new BusinessException("Task强转参数异常!");
		}
		return (Task)bean;
	}
}
