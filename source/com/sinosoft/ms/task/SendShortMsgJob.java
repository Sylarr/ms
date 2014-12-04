package com.sinosoft.ms.task;

import java.util.List;

import com.sinosoft.ms.beans.ShortMsgInfo;
import com.sinosoft.ms.beans.ShortMsgSender;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.impl.TaskShortMsgDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.service.SendShortMsgService;

/**
 * 用于发短信的任务。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class SendShortMsgJob implements TargetJob {
	
	private DaoSupport taskShortMsgDao;
	private DaoSupport shortMsgSenderDao;
	private DaoSupport shortMsgInfoDao;
	private SendShortMsgService sendShortMsgService;
	
	public void setTaskShortMsgDao(DaoSupport taskShortMsgDao) {
		this.taskShortMsgDao = taskShortMsgDao;
	}
	public void setShortMsgSenderDao(DaoSupport shortMsgSenderDao) {
		this.shortMsgSenderDao = shortMsgSenderDao;
	}
	public void setShortMsgInfoDao(DaoSupport shortMsgInfoDao) {
		this.shortMsgInfoDao = shortMsgInfoDao;
	}
	public void setSendShortMsgService(SendShortMsgService sendShortMsgService) {
		this.sendShortMsgService = sendShortMsgService;
	}
	
	
	/**
	 * 任务执行内容。
	 */
	@SuppressWarnings("unchecked")
	public void execute(String taskId) {
		TaskShortMsgDto dto = new TaskShortMsgDto();
		
		dto.transToDto(taskShortMsgDao.query("TaskId=?", taskId));// 查询关联关系
		dto.setSender((ShortMsgSender)shortMsgSenderDao.read(dto.getSender().getSenderId()));// 设置短信发送器
		
		if (dto.getSender() == null) {
			throw new BusinessException("短信发送失败：发送器为空！");
		}
		
		List<ShortMsgInfo> msgs = dto.getMsgs();
		String condition = "MsgId IN (";
		Object[] params = new String[msgs.size()];
		//组织查询SQL的条件部分与参数。
		for (int i = 0; i < msgs.size(); i++) {
			condition += "?";
			condition += (i == msgs.size()-1) ? ")" : ",";
			params[i] = msgs.get(i).getMsgId();
		}
		dto.setMsgs((List<ShortMsgInfo>)shortMsgInfoDao.query(condition, params));// 设置短信内容
		// 发送短信。
		sendShortMsgService.send(dto);
	}
}
