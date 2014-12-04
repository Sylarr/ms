package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.ShortMsgInfo;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 短信信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class ShortMsgInfoDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE MsgId=?", cast(bean).getMsgId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "MsgId=?", cast(bean).getMsgId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(ShortMsgInfo.class, "SELECT * FROM " + getBean().getTableName() + " WHERE MsgId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(ShortMsgInfo.class, ShortMsgInfo.class.getSimpleName(), String.valueOf(params[0]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE MsgId=?", params);
	}

	@Override
	protected Bean getBean() {
		return new ShortMsgInfo();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private ShortMsgInfo cast(Bean bean) {
		if (bean == null || !(bean instanceof ShortMsgInfo)) {
			throw new BusinessException("ShortMsgInfo强转参数异常!");
		}
		return (ShortMsgInfo)bean;
	}
}
