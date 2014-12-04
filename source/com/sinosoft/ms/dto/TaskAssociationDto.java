package com.sinosoft.ms.dto;

import java.util.List;

import com.sinosoft.ms.beans.Bean;
import com.sinosoft.ms.beans.Task;

/**
 * 定时任务与推送方式之间的关联关系的DTO。
 * 每个推送方式的DTO须实现这个接口。
 * 
 * @author HanYan
 * @date 2014-10-21
 */
public interface TaskAssociationDto {
	
	/**
	 * 将DTO转换为特定的Bean列表，用来存储至数据库。
	 * @return
	 */
	public List<? extends Bean> transToBeans();
	
	/**
	 * 根据给定Bean列表，转换为相应的DTO实例。
	 * @return
	 */
	public TaskAssociationDto transToDto(List<? extends Bean> beans);
	
	/**
	 * 设置关联的Task对象。
	 * @param task
	 */
	public void setTask(Task task);
	
	/**
	 * 获取关联的Task对象。
	 * @return
	 */
	public Task getTask();
}
