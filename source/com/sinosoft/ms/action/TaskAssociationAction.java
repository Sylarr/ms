package com.sinosoft.ms.action;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.service.TaskAssociationService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 用来处理定时任务与不同的推送方式之间的关联关系的处理的请求。
 * 
 * @author HanYan
 * @date 2014-10-22
 */
public class TaskAssociationAction {
	
	/**
	 * 新增的录入界面。
	 * @return
	 */
	public String input() {
		if (StringUtil.isEmpty(task.getKindId())) {
			return null;
		}
		return task.getKindId();
	}
	
	/**
	 * 修改的录入页面，会初始化修改前的数据。
	 * @return
	 */
	public String modifyInput() {
		ActionContext context = ActionContext.getContext();
		dto = service.read(task);
		context.put("dto", dto);
		return task.getKindId();
	}
	
	/**
	 * 修改关联关系。
	 * @return
	 */
	public String modify() {
		service.update(dto);
		return StringUtil.SUCCESS;
	}
	
	private Task task;
	private TaskAssociationDto dto;
	private TaskAssociationService service;
	
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public TaskAssociationDto getDto() {
		return dto;
	}
	public void setDto(TaskAssociationDto dto) {
		this.dto = dto;
	}
	public TaskAssociationService getService() {
		return service;
	}
	public void setService(TaskAssociationService service) {
		this.service = service;
	}
}
