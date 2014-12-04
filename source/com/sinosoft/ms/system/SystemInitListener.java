package com.sinosoft.ms.system;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.sinosoft.ms.cache.SystemCache;
import com.sinosoft.ms.exception.BusinessException;

/**
 * Initialize the web application context.
 * All of initialization should be write here.
 * 
 * @author HanYan
 * @date 2014-11-10
 */
public class SystemInitListener implements ServletContextListener {
	
	private final static Logger log = Logger.getLogger(SystemInitListener.class);
	/** Parameter specifying the profile's folder */
	private static final String CONFIG_PATH_PARAM = "WebConfigPath";

	/**
	 * Initialize the web application context.
	 */
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		
		String webRootPath = context.getRealPath(File.separator);
		String webConfigPath = (String)context.getInitParameter(CONFIG_PATH_PARAM);
		String configFullPath = webRootPath + webConfigPath;// the full path of the profile's folder.
		
		File file = new File(configFullPath);
		if (!file.exists()) {
			throw new BusinessException("Can not found the folder [" + configFullPath + "], " +
					"please check the context-param in your web.xml which named " + CONFIG_PATH_PARAM);
		}
		file = null;
		
		log.info("Initialization begins ...");
		try {
			//Initialize the web application level configuration with the file named system.properties.
			SystemCache.newInstance().init(configFullPath);
			Class.forName("com.sinosoft.ms.db.DBManager");
			Class.forName("com.sinosoft.ms.cache.CacheManager");
			// add other init(String) methods ...
		} catch (BusinessException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			log.error("Initialization has failed", e);
		} finally {
			log.info("Initialization has finished ...");
		}
	}

	/**
	 * Close the root web application context.
	 */
	public void contextDestroyed(ServletContextEvent event) {
		// do nothing...
	}
}
