package com.sinosoft.ms.dto.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.ShortMsgInfo;
import com.sinosoft.ms.beans.ShortMsgSender;
import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.beans.TaskShortMsg;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 定时任务与发送短信的关联关系的DTO。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class TaskShortMsgDto implements TaskAssociationDto {
	
	private Task task;
	private ShortMsgSender sender;
	private List<ShortMsgInfo> msgs;
	
	/**
	 * 将DTO转换为特定的Bean列表
	 */
	public List<TaskShortMsg> transToBeans() {
		List<TaskShortMsg> list = new ArrayList<TaskShortMsg>();
		if (task == null || sender == null || msgs == null
				|| StringUtil.isEmpty(task.getTaskId()) ||  StringUtil.isEmpty(sender.getSenderId())) {
			throw new BusinessException("不能转换为List<TaskShortMsg>，dto格式不正确或参数有误。");
		}
		
		TaskShortMsg taskShortMsg = null;
		for (int i = 0; i < msgs.size(); i++) {
			taskShortMsg = new TaskShortMsg();
			taskShortMsg.setTaskId(task.getTaskId());
			taskShortMsg.setSenderId(sender.getSenderId());
			taskShortMsg.setMsgId(msgs.get(i).getMsgId());
			list.add(taskShortMsg);
		}
		
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
		List<TaskShortMsg> list = (List<TaskShortMsg>)beans;
		HashSet<String> valid = new HashSet<String>();
		msgs = new ArrayList<ShortMsgInfo>();
		ShortMsgInfo msg = null;
		for (TaskShortMsg taskShortMsg : list) {
			valid.add(taskShortMsg.getTaskId() + taskShortMsg.getSenderId());// 校验是否可以转换为DTO
			msg = new ShortMsgInfo();
			msg.setMsgId(taskShortMsg.getMsgId());
			msgs.add(msg);
		}
		if (valid.size() > 1) {
			throw new BusinessException("不能转换为TaskAssociationDto，beans格式不正确：" + valid.size());
		}
		valid = null;
		task = new Task();
		task.setTaskId(list.get(0).getTaskId());
		sender = new ShortMsgSender();
		sender.setSenderId(list.get(0).getSenderId());
		
		return this;
	}

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public ShortMsgSender getSender() {
		return sender;
	}
	public void setSender(ShortMsgSender sender) {
		this.sender = sender;
	}
	public List<ShortMsgInfo> getMsgs() {
		return msgs;
	}
	public void setMsgs(List<ShortMsgInfo> msgs) {
		this.msgs = msgs;
	}
}
