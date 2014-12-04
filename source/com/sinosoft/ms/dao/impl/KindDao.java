package com.sinosoft.ms.dao.impl;

import java.util.List;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.Kind;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 推送途径的数据处理。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
public class KindDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE KindId=?", cast(bean).getKindId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "KindId=?", cast(bean).getKindId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(Kind.class, "SELECT * FROM " + getBean().getTableName() + " WHERE KindId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(Kind.class, Kind.class.getSimpleName(), String.valueOf(params[0]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE KindId=?", params);
	}
	
	/**
	 * 重写的带缓存的查询。
	 */
	public List<? extends Bean> query_cache(String condition, Object... params) {
		String sql = "SELECT * FROM " + getBean().getTableName() + " WHERE " + condition;
		return QueryHelper.query_cache(Kind.class, Kind.class.getSimpleName(), "ALL-KIND", sql, params);
	}

	@Override
	protected Bean getBean() {
		return new Kind();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private Kind cast(Bean bean) {
		if (bean == null || !(bean instanceof Kind)) {
			throw new BusinessException("Kind强转参数异常!");
		}
		return (Kind)bean;
	}
}
