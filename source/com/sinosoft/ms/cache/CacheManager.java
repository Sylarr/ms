package com.sinosoft.ms.cache;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

/**
 * 缓存管理器。
 * 
 * @author HanYan
 * @date 2014-09-24
 */
public class CacheManager {
	private final static Logger log = Logger.getLogger(CacheManager.class);
	private final static String CONFIG_NAME = "ehcache.xml";
	
	private static net.sf.ehcache.CacheManager manager;// EhCache的缓存管理器。
	
	static {
		try {
			String path = SystemCache.getCache("webinfo.path") + CONFIG_NAME;
			log.info("ehcache.xml路径为:" + path);
			manager = new net.sf.ehcache.CacheManager(path);
		} catch (CacheException e) {
			log.error("CacheManager初始化失败!", e);
			manager = new net.sf.ehcache.CacheManager();
		}
	}
	
	/**
	 * 获取缓存实例。
	 * @param cacheName
	 * @return
	 */
	private final static Cache getCache(String cacheName) {
		return manager.getCache(cacheName);
	}
	
	/**
	 * 获取缓存中的数据。
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public final static Object get(String cacheName, Serializable key) {
		Cache cache = null;
		Object obj = null;
		if (cacheName != null && key != null) {
			cache = getCache(cacheName);
			if (cache != null) {
				Element element = cache.get(key);
				obj = element == null ? null : element.getObjectValue();
			} else {
				log.warn("名称为[" + cacheName + "]的缓存没有配置。");
			}
			return obj;
		}
		return null;
	}
	
	/**
	 * 获取缓存中的某个数据。
	 * @param <T>
	 * @param clazz
	 * @param cacheName
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T get(Class<T> clazz, String cacheName, Serializable key) {
		return (T)get(cacheName, key);
	}
	
	/**
	 * 设置缓存中的某个数据。
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public final static void set(String cacheName, Serializable key, Serializable value) {
		Cache cache = null;
		if (cacheName != null && key != null && value != null) {
			Element element = new Element(key, value);
			cache = getCache(cacheName);
			if (cache != null) {
				cache.put(element);
			} else {
				log.warn("名称为[" + cacheName + "]的缓存没有配置。");
			}
		}
	}
	
	/**
	 * 清除缓存中的某个数据。
	 * @param cacheName
	 * @param key
	 */
	public final static void evict(String cacheName, Serializable key) {
		Cache cache = null;
		if (cacheName != null && key != null) {
			cache = getCache(cacheName);
			if (cache != null) {
				cache.remove(key);
			} else {
				log.warn("名称为[" + cacheName + "]的缓存没有配置。");
			}
		}
	}
	
	/**
	 * 清除缓存中的所有数据。
	 * @param cacheName
	 */
	public final static void clear(String cacheName) {
		Cache cache = null;
		if (cacheName != null) {
			cache = getCache(cacheName);
			if (cache != null) {
				cache.removeAll();
			} else {
				log.warn("名称为[" + cacheName + "]的缓存没有配置。");
			}
		}
	}
	
	/**
	 * 管理器销毁。
	 */
	public final static void shutdown() {
		if (manager != null) {
			manager.shutdown();
			manager = null;
		}
	}
}
