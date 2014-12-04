package com.sinosoft.ms.interceptor;

import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sinosoft.ms.action.TaskAction;
import com.sinosoft.ms.action.TaskAssociationAction;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.service.TaskAssociationService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 在新增任务时，将DTO参数在ParametersInterceptor拦截器自动装配Form表单参数前，强转成具体类型，
 * 并指定具体服务来处理业务逻辑。
 * 
 * @author HanYan
 * @date 2014-10-20
 */
public class TaskInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4745797711194439640L;
	private final static Logger log = Logger.getLogger(TaskInterceptor.class);
	
	/**
	 * 存放TaskAssociationService服务的map，有新增定时任务的推送方式时，只需在AOP注入，增加map参数即可。
	 */
	private Map<String, TaskAssociationService> services;
	
	/**
	 * 存放TaskAssociationDto的类名的map，有新增定时任务的推送方式时，只需在AOP注入时，增加map参数即可。
	 */
	private Map<String, String> dtoClassNames;
	
	public void setServices(Map<String, TaskAssociationService> services) {
		this.services = services;
	}
	public void setDtoClassNames(Map<String, String> dtoClassNames) {
		this.dtoClassNames = dtoClassNames;
	}

	/**
	 * 统一Action对异常的处理。
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		String[] kindId = null;
		try {
			kindId = (String[])invocation.getInvocationContext().getParameters().get("task.kindId");
			if (action instanceof TaskAction) {
				((TaskAction)action).setDto((TaskAssociationDto)Class.forName(dtoClassNames.get(kindId[0])).newInstance());
				((TaskAction)action).setService(services.get(kindId[0]));
				log.debug("TaskAction intercept success..." + kindId[0]);
			} else if (action instanceof TaskAssociationAction) {
				((TaskAssociationAction)action).setDto((TaskAssociationDto)Class.forName(dtoClassNames.get(kindId[0])).newInstance());
				((TaskAssociationAction)action).setService(services.get(kindId[0]));
				log.debug("TaskAssociationAction intercept success..." + kindId[0]);
			}
		} catch (Exception e) {
			log.error("拦截器处理异常，kindId：" + kindId, e);
			return StringUtil.ERROR;
		}
		return invocation.invoke();
	}
}
