package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.Reverse;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 邮件反向推送信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class ReverseDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE ReverseId=?", cast(bean).getReverseId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "ReverseId=?", cast(bean).getReverseId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(Reverse.class, "SELECT * FROM " + getBean().getTableName() + " WHERE ReverseId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(Reverse.class, Reverse.class.getSimpleName(), String.valueOf(params[0]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE ReverseId=?", params);
	}

	@Override
	protected Bean getBean() {
		return new Reverse();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private Reverse cast(Bean bean) {
		if (bean == null || !(bean instanceof Reverse)) {
			throw new BusinessException("Reverse强转参数异常!");
		}
		return (Reverse)bean;
	}
}
