package com.sinosoft.ms.action;

import java.util.HashMap;

import com.opensymphony.xwork2.ActionContext;
import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.service.TaskAssociationService;
import com.sinosoft.ms.service.TaskService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理定时任务的Action。
 * 
 * @author HanYan
 * @date 2014-10-09
 */
public class TaskAction {
	
	private TaskService taskService;
	
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	/**
	 * 查询
	 * @return
	 */
	public String query() {
		ActionContext context = ActionContext.getContext();
		if (msg == null) {
			msg = "查询成功";
		}
		context.put("msg", msg);
		context.put("tasks", taskService.queryAll());
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 新增任务的输入页面。
	 * @return
	 */
	public String addInput() {
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 新增任务。
	 * @return
	 * @throws InterruptedException
	 */
	public String add() {
		ActionContext context = ActionContext.getContext();
		if (StringUtil.isEmpty(task.getTaskId()) || StringUtil.isEmpty(task.getCronExpression())) {
			context.put(StringUtil.ERROR_MESSAGE, "参数有误。");
			return StringUtil.FAIL;
		}
		taskService.addTask(task, service, dto);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 删除任务。
	 * @return
	 */
	public String remove() {
		ActionContext context = ActionContext.getContext();
		if (StringUtil.isEmpty(task.getTaskId())) {
			context.put(StringUtil.ERROR_MESSAGE, "任务不存在。");
			return StringUtil.FAIL;
		}
		taskService.deleteTask(task, service);
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", task.getTaskId() + "删除成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 修改任务。
	 * @return
	 */
	public String modify() {
		ActionContext context = ActionContext.getContext();
		if (StringUtil.isEmpty(task.getTaskId())) {
			context.put(StringUtil.ERROR_MESSAGE, "任务不存在。");
			return StringUtil.FAIL;
		}
		taskService.updateTask(task);
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", task.getTaskId() + "更新并启动成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 暂停任务。
	 * @return
	 */
	public String stop() {
		ActionContext context = ActionContext.getContext();
		if (StringUtil.isEmpty(task.getTaskId())) {
			context.put(StringUtil.ERROR_MESSAGE, "任务不存在。");
			return StringUtil.FAIL;
		}
		taskService.stopTask(task);
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", task.getTaskId() + "关闭成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 开启任务。
	 * @return
	 */
	public String start() {
		ActionContext context = ActionContext.getContext();
		if (StringUtil.isEmpty(task.getTaskId())) {
			context.put(StringUtil.ERROR_MESSAGE, "任务不存在。");
			return StringUtil.FAIL;
		}
		taskService.startTask(task);
		HashMap<String, Object> hm = new HashMap<String, Object>();//向下一个Action传递的参数。
		hm.put("msg", task.getTaskId() + "开启成功!");
		context.setParameters(hm);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 修改任务与推送方式关联关系时，任务部分的初始化。
	 * @return
	 */
	public String modifyAssInput() {
		ActionContext context = ActionContext.getContext();
		task = taskService.read(task);
		context.put("task", task);
		return StringUtil.SUCCESS;
	}
	
	/** -------------以下为Action参数------------- */
	
	private Task task;
	private TaskAssociationDto dto;
	private TaskAssociationService service;
	private String msg;

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
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
