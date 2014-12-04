package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.ShortMsgSender;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 短信发送器的数据处理。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
public class ShortMsgSenderDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE SenderId=?", cast(bean).getSenderId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "SenderId=?", cast(bean).getSenderId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(ShortMsgSender.class, "SELECT * FROM " + getBean().getTableName() + " WHERE SenderId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(ShortMsgSender.class, ShortMsgSender.class.getSimpleName(), String.valueOf(params[0]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE SenderId=?", params);
	}

	@Override
	protected Bean getBean() {
		return new ShortMsgSender();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private ShortMsgSender cast(Bean bean) {
		if (bean == null || !(bean instanceof ShortMsgSender)) {
			throw new BusinessException("ShortMsgSender强转参数异常!");
		}
		return (ShortMsgSender)bean;
	}
}
