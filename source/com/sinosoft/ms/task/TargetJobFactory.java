package com.sinosoft.ms.task;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sinosoft.ms.exception.BusinessException;

/**
 * 根据KindId获取具体执行任务的类的实例的工厂。
 * 在spring的配置文件中配置存放实例的jobInstance。
 * 
 * @author HanYan
 * @date 2014-10-28
 */
public class TargetJobFactory {
	
	private final static Logger log = Logger.getLogger(TargetJobFactory.class);
	private static Map<String, TargetJob> targetjobs = new Hashtable<String, TargetJob>();
	private Map<String, TargetJob> jobInstance;
	
	public void setJobInstance(Map<String, TargetJob> jobInstance) {
		this.jobInstance = jobInstance;
	}

	/**
	 * 初始化，将配置信息转储到静态变量中。
	 */
	public void init() {
		if (jobInstance == null || jobInstance.isEmpty()) {
			throw new BusinessException("spring配置文件中，jobInstance为空!");
		}
		Iterator<String> i = jobInstance.keySet().iterator();
		String key;
		try {
			while (i.hasNext()) {
				key = i.next();
				synchronized (TargetJobFactory.class) {
					targetjobs.put(key, jobInstance.get(key));
				}
			}
			log.info("JobClassFactory初始化完成!");
		} catch (Exception ex) {
			log.error("JobClassFactory初始化失败!", ex);
		} finally {
			i = null;
		}
	}
	
	/**
	 * 生产实例的方法。
	 * @param kindId
	 * @return
	 */
	public static TargetJob getJobClass(String kindId) {
		return targetjobs.get(kindId);
	}
}
