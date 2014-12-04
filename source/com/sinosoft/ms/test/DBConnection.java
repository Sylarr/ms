package com.sinosoft.ms.test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sinosoft.ms.exception.BusinessException;

/**
 * 建立数据库链接并执行SQL的工具类，目前版本没有增加批处理功能。
 * 
 * @author HanYan
 * @date 2014-09-09
 */
public class DBConnection {
	
	private static final String USERNAME = "db2inst1";// 数据库用户名
	private static final String PASSWORD = "hxtest";// 数据库密码
	private static final String DRIVER = "com.ibm.db2.jcc.DB2Driver";// 驱动信息
	private static final String URL = "jdbc:db2://172.16.31.100:50000/testdb";// 数据库地址
	private final static Logger log = Logger.getLogger(DBConnection.class);
	
	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet resultSet;
	private boolean autoCommit = true;

	/**
	 * 构造时，加载数据库驱动。
	 */
	public DBConnection() {
		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			throw new BusinessException("数据库驱动加载失败！", e);
		}
	}
	
	/**
	 * 获得数据库的链接
	 * 
	 * @return
	 */
	public Connection getConnection() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new BusinessException("获取数据库链接失败！", e);
		}
		return connection;
	}
	
	/**
	 * 打开新的事务
	 */
	public void newTransaction() {
		if (autoCommit) {
			try {
				connection.setAutoCommit(false);
				autoCommit = false;
			} catch (SQLException e) {
				throw new BusinessException("开启新的事务失败！", e);
			}
		}
	}

	/**
	 * 增加、删除、改
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public boolean executeUpdate(String sql, List<Object> params) {
		boolean flag = false;
		int result = -1;
		try {
			pstmt = connection.prepareStatement(sql);
			setParams(pstmt, params);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("fail SQL is: " + getPreparedSQL(sql, params), e);
			throw new BusinessException("SQL executeUpdate fail!", e);
		} finally {
			closePreparedStatement();
		}
		flag = result > 0 ? true : false;
		return flag;
	}

	/**
	 * 查询单条记录，以键值对返回。
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> executeQueryOne(String sql, List<Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pstmt = connection.prepareStatement(sql);
			setParams(pstmt, params);
			resultSet = pstmt.executeQuery();// 返回查询结果
			ResultSetMetaData metaData = resultSet.getMetaData();
			int col_len = metaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 0; i < col_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
			}
		} catch (SQLException e) {
			log.error("fail SQL is: " + getPreparedSQL(sql, params), e);
			throw new BusinessException("SQL executeQueryOne fail!", e);
		} finally {
			closePreparedStatement();
		}
		return map;
	}

	/**
	 * 查询多条记录，以键值对返回。
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> executeQuery(String sql, List<Object> params) {
		List<Map<String, Object>> list;
		try {
			list = new ArrayList<Map<String, Object>>();
			pstmt = connection.prepareStatement(sql);
			setParams(pstmt, params);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (SQLException e) {
			log.error("fail SQL is: " + getPreparedSQL(sql, params), e);
			throw new BusinessException("SQL executeQuery fail!", e);
		} finally {
			closePreparedStatement();
		}
		return list;
	}

	/**
	 * 查询单条记录，以POJO的形式返回，类的成员变量名必须与数据库的字段一样，不区分大小写。
	 * 
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> T executeQueryOne(String sql, List<Object> params, Class<T> cls) {
		T resultObject = null;
		try {
			pstmt = connection.prepareStatement(sql);
			setParams(pstmt, params);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				// 通过反射机制创建一个实例
				resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
//				Method m = cls.getDeclaredMethod(name, parameterTypes)
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);
				}
			}
		} catch (SQLException e) {
			log.error("fail SQL is: " + getPreparedSQL(sql, params), e);
			throw new BusinessException("SQL executeQueryOne fail!", e);
		} catch (InstantiationException e) {
			throw new BusinessException("InstantiationException!", e);
		} catch (IllegalAccessException e) {
			throw new BusinessException("IllegalAccessException!", e);
		} catch (SecurityException e) {
			throw new BusinessException("SecurityException!", e);
		} catch (NoSuchFieldException e) {
			throw new BusinessException("NoSuchFieldException!", e);
		} finally {
			closePreparedStatement();
		}
		
		return resultObject;
	}

	/**
	 * 查询多条记录，以POJO的形式返回，类的成员变量名必须与数据库的字段一样，不区分大小写。
	 * 
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> executeQuery(String sql, List<Object> params, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			pstmt = connection.prepareStatement(sql);
			setParams(pstmt, params);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				// 通过反射机制创建一个实例
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);
				}
				list.add(resultObject);
			}
		} catch (SQLException e) {
			log.error("fail SQL is: " + getPreparedSQL(sql, params), e);
			throw new BusinessException("SQL executeQueryOne fail!", e);
		} catch (SecurityException e) {
			throw new BusinessException("SecurityException!", e);
		} catch (IllegalArgumentException e) {
			throw new BusinessException("IllegalArgumentException!", e);
		} catch (InstantiationException e) {
			throw new BusinessException("InstantiationException!", e);
		} catch (IllegalAccessException e) {
			throw new BusinessException("IllegalAccessException!", e);
		} catch (NoSuchFieldException e) {
			throw new BusinessException("NoSuchFieldException!", e);
		} finally {
			closePreparedStatement();
		}
		return list;
	}
	
	/**
	 * 事务提交
	 */
	public void commitTransaction() {
		try {
			connection.commit();
			connection.setAutoCommit(true);
			autoCommit = true;
		} catch (SQLException e) {
			throw new BusinessException("Transaction commit fail!", e);
		}
	}
	
	/**
	 * 事务回滚
	 */
	public void rollbackTransaction() {
		try {
			connection.rollback();
			connection.setAutoCommit(true);
			autoCommit = true;
		} catch (SQLException e) {
			throw new BusinessException("Transaction rollback fail!", e);
		}
	}
	
	/**
	 * 释放数据库连接
	 */
	public void close() {
		try {
			closePreparedStatement();
		} catch (BusinessException e) {
			throw new BusinessException("Connection close fail!", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * 关闭结果集
	 */
	private void closeResultSet() {
		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
		} catch (SQLException e) {
			throw new BusinessException("ResultSet close fail!", e);
		}
	}
	
	/**
	 * 关闭PreparedStatement
	 */
	private void closePreparedStatement() {
		try {
			closeResultSet();
		} catch (BusinessException e) {
			throw new BusinessException("PreparedStatement close fail!", e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * 设置SQL的参数
	 * 
	 * @param pstmt
	 * @param params
	 * @throws SQLException
	 */
	private void setParams(PreparedStatement pstmt, List<Object> params) throws SQLException{
		if (params != null && !params.isEmpty()) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}
		}
	}
	
	/**
	 * 得到真实的执行SQL
	 * @param sql
	 * @param params
	 * @return
	 */
	private String getPreparedSQL(String sql, List<Object> params) {
		int paramCount = 0;
		if (params != null) {
			paramCount = params.size();
		}
		if (paramCount < 1) {
			return sql;
		}
		StringBuffer returnSQL = new StringBuffer();
		String[] subSQL = sql.split("\\?");
		for (int i = 0; i < paramCount; i++) {
			if (params.get(i) instanceof Date) {
				// 这里的日期类型暂时没有处理，待以后扩展
				returnSQL.append(subSQL[i]).append("'").append(params.get(i)).append("'");
			} else if (params.get(i) instanceof String) {
				returnSQL.append(subSQL[i]).append("'").append(params.get(i)).append("'");
			} else {// 其它数字类型
				returnSQL.append(subSQL[i]).append(params.get(i));
			}
		}
		if (subSQL.length > params.size()) {
			returnSQL.append(subSQL[subSQL.length - 1]);
		}
		return returnSQL.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		DBConnection c = new DBConnection();
		c.getConnection();
		ArrayList params = new ArrayList();
		params.add("BusinessType1");
		List<PrpDuser> list = c.executeQuery("select codecode,codecname from prpdcode where codetype=?", params, PrpDuser.class);
		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i).getCODECODE() + "~" + list.get(i).getCODECNAME());
		}
	}
}