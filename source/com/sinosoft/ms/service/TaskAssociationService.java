package com.sinosoft.ms.service;

import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.dto.TaskAssociationDto;

/**
 * 这个服务接口用于提供以下服务：
 * 用来处理定时任务与不同的推送方式之间的关联关系的处理。
 * 
 * @author HanYan
 * @date 2014-10-21
 */
public interface TaskAssociationService {
	
	/**
	 * 将关联关系保存。
	 * @param dto
	 */
	public void save(TaskAssociationDto dto);
	
	/**
	 * 将关联关系保存，有事务控制。
	 * @param dto
	 */
	public void saveWithTrans(TaskAssociationDto dto);
	
	/**
	 * 根据给定的Task读出整个关联关系。
	 * @param task
	 * @return
	 */
	public TaskAssociationDto read(Task task);
	
	/**
	 * 更新关联关系。
	 * @param dto
	 */
	public void update(TaskAssociationDto dto);
	
	/**
	 * 根据传入的任务ID删除关联关系。
	 * @param task
	 */
	public void delete(Task task);
}
