package com.sinosoft.ms.remote.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.sinosoft.ms.dto.ServiceDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.BeanUtil;

/**
 * 远程服务的调用封装类。
 * 
 * @author HanYan
 * @date 2014-11-13
 */
public class RemoteServiceInvocation {
	
	private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	private static final Logger log = Logger.getLogger(RemoteServiceInvocation.class);

	private Object service;
	private String methodName;
	private Object[] args;
	private LocalServiceFactory localServiceFactory;
	
	public void setLocalServiceFactory(LocalServiceFactory localServiceFactory) {
		this.localServiceFactory = localServiceFactory;
	}

	/**
	 * inject remote service.
	 * @param dto
	 * @param args
	 */
	public void inject(ServiceDto dto, Object[] args) {
		service = localServiceFactory.getLocalService(dto.getServiceName(), dto.getVersion(), dto.getMethodName());
		methodName = dto.getMethodName();
		this.args = args;
	}
	
	/**
	 * execute remote service.
	 * @return
	 */
	public Object execute() {
		Object result = null;
		long startTime = System.currentTimeMillis();
		// do something
		result = invoke();
		// do something
		log.info("The service[" + service + ":" + methodName + "] execution time：" + (System.currentTimeMillis() - startTime) + " ms.");
		return result;
	}
	
	/**
	 * The real invoke of local service.
	 * @return
	 */
	private Object invoke() {
		Method method = null;
		Object result = null;
		
		try {
			method = service.getClass().getMethod(methodName, args == null ? EMPTY_CLASS_ARRAY : BeanUtil.getClassArray(args));
			result = method.invoke(service, args == null ? EMPTY_OBJECT_ARRAY : args);
		} catch (NoSuchMethodException e) {
			throw new BusinessException("此服务不提供名称为[" + methodName + "]的方法。");
		} catch (IllegalArgumentException e) {
			throw new BusinessException("方法[" + methodName + "]的参数错误。");
		} catch (IllegalAccessException e) {
			log.error("There is an IllegalAccessException:", e);
		} catch (InvocationTargetException e) {
			log.error("There is an InvocationTargetException:", e);
		} finally {
			method = null;
		}
		return result;
	}

	public Object getService() {
		return service;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object getArgs() {
		return args;
	}
}
