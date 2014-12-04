package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.Recipient;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 用户信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
public class RecipientDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE RecipientId=? AND Email=?", cast(bean).getRecipientId(), cast(bean).getEmail());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "RecipientId=? AND Email=?", cast(bean).getRecipientId(), cast(bean).getEmail());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(Recipient.class, "SELECT * FROM " + getBean().getTableName() + " WHERE RecipientId=? AND Email=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(Recipient.class, Recipient.class.getSimpleName(), String.valueOf(params[0])+String.valueOf(params[1]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE RecipientId=? AND Email=?", params);
	}

	@Override
	protected Bean getBean() {
		return new Recipient();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private Recipient cast(Bean bean) {
		if (bean == null || !(bean instanceof Recipient)) {
			throw new BusinessException("Recipient强转参数异常!");
		}
		return (Recipient)bean;
	}
}
