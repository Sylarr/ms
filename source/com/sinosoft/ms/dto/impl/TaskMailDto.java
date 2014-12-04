package com.sinosoft.ms.dto.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.MailInfo;
import com.sinosoft.ms.beans.MailSender;
import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.beans.TaskMail;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 定时任务与发送邮件的关联关系的DTO。
 * 
 * @author HanYan
 * @date 2014-10-21
 */
public class TaskMailDto implements TaskAssociationDto {
	
	private Task task;
	private MailSender sender;
	private List<MailInfo> mails;
	
	/**
	 * 将DTO转换为特定的Bean列表
	 */
	public List<TaskMail> transToBeans() {
		List<TaskMail> list = new ArrayList<TaskMail>();
		if (task == null || sender == null || mails == null
				|| StringUtil.isEmpty(task.getTaskId()) ||  StringUtil.isEmpty(sender.getSenderId())) {
			throw new BusinessException("不能转换为List<TaskMail>，dto格式不正确或参数有误。");
		}
		
		TaskMail taskMail = null;
		for (int i = 0; i < mails.size(); i++) {
			taskMail = new TaskMail();
			taskMail.setTaskId(task.getTaskId());
			taskMail.setSenderId(sender.getSenderId());
			taskMail.setMailId(mails.get(i).getMailId());
			list.add(taskMail);
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
		List<TaskMail> list = (List<TaskMail>)beans;
		HashSet<String> valid = new HashSet<String>();
		mails = new ArrayList<MailInfo>();
		MailInfo mail = null;
		for (TaskMail taskMail : list) {
			valid.add(taskMail.getTaskId() + taskMail.getSenderId());// 校验是否可以转换为DTO
			mail = new MailInfo();
			mail.setMailId(taskMail.getMailId());
			mails.add(mail);
		}
		if (valid.size() > 1) {
			throw new BusinessException("不能转换为TaskAssociationDto，beans格式不正确：" + valid.size());
		}
		valid = null;
		task = new Task();
		task.setTaskId(list.get(0).getTaskId());
		sender = new MailSender();
		sender.setSenderId(list.get(0).getSenderId());
		
		return this;
	}

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public MailSender getSender() {
		return sender;
	}
	public void setSender(MailSender sender) {
		this.sender = sender;
	}
	public List<MailInfo> getMails() {
		return mails;
	}
	public void setMails(List<MailInfo> mails) {
		this.mails = mails;
	}
}
