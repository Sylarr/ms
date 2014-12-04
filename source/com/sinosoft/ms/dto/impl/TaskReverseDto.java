package com.sinosoft.ms.dto.impl;

import java.util.ArrayList;
import java.util.List;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.Reverse;
import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.beans.TaskReverse;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 定时任务与反向消息的关联关系的DTO。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class TaskReverseDto implements TaskAssociationDto {
	
	private Task task;
	private Reverse reverse;
	
	/**
	 * 将DTO转换为特定的Bean列表
	 */
	public List<TaskReverse> transToBeans() {
		List<TaskReverse> list = new ArrayList<TaskReverse>();
		if (task == null || reverse == null 
				|| StringUtil.isEmpty(task.getTaskId()) ||  StringUtil.isEmpty(reverse.getReverseId())) {
			throw new BusinessException("不能转换为List<TaskMail>，dto格式不正确或参数有误。");
		}
		
		TaskReverse taskReverse = new TaskReverse();
		taskReverse.setTaskId(task.getTaskId());
		taskReverse.setReverseId(reverse.getReverseId());
		list.add(taskReverse);
		
		return list;
	}
	
	/**
	 * 根据给定Bean列表，转换为相应的DTO实例
	 */
	@SuppressWarnings("unchecked")
	public TaskAssociationDto transToDto(List<? extends Bean> beans) {
		if (beans == null || beans.isEmpty()) {
			return this;
		}
		List<TaskReverse> list = (List<TaskReverse>)beans;
		
		task = new Task();
		task.setTaskId(list.get(0).getTaskId());
		reverse = new Reverse();
		reverse.setReverseId(list.get(0).getReverseId());
		
		return this;
	}

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Reverse getReverse() {
		return reverse;
	}
	public void setReverse(Reverse reverse) {
		this.reverse = reverse;
	}
}
