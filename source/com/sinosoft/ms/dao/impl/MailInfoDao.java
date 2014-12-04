package com.sinosoft.ms.dao.impl;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.MailInfo;
import com.sinosoft.ms.dao.BaseSupportDao;
import com.sinosoft.ms.db.QueryHelper;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 邮件信息的数据处理。
 * 
 * @author HanYan
 * @date 2014-10-13
 */
public class MailInfoDao extends BaseSupportDao {
	
	@Override
	public boolean delete(Bean bean) {
		int num = QueryHelper.update("DELETE FROM " + bean.getTableName() + " WHERE MailId=?", cast(bean).getMailId());
		return num == 1;
	}

	@Override
	public boolean update(Bean bean) {
		return update(bean, "MailId=?", cast(bean).getMailId());
	}
	
	@Override
	public Bean read(Object... params) {
		return QueryHelper.read(MailInfo.class, "SELECT * FROM " + getBean().getTableName() + " WHERE MailId=?", params);
	}
	
	@Override
	public Bean read_cache(Object... params) {
		return QueryHelper.read_cache(MailInfo.class, MailInfo.class.getSimpleName(), String.valueOf(params[0]), 
				"SELECT * FROM " + getBean().getTableName() + " WHERE MailId=?", params);
	}

	@Override
	protected Bean getBean() {
		return new MailInfo();
	}
	
	/**
	 * 强转
	 * @param bean
	 * @return
	 */
	private MailInfo cast(Bean bean) {
		if (bean == null || !(bean instanceof MailInfo)) {
			throw new BusinessException("MailInfo强转参数异常!");
		}
		return (MailInfo)bean;
	}
}
