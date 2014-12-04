package com.sinosoft.ms.service;

import java.util.List;

import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.beans.TaskMail;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.dto.impl.TaskMailDto;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 定时任务与发送邮件的关联关系的业务逻辑处理。
 * 
 * @author HanYan
 * @date 2014-10-21
 */
public class TaskMailService implements TaskAssociationService {
	
	private DaoSupport dao;
	private MailInfoService mailInfoService;
	
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	public void setMailInfoService(MailInfoService mailInfoService) {
		this.mailInfoService = mailInfoService;
	}
	
	
	/**
	 * 保存Task与邮件方式的关联关系
	 */
	public void save(TaskAssociationDto dto) {
		TaskMailDto taskMailDto = (TaskMailDto)dto;
		List<TaskMail> list = taskMailDto.transToBeans();
		String errorMsg = mailInfoService.checkMailId(taskMailDto.getMails());
		if (errorMsg.length() > 0) {
			throw new BusinessException(errorMsg);
		}
		dao.insert_batch(list);
	}
	
	/**
	 * 保存Task与邮件方式的关联关系，有事务控制。
	 */
	public void saveWithTrans(TaskAssociationDto dto) {
		try {
			dao.startTransaction();
			this.save(dto);
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("保存关联关系失败!", e);
		} finally {
			dao.closeConnection();
		}
	}

	/**
	 * 读出Task与邮件方式的关联关系
	 */
	@SuppressWarnings("unchecked")
	public TaskMailDto read(Task task) {
		List<TaskMail> list = (List<TaskMail>)dao.query("TaskId=?", task.getTaskId());
		TaskMailDto dto = (TaskMailDto)new TaskMailDto().transToDto(list);
		if (dto.getTask() == null) {
			dto.setTask(task);
		}
		return dto;
	}

	/**
	 * 更新Task与邮件方式的关联关系，先删后插。
	 */
	public void update(TaskAssociationDto dto) {
		TaskMailDto taskMailDto = (TaskMailDto)dto;
		List<TaskMail> list = taskMailDto.transToBeans();
		String errorMsg = "";
		try {
			dao.startTransaction();
			errorMsg = mailInfoService.checkMailId(taskMailDto.getMails());
			if (errorMsg.length() > 0) {
				throw new BusinessException(errorMsg);
			}
			dao.delete("TaskId=?", taskMailDto.getTask().getTaskId());
			dao.insert_batch(list);
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("更新关联关系失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 根据传入的Task，删除关联关系。
	 */
	public void delete(Task task) {
		dao.delete("TaskId=?", task.getTaskId());
	}
}
