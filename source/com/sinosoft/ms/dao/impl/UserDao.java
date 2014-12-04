package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.PrpDuser;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 用户信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-09-16
 */
public class UserDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE UserCode=?", cast(bean).getUserCode());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "UserCode=?", cast(bean).getUserCode());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(PrpDuser.class, "SELECT * FROM " + getBean().getTableName() + " WHERE UserCode=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(PrpDuser.class, PrpDuser.class.getSimpleName(), String.valueOf(params[0]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE UserCode=?", params);
	}

	@Override
	protected Bean getBean() {
		return new PrpDuser();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private PrpDuser cast(Bean bean) {
		if (bean == null || !(bean instanceof PrpDuser)) {
			throw new BusinessException("PrpDuser强转参数异常!");
		}
		return (PrpDuser)bean;
	}
}
