package com.sinosoft.ms.task;

import com.sinosoft.ms.beans.Reverse;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.impl.TaskReverseDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.service.SendReverseService;

/**
 * 用于反向访问消息的任务。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class SendReverseJob implements TargetJob {
	
	private DaoSupport taskReverseDao;
	private DaoSupport reverseDao;
	private SendReverseService sendReverseService;
	
	public void setTaskReverseDao(DaoSupport taskReverseDao) {
		this.taskReverseDao = taskReverseDao;
	}
	public void setReverseDao(DaoSupport reverseDao) {
		this.reverseDao = reverseDao;
	}
	public void setSendReverseService(SendReverseService sendReverseService) {
		this.sendReverseService = sendReverseService;
	}

	/**
	 * 任务执行内容。
	 */
	public void execute(String taskId) {
		TaskReverseDto dto = new TaskReverseDto();
		
		dto.transToDto(taskReverseDao.query("TaskId=?", taskId));// 查询关联关系
		dto.setReverse((Reverse)reverseDao.read(dto.getReverse().getReverseId()));// 设置Reverse内容
		
		if (dto.getReverse() == null) {
			throw new BusinessException("消息反向请求失败：请求信息为空！");
		}
		// 请求。
		sendReverseService.send(dto);
	}
}
