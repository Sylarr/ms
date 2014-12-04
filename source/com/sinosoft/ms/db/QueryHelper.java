package com.sinosoft.ms.db;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.PrpDuser;
import com.sinosoft.ms.cache.CacheManager;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 数据库查询工具。
 * 
 * @author HanYan
 * @date 2014-09-10
 */
@SuppressWarnings({ "unchecked", "serial" })
public class QueryHelper {
    
	private final static Logger log = Logger.getLogger(QueryHelper.class);
    private final static QueryRunner runner = new QueryRunner();
    private final static ColumnListHandler columnListHandler = new ColumnListHandler(){
        @Override
        protected Object handleRow(ResultSet rs) throws SQLException {
            Object obj = super.handleRow(rs);
            if(obj instanceof BigInteger)
                return ((BigInteger)obj).longValue();
            return obj;
        }
    };
    
    /**
     * 长整型的格式化类。
     */
    private final static ScalarHandler scaleHandler = new ScalarHandler(){
        @Override
        public Object handle(ResultSet rs) throws SQLException {
            Object obj = super.handle(rs);
            if(obj instanceof BigInteger)
                return ((BigInteger)obj).longValue();
            return obj;
        }       
    };
    
    /**
     * 基准类型的分类
     */
    private final static List<Class<?>> primitiveClasses = new ArrayList<Class<?>>(){{
        add(Long.class);
        add(Integer.class);
        add(String.class);
        add(java.util.Date.class);
        add(java.sql.Date.class);
        add(java.sql.Timestamp.class);
    }};
    
