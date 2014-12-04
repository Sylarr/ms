package com.sinosoft.ms.task;

import java.util.List;

import com.sinosoft.ms.beans.MailInfo;
import com.sinosoft.ms.beans.MailSender;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.impl.TaskMailDto;
import com.sinosoft.ms.service.SendMailService;

/**
 * 用于发邮件的任务。
 * 
 * @author HanYan
 * @date 2014-10-10
 */
public class SendMailJob implements TargetJob {
	
	private DaoSupport taskMailDao;
	private DaoSupport mailSenderDao;
	private DaoSupport mailInfoDao;
	private SendMailService sendMailService;
	
	public void setTaskMailDao(DaoSupport taskMailDao) {
		this.taskMailDao = taskMailDao;
	}
	public void setMailSenderDao(DaoSupport mailSenderDao) {
		this.mailSenderDao = mailSenderDao;
	}
	public void setMailInfoDao(DaoSupport mailInfoDao) {
		this.mailInfoDao = mailInfoDao;
	}
	public void setSendMailService(SendMailService sendMailService) {
		this.sendMailService = sendMailService;
	}
	
	
	/**
	 * 任务执行内容。
	 */
	@SuppressWarnings("unchecked")
	public void execute(String taskId) {
		TaskMailDto dto = new TaskMailDto();
		
		dto.transToDto(taskMailDao.query("TaskId=?", taskId));// 查询关联关系
		dto.setSender((MailSender)mailSenderDao.read(dto.getSender().getSenderId()));// 设置发送器
		
		List<MailInfo> mails = dto.getMails();
		String condition = "MailId IN (";
		Object[] params = new String[mails.size()];
		//组织查询SQL的条件部分与参数。
		for (int i = 0; i < mails.size(); i++) {
			condition += "?";
			condition += (i == mails.size()-1) ? ")" : ",";
			params[i] = mails.get(i).getMailId();
		}
		dto.setMails((List<MailInfo>)mailInfoDao.query(condition, params));// 设置邮件
		// 发送邮件。
		sendMailService.send(dto);
	}
}
