package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.TaskShortMsg;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 任务与短信的关联信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class TaskShortMsgDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE TaskId=?,SenderId=?,MsgId=?", 
				cast(bean).getTaskId(), cast(bean).getSenderId(), cast(bean).getMsgId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "TaskId=?,SenderId=?,MsgId=?", cast(bean).getTaskId(), cast(bean).getSenderId(), cast(bean).getMsgId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(TaskShortMsg.class, "SELECT * FROM " + getBean().getTableName() + " WHERE TaskId=?,SenderId=?,MsgId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(TaskShortMsg.class, TaskShortMsg.class.getSimpleName(), 
				String.valueOf(params[0])+String.valueOf(params[1])+String.valueOf(params[2]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE TaskId=?,SenderId=?,MsgId=?", params);
	}

	@Override
	protected Bean getBean() {
		return new TaskShortMsg();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private TaskShortMsg cast(Bean bean) {
		if (bean == null || !(bean instanceof TaskShortMsg)) {
			throw new BusinessException("TaskShortMsg强转参数异常!");
		}
		return (TaskShortMsg)bean;
	}
}