    /**
     * 是否是基准类型
     * @param cls
     * @return
     */
    private final static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || primitiveClasses.contains(cls);
    }
    
    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection() {
        try{
            return DBManager.getConnection();
        }catch(SQLException e){
            throw new BusinessException("获取数据库链接失败!", e);
        }
    }
    
    /**
     * 关闭数据库链接
     */
    public static void closeConnection() {
    	DBManager.closeConnection();
    }

    /**
     * 读取某个对象
     * @param sql
     * @param params
     * @return
     */
    public static <T> T read(Class<T> beanClass, String sql, Object...params) {
        try{
            return (T)runner.query(getConnection(), sql, isPrimitive(beanClass)?scaleHandler:new BeanHandler(beanClass), params);
        }catch(SQLException e){
        	log.error("异常SQL：" + StringUtil.getPreparedSQL(sql, params));
            throw new BusinessException("读取对象失败!", e);
        }
    }
    
    /**
     * 按照key值从名称为cache的缓存中读取某个对象。
     * @param <T>
     * @param beanClass
     * @param cache
     * @param key
     * @param sql
     * @param params
     * @return
     */
    public static <T extends Serializable> T read_cache(Class<T> beanClass, String cache, Serializable key, String sql, Object...params) {
    	T obj = CacheManager.get(beanClass, cache, key);
    	if (obj == null) {
    		obj = read(beanClass, sql, params);
    		CacheManager.set(beanClass.getSimpleName(), key, obj);
    	}
        return obj;
    }
    
    /**
     * 对象查询
     * @param <T>
     * @param beanClass
     * @param sql
     * @param params
     * @return
     */
    public static <T> List<T> query(Class<T> beanClass, String sql, Object...params) {
        try{
            return (List<T>)runner.query(getConnection(), sql, isPrimitive(beanClass)?columnListHandler:new BeanListHandler(beanClass), params);
        }catch(SQLException e){
        	log.error("异常SQL：" + StringUtil.getPreparedSQL(sql, params));
            throw new BusinessException("查询对象失败!", e);
        }finally{
        }
    }

    /**
     * 按照key值从名称为cache的缓存中读取对象。
     * @param <T>
     * @param beanClass
     * @param cache
     * @param key
     * @param sql
     * @param params
     * @return
     */
    public static <T> List<T> query_cache(Class<T> beanClass, String cache, Serializable key, String sql, Object...params) {
    	List<T> objs = CacheManager.get(List.class, cache, key);
    	if (objs == null) {
    		objs = query(beanClass, sql, params);
    		CacheManager.set(cache, key, (Serializable)objs);
    	}
        return objs;
    }
    
    /**
     * 分页查询
     * @param <T>
     * @param beanClass
     * @param sql
     * @param page
     * @param count
     * @param params
     * @return
     */
    public static <T> List<T> query_slice(Class<T> beanClass, String sql, String rowKey, int page, int count, Object...params) {
        if(page < 0 || count < 0) 
            throw new IllegalArgumentException("Illegal parameter of 'page' or 'count', Must be positive.");
        int from = (page - 1) * count;
        int to = page * count;
        to = (to > 0) ? to : Integer.MAX_VALUE;
        String newSql = "SELECT * FROM (" +
                    "SELECT rownumber() over(ORDER BY " + rowKey + ") AS LineNum,T.* FROM (" + sql + ") T" +
                    ") K WHERE LineNum > " + from + "AND LineNum <=" + to;
        return query(beanClass, newSql, params);      
    }
    
    /**
     * 支持缓存的分页查询
     * @param <T>
     * @param beanClass
     * @param cache
     * @param cache_key
     * @param cache_obj_count
     * @param sql
     * @param page
     * @param count
     * @param params
     * @return
     */
    public static <T> List<T> query_slice_cache(Class<T> beanClass, String cache, Serializable cacheKey, String rowKey, String sql, int page, int count, Object...params) {
    	List<T> objs = query_slice(beanClass, sql, rowKey, page, count, params);
    	//TODO 这个设计的不理想，缓存应该是递增的，而不是替换的，这里以后再优化
        /*int from = (page - 1) * count;
        if(from < 0)
            return null;
        int end = Math.min(from + count, objs.size());
        if(from >= end)
            return null;
        return objs.subList(from, end);*/
        return objs;
    }
    
    /**
     * 执行统计查询语句，语句的执行结果必须只返回一个数值
     * @param sql
     * @param params
     * @return
     */
    public static long count(String sql, Object...params) {
        try{
            Number num = (Number)runner.query(getConnection(), sql, scaleHandler, params);
            return (num!=null)?num.longValue():-1;
        }catch(SQLException e){
        	log.error("异常SQL：" + StringUtil.getPreparedSQL(sql, params));
            throw new BusinessException("查询记录数失败!", e);
        }
    }

    /**
     * 执行统计查询语句，语句的执行结果必须只返回一个数值
     * @param cache
     * @param key
     * @param sql
     * @param params
     * @return
     */
    public static long count_cache(String cache, Serializable key, String sql, Object...params) {
    	Number value = (Number)CacheManager.get(cache, key);
    	if (value == null) {
    		value = count(sql, params);
            CacheManager.set(cache, key, value);
        }
        return value.longValue();
    }

    /**
     * 执行INSERT/UPDATE/DELETE语句
     * @param sql
     * @param params
     * @return
     */
    public static int update(String sql, Object...params) {
        try{
            return runner.update(getConnection(), sql, params);
        }catch(SQLException e){
        	log.error("异常SQL：" + StringUtil.getPreparedSQL(sql, params));
            throw new BusinessException("更新对象失败!", e);
        }
    }
    
    /**
     * 批量执行指定的SQL语句
     * @param sql
     * @param params
     * @return
     */
    public static int[] batch(String sql, Object[][] params) {
        try{
            return runner.batch(getConnection(), sql, params);
        }catch(SQLException e){
        	log.error("异常SQL：" + sql);
            throw new BusinessException("批处理执行SQL失败!", e);
        }
    }
    
    
    public static void main (String[] args) throws Exception{
    	Class.forName("com.sinosoft.message.db.DBManager");
        List<PrpDuser> list = QueryHelper.query(PrpDuser.class, "select usercode,username from prpduser where usercode=?", "00074");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getUserCode() + "~" + list.get(i).getUserName());
            byte[] b = list.get(i).getUserName().getBytes("UTF-8");
//            byte[] b = list.get(i).getUserName().getBytes();
            for(int j = 0; j < b.length; j++) {
            	System.out.print(b[j]);
            	System.out.print(",");
            }
            
        }
    }
}
