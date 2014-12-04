package com.sinosoft.ms.remote.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This factory is used to obtain an instance of the bean, which managed by Spring.
 * This factory is also managed by Spring, and this is a singleton pattern.
 * 
 * @author HanYan
 * @date 2014-11-13
 */
public class SpringBeanFactory implements ApplicationContextAware {
	
	private ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}

	/**
	 * Return an instance of the specified bean.
	 * 
	 * @param beanId
	 * @return
	 */
	public Object lookup(String beanId) {
		if (beanId == null) {
			return null;
		}
		Object bean = null;
		try {
			bean = context.getBean(beanId);
		} catch (NoSuchBeanDefinitionException e) {
			// Do nothing, because this is a normal condition.
		}
		return bean;
	}
}
