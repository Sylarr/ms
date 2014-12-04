package com.sinosoft.ms.beans;

/**
 * 用来映射数据库的Bean的公共接口。
 * 所有映射数据库表的Bean都要实现这个接口。
 * 
 * @author HanYan
 * @date 2014-09-12
 */
public interface Bean extends java.io.Serializable {
	
	/**
	 * 获取对应的表的名称。
	 * @return
	 */
	public String getTableName();
	
	/**
	 * 获取主键值，如果是联合主键，则用字符串拼接。
	 * 用于辨别缓存数据。
	 * @return
	 */
	public String getPrimaryKey();
}
