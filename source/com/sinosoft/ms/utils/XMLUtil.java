package com.sinosoft.ms.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.sinosoft.ms.exception.BusinessException;

/**
 * Dom4j解析XML的工具栏，主要用于XML文档与JAVA对象间的相互转换。
 * 
 * @author HanYan
 * @date 2014-11-13
 */
public class XMLUtil {
	
	/**
	 * 通过给定XML节点和映射类型生成对应的JAVA对象。
	 * 
	 * @param e 需要转换的XML节点
	 * @param c 对应的映射类的Class
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static <T> T marshal(Element e, Class<T> c) {
		if (e == null || c == null) {
			return null;
		}
		Map<String, Method> methods = BeanUtil.getSetMethods(c);
		String value;
		Object temp;
		T instance = null;
		Class<?> paramClass;// 方法的参数类型
		try {
			instance = c.newInstance();
			for (String f : methods.keySet()) {//遍历所有set方法。
				value = e.elementText(f);
				paramClass = methods.get(f).getParameterTypes()[0];//获取该set方法的参数类型
				
				if (paramClass == String.class) {
					temp = (value == null) ? "" : value;
				} else if (paramClass == int.class || paramClass == Integer.class) {
					temp = (value == null) ? 0 : Integer.parseInt(value);
				} else if (paramClass == long.class || paramClass == Long.class) {
					temp = (value == null) ? 0L : Long.parseLong(value);
				} else if (paramClass == double.class || paramClass == Double.class) {
					temp = (value == null) ? 0D : Double.parseDouble(value);
				} else if (paramClass == boolean.class || paramClass == Boolean.class) {
					temp = (value == null) ? null : Boolean.parseBoolean(value);
				} else if (paramClass == float.class || paramClass == Float.class) {
					temp = (value == null) ? 0F : Float.parseFloat(value);
				} else if (paramClass == short.class || paramClass == Short.class) {
					temp = (value == null) ? (short)0 : Short.parseShort(value);
				} else if (paramClass == char.class || paramClass == Character.class) {
					temp = (value == null) ? '0' : value.charAt(0);
				} else if (paramClass == byte.class || paramClass == Byte.class) {
					temp = (value == null) ? (byte)0 : Byte.parseByte(value);
				} else if (paramClass == java.sql.Date.class) {// java.sql.Date
					temp = (value == null) ? null : java.sql.Date.valueOf(value);
				} else if (paramClass == java.sql.Timestamp.class) {// java.sql.Timestamp
					temp = (value == null) ? null : java.sql.Timestamp.valueOf(value);
				} else if (paramClass == java.util.Date.class) {// java.util.Date
					temp = (value == null) ? null : new java.util.Date(value);
				} else if (List.class.isAssignableFrom(paramClass)) {// java.util.List
					Type type = methods.get(f).getGenericParameterTypes()[0];
					if (type == null ) {
						throw new BusinessException("解析报文异常：不支持无泛型的java.util.List子类自动匹配。");
					}
					Class<?> clazz = null;// 泛型中的类型
					if (type instanceof ParameterizedType) {
						temp = new ArrayList();
						clazz = (Class<?>)((ParameterizedType)type).getActualTypeArguments()[0];
						for (Element element : (List<Element>)e.elements(f)) {
							((ArrayList)temp).add(marshal(element, clazz));
						}
					} else {
						temp = null;
					}
				} else if (Set.class.isAssignableFrom(paramClass)) {// java.util.Set
					throw new BusinessException("解析报文异常：不支持Set子类自动匹配。");
				} else if (Map.class.isAssignableFrom(paramClass)) {// java.util.Map
					throw new BusinessException("解析报文异常：不支持Map子类自动匹配。");
				} else if (paramClass.isArray()) {// Array
					throw new BusinessException("解析报文异常：不支持数组自动匹配。");
				} else {// 是对象类型。
					temp = marshal(e.element(f), paramClass);
				}
				methods.get(f).invoke(instance, temp);
			}
		} catch (InstantiationException ex) {
			throw new BusinessException("解析报文异常：" + ex.getMessage());
		} catch (IllegalAccessException ex) {
			throw new BusinessException("解析报文异常：" + ex.getMessage());
		} catch (InvocationTargetException ex) {
			throw new BusinessException("解析报文异常：" + ex.getMessage());
		}
		return instance;
	}
	
	/**
	 * 根据给定JAVA对象，解析成DOM4J中的元素对象Element，用于生成XML文档。
	 * 
	 * @param e
	 * @param obj
	 * @return
	 */
	public static Element unmarshal(Element e, Object obj) {
		if (e == null || obj == null) {
			return null;
		}
		Class<?> c = null;
		Map<String, Method> methods = BeanUtil.getColumns(obj.getClass());
		Object value;
		try {
			for (String f : methods.keySet()) {//遍历所有get方法。
				value = methods.get(f).invoke(obj);
				c = value.getClass();
				if (value == null) {
					e.addElement(f).setText("");
				} else {
					if (c.isPrimitive() || c == String.class || c == java.sql.Date.class 
							|| c == java.sql.Timestamp.class || c == java.util.Date.class) {
						e.addElement(f).setText(String.valueOf(value));
					} else if (Collection.class.isAssignableFrom(c)) {// java.util.Collection
						for (Object o : (Collection<?>)value) {
							unmarshal(e.addElement(f), o);
						}
					} else if (c.isArray()) {
						throw new BusinessException("组织报文异常：不支持数组自动匹配。");
					} else if (Map.class.isAssignableFrom(c)) {
						throw new BusinessException("组织报文异常：不支持Map子类自动匹配。");
					} else {// 是对象类型。
						unmarshal(e.addElement(f), value);
					}
				}
			}
		} catch (IllegalAccessException ex) {
			throw new BusinessException("组织报文异常：" + ex.getMessage());
		} catch (InvocationTargetException ex) {
			throw new BusinessException("组织报文异常：" + ex.getMessage());
		}
		
		return e;
	}
}
