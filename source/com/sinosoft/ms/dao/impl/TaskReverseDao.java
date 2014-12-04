package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.TaskReverse;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 任务反向推送关联信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class TaskReverseDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE TaskId=?,ReverseId=?", 
				cast(bean).getTaskId(), cast(bean).getReverseId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "TaskId=?,ReverseId=?", cast(bean).getTaskId(), cast(bean).getReverseId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(TaskReverse.class, "SELECT * FROM " + getBean().getTableName() + " WHERE TaskId=?,ReverseId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(TaskReverse.class, TaskReverse.class.getSimpleName(), 
				String.valueOf(params[0])+String.valueOf(params[1])+String.valueOf(params[2]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE TaskId=?,ReverseId=?", params);
	}

	@Override
	protected Bean getBean() {
		return new TaskReverse();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private TaskReverse cast(Bean bean) {
		if (bean == null || !(bean instanceof TaskReverse)) {
			throw new BusinessException("TaskReverse强转参数异常!");
		}
		return (TaskReverse)bean;
	}
}
