package com.sinosoft.ms.remote.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sinosoft.ms.cache.SystemCache;
import com.sinosoft.ms.exception.BusinessException;

/**
 * This is a factory used to get a local service.
 * 通过解析web-service.xml（称为远程服务注册表配置文件）配置所有现已提供的远程服务。
 * 在初始化时，将所有配置存入缓存中，并校验其对应的本地服务的准确性。
 * 在生产时，通过远程服务的标识（服务名称、版本号等）生产出对应的本地服务的实例。
 * 
 * @author HanYan
 * @date 2014-11-13
 */
public class LocalServiceFactory {
	
	private static final Logger log = Logger.getLogger(LocalServiceFactory.class);
	private static final String CONFIG_FILE_NAME = "remote-service.xml";
	private static Hashtable<RemoteServiceMapping, RemoteServiceMapping> web_services_registry = 
		new Hashtable<RemoteServiceMapping, RemoteServiceMapping>();
	
	private SpringBeanFactory springBeanFactory;
	
	public void setSpringBeanFactory(SpringBeanFactory springBeanFactory) {
		this.springBeanFactory = springBeanFactory;
	}

	/**
	 * Initialize this factory.
	 */
	public void init() {
		try {
			init_XmlConfigurations();
			init_CheckWebServiceConfig();
			log.info("WebServiceFactory be initialized successfully");
		} catch (BusinessException e) {
			log.error("WebServiceFactory initialize failed:" + e.getMessage(), e);
		}
	}
	
	/**
	 * Load configurations, set the mapping into cache.
	 */
	@SuppressWarnings("unchecked")
	private void init_XmlConfigurations() {
		InputStream inputStream = null;
		Document document = null;
		SAXReader reader = new SAXReader();
		String path = SystemCache.getCache("webinfo.path") + File.separator + CONFIG_FILE_NAME;
		try {
			inputStream = new FileInputStream(new File(path));
			document = reader.read(inputStream);
			Element root = document.getRootElement();
			List<Element> elementList = root.elements("remote-service");
			for (Element e : elementList) {
				RemoteServiceMapping mapping = new RemoteServiceMapping(
						e.attributeValue("name"), 
						e.attributeValue("version"), 
						e.attributeValue("class"));
				List<Element> methods = e.elements("method");
				for (Element em : methods) {
					List<Element> eArgs = em.elements("arguments");
					List<String> args = new ArrayList<String>();
					for (Element ea : eArgs) {
						args.add(ea.getText());
					}
					mapping.addMethod(em.attributeValue("name"), args);
				}
				web_services_registry.put(mapping, mapping);
			}
		} catch (FileNotFoundException e) {
			throw new BusinessException("No config file found:" + path, e);
		} catch (DocumentException e) {
			throw new BusinessException("Document well-formedness error.", e);
		} finally {
			try {
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {
				log.warn("inputStream closed warning...");
			}
		}
	}
	
	/**
	 * Check in service in the configuration file.
	 */
	private void init_CheckWebServiceConfig() {
		Object instance = null;
		Class<?> serviceClass = null;
		for (RemoteServiceMapping service : web_services_registry.keySet()) {
			String namespace = service.getNamespace();
			instance = getServiceBean(namespace);
			if (instance == null) {// check the service instance.
				throw new BusinessException("The class named[" + namespace + "] is not a spring bean, " +
						"and it was not found. please check your config file in " + CONFIG_FILE_NAME);
			}
			Map<String, List<String>> methods = service.getMethods();
			serviceClass = instance.getClass();
			for (String method : methods.keySet()) {
				try {// check the method.
					serviceClass.getMethod(method, getArgsClasses(methods.get(method)));
				} catch (SecurityException e) {
					throw new BusinessException("The method named[" + method + "] with args[" 
							+ getArgsClasses(methods.get(method)) + "] is not public.", e);
				} catch (NoSuchMethodException e) {
					throw new BusinessException("The method named[" + method + "] with args[" 
							+ getArgsClasses(methods.get(method)) + "] was not found.", e);
				}
			}
		}
	}
	
	/**
	 * 根据给定的namespace获取服务实例。
	 * 优先从spring管理的bean中获取。
	 * @param namespace
	 * @return
	 */
	private Object getServiceBean(String namespace) {
		if (namespace == null) {
			return null;
		}
		Object obj = springBeanFactory.lookup(namespace);
		if (obj == null) {
			try {
				obj = Class.forName(namespace).newInstance();
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
				throw new BusinessException("The class named[" + namespace + "] can't new an instance.", e);
			} catch (IllegalAccessException e) {
				throw new BusinessException("The class named[" + namespace + "] has a private constructor.", e);
			}
		}
		return obj;
	}
	
	/**
	 * 获取方法的参数类型。
	 * @param argsClassName
	 * @return
	 */
	private Class<?>[] getArgsClasses(List<String> argsClassName) {
		if (argsClassName == null) {
			return null;
		}
		Class<?>[] result = new Class<?>[argsClassName.size()];
		for (int i = 0; i < argsClassName.size(); i++) {
			try {
				result[i] = Class.forName(argsClassName.get(i));
			} catch (ClassNotFoundException e) {
				throw new BusinessException("The arguments Class named[" + argsClassName.get(i) + "] was not found.", e);
			}
		}
		return result;
	}
	
	/**
	 * 通过给定的远程服务的标识，获取本地服务实例。
	 * @param serviceName
	 * @param version
	 * @param method
	 * @return
	 */
	public synchronized Object getLocalService(String serviceName, String version, String method) {
		RemoteServiceMapping mapping = web_services_registry.get(new RemoteServiceMapping(serviceName, version));
		Object service = null;
		if (mapping != null && mapping.getMethods().containsKey(method)) {
			service = getServiceBean(mapping.getNamespace());
		}
		return service;
	}
	
	/**
	 * 通过给定的远程服务的标识，获取服务的参数类型。
	 * @param serviceName
	 * @param version
	 * @param method
	 * @return
	 */
	public synchronized Class<?>[] getMethodArgsClasses(String serviceName, String version, String method) {
		RemoteServiceMapping mapping = web_services_registry.get(new RemoteServiceMapping(serviceName, version));
		if (mapping != null && mapping.getMethods().containsKey(method)) {
			return getArgsClasses(mapping.getMethods().get(method));
		}
		return null;
	}
}
