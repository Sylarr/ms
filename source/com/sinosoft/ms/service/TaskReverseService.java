package com.sinosoft.ms.service;

import java.util.List;

import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.beans.TaskReverse;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.dto.impl.TaskReverseDto;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 定时任务与反向消息的关联关系的业务逻辑处理。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class TaskReverseService implements TaskAssociationService {
	
	private DaoSupport dao;
	private ReverseService reverseService;
	
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	public void setReverseService(ReverseService reverseService) {
		this.reverseService = reverseService;
	}
	
	
	/**
	 * 保存Task与反向消息推送的关联关系
	 */
	public void save(TaskAssociationDto dto) {
		TaskReverseDto taskReverseDto = (TaskReverseDto)dto;
		List<TaskReverse> list = taskReverseDto.transToBeans();
		String errorMsg = reverseService.checkReverseId(taskReverseDto.getReverse());
		if (errorMsg.length() > 0) {
			throw new BusinessException(errorMsg);
		}
		dao.insert_batch(list);
	}
	
	/**
	 * 保存Task与反向消息的关联关系，有事务控制。
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
	 * 读出Task与反向消息的关联关系
	 */
	@SuppressWarnings("unchecked")
	public TaskReverseDto read(Task task) {
		List<TaskReverse> list = (List<TaskReverse>)dao.query("TaskId=?", task.getTaskId());
		TaskReverseDto dto = (TaskReverseDto)new TaskReverseDto().transToDto(list);
		if (dto.getTask() == null) {
			dto.setTask(task);
		}
		return dto;
	}

	/**
	 * 更新Task与反向消息的关联关系，先删后插。
	 */
	public void update(TaskAssociationDto dto) {
		TaskReverseDto taskMailDto = (TaskReverseDto)dto;
		List<TaskReverse> list = taskMailDto.transToBeans();
		String errorMsg = "";
		try {
			dao.startTransaction();
			errorMsg = reverseService.checkReverseId(taskMailDto.getReverse());
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
