package com.sinosoft.ms.service;

import java.util.List;

import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.beans.TaskShortMsg;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.dto.impl.TaskShortMsgDto;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 定时任务与发送短信的关联关系的业务逻辑处理。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class TaskShortMsgService implements TaskAssociationService {
	
	private DaoSupport dao;
	private ShortMsgInfoService shortMsgInfoService;
	

	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	public void setShortMsgInfoService(ShortMsgInfoService shortMsgInfoService) {
		this.shortMsgInfoService = shortMsgInfoService;
	}
	

	/**
	 * 保存Task与短信方式的关联关系
	 */
	public void save(TaskAssociationDto dto) {
		TaskShortMsgDto taskShortMsgDto = (TaskShortMsgDto)dto;
		List<TaskShortMsg> list = taskShortMsgDto.transToBeans();
		String errorMsg = shortMsgInfoService.checkMsgId(taskShortMsgDto.getMsgs());
		if (errorMsg.length() > 0) {
			throw new BusinessException(errorMsg);
		}
		dao.insert_batch(list);
	}
	
	/**
	 * 保存Task与短信方式的关联关系，有事务控制。
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
	 * 读出Task与短信方式的关联关系
	 */
	@SuppressWarnings("unchecked")
	public TaskShortMsgDto read(Task task) {
		List<TaskShortMsg> list = (List<TaskShortMsg>)dao.query("TaskId=?", task.getTaskId());
		TaskShortMsgDto dto = (TaskShortMsgDto)new TaskShortMsgDto().transToDto(list);
		if (dto.getTask() == null) {
			dto.setTask(task);
		}
		return dto;
	}

	/**
	 * 更新Task与短信方式的关联关系，先删后插。
	 */
	public void update(TaskAssociationDto dto) {
		TaskShortMsgDto taskShortMsgDto = (TaskShortMsgDto)dto;
		List<TaskShortMsg> list = taskShortMsgDto.transToBeans();
		String errorMsg = "";
		try {
			dao.startTransaction();
			errorMsg = shortMsgInfoService.checkMsgId(taskShortMsgDto.getMsgs());
			if (errorMsg.length() > 0) {
				throw new BusinessException(errorMsg);
			}
			dao.delete("TaskId=?", taskShortMsgDto.getTask().getTaskId());
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
