package com.sinosoft.ms.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sinosoft.ms.cache.CacheManager;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 对JavaBean一些操作的工具类。
 * 
 * @author HanYan
 * @date 2014-09-16
 */
public class BeanUtil {
	private final static Logger log = Logger.getLogger(BeanUtil.class);
	private final static String BEAN_COLUMNS_GET = "Bean_Columns_Get";
	private final static String BEAN_COLUMNS_SET = "Bean_Columns_Set";
	
	/**
	 * 获取JavaBean在数据库中的列名，以键值形式（列名 - 获取值的Method:getXXX()）返回
	 * 并将结果缓存。
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Method> getColumns(Class<?> type) {
		HashMap<String, Method> result = (HashMap<String, Method>)CacheManager.get(BEAN_COLUMNS_GET, type.getSimpleName());
		if (result == null) {
			result = (HashMap<String, Method>)getColumns(type, null);
			CacheManager.set(BEAN_COLUMNS_GET, type.getSimpleName(), result);
		}
		return result;
	}
	
	/**
	 * 根据指定列名，获取JavaBean，以键值形式（列名 - 获取值的Method:getXXX()）返回
	 * 
	 * @param type
	 * @param args
	 * @return
	 */
	public static Map<String, Method> getColumns(Class<?> type, String[] args) {
		BeanInfo info = null;
		Map<String, Method> params = new HashMap<String, Method>();
		try {
			info = Introspector.getBeanInfo(type);
			log.debug("查看" + type.getName() + "的Info是否有被缓存，HashCode为：" + info.hashCode());
			PropertyDescriptor[] pd = info.getPropertyDescriptors();
			Method readMethod = null;
			Method writeMethod = null;
			for (int i = 0; i < pd.length; i++) {
				if (args != null) {// 如果有指定列名，则匹配指定的列名。
					for (int j = 0; j < args.length; j++) {
						if (args[j].equalsIgnoreCase(pd[i].getDisplayName())) {
							readMethod = pd[i].getReadMethod();
							writeMethod = pd[i].getWriteMethod();
							break;
						}
					}
				} else {
					readMethod = pd[i].getReadMethod();
					writeMethod = pd[i].getWriteMethod();
				}
				
				if (readMethod != null && writeMethod != null) {//必须同时存在getter and setter才认为是Bean的属性。
					params.put(pd[i].getName(), readMethod);
				}
			}
		} catch (IntrospectionException e) {
			throw new BusinessException("获取Bean的列时异常!", e);
		} finally {
			info = null;
		}
		return params;
	}
	
	/**
	 * 根据给定Class对象，获取其所有属性的set方法。，以键值形式（列名 - 获取值的Method:setXXX()）返回
	 * 并将结果缓存。
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Method> getSetMethods(Class<?> type) {
		HashMap<String, Method> result = (HashMap<String, Method>)CacheManager.get(BEAN_COLUMNS_SET, type.getSimpleName());
		if (result == null) {
			result = (HashMap<String, Method>)getSetMethods(type, null);
			CacheManager.set(BEAN_COLUMNS_SET, type.getSimpleName(), result);
		}
		return result;
	}
	
	/**
	 * 根据给定Class对象和属性名称，获取其所有属性的set方法。
	 * 
	 * @param type
	 * @param args 指定的属性名
	 * @return
	 */
	public static Map<String, Method> getSetMethods(Class<?> type, String[] args) {
		BeanInfo info = null;
		Map<String, Method> params = new HashMap<String, Method>();
		try {
			info = Introspector.getBeanInfo(type);
			log.debug("查看" + type.getName() + "的Info是否有被缓存，HashCode为：" + info.hashCode());
			PropertyDescriptor[] pd = info.getPropertyDescriptors();
			Method readMethod = null;
			Method writeMethod = null;
			for (int i = 0; i < pd.length; i++) {
				if (args != null) {// 如果有指定列名，则匹配指定的列名。
					for (int j = 0; j < args.length; j++) {
						if (args[j].equalsIgnoreCase(pd[i].getDisplayName())) {
							readMethod = pd[i].getReadMethod();
							writeMethod = pd[i].getWriteMethod();
							break;
						}
					}
				} else {
					readMethod = pd[i].getReadMethod();
					writeMethod = pd[i].getWriteMethod();
				}
				
				if (readMethod != null && writeMethod != null) {//必须同时存在getter and setter才认为是Bean的属性。
					params.put(pd[i].getName(), writeMethod);
				}
			}
		} catch (IntrospectionException e) {
			throw new BusinessException("获取set methods时异常!", e);
		} finally {
			info = null;
		}
		return params;
	}
	
	/**
	 * 将给定Object实例的数组转换成其对应Class类型的数组。
	 * 
	 * @param objs
	 * @return
	 */
	public static Class<?>[] getClassArray(Object[] objs) {
		if (objs == null) {
			return null;
		}
		Class<?>[] result = new Class<?>[objs.length];
		for (int i = 0; i < objs.length; i++) {
			result[i] = objs[i].getClass();
		}
		return result;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
}
