package com.sinosoft.ms.service;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.TaskLog;
import com.sinosoft.ms.dao.DaoSupport;

/**
 * 任务执行日志信息的业务逻辑。
 * 
 * @author HanYan
 * @date 2014-11-03
 */
public class TaskLogService {
	
	private final static Logger log = Logger.getLogger(TaskLogService.class);
	private final static String SUCCESS_STATUS = "1";
	private final static String FAIL_STATUS = "0";
	
	private DaoSupport dao;
	
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	
	/**
	 * 任务执行成功的日志记录。
	 * @param taskId
	 * @param senderId
	 * @param messageInfoId
	 * @param content
	 */
	public void loggingSuccess(String taskId, String senderId, String messageInfoId, String content, long time) {
		loggingSuccess(taskId, senderId, "", messageInfoId, "", content, "", time);
	}
	
	/**
	 * 任务执行成功的日志记录。（附带描述）
	 * @param taskId
	 * @param senderId
	 * @param senderDesc
	 * @param messageInfoId
	 * @param messageDesc
	 * @param content
	 * @param contentDesc
	 */
	public void loggingSuccess(String taskId, String senderId, String senderDesc, String messageInfoId, 
			String messageInfoDesc, String content, String contentDesc, long time) {
		if (taskId == null || senderId == null || messageInfoId == null) {
			log.error("Task successful log logging failed. because the taskId or the senderId or the messageInfoId is NULL.");
			return;
		}
		TaskLog taskLog = newTaskLog(taskId, senderId, senderDesc, messageInfoId, messageInfoDesc, content, contentDesc, time);
		taskLog.setStatus(SUCCESS_STATUS);
		try {
			dao.insert(taskLog);
		} catch (Exception e) {
			log.error("Task successful log logging failed. The cause is:", e);
		}
	}
	
	/**
	 * 任务执行失败的日志记录。
	 * @param taskId
	 * @param senderId
	 * @param messageInfoId
	 * @param content
	 */
	public void loggingFail(String taskId, String senderId, String messageInfoId, String content, long time) {
		loggingFail(taskId, senderId, "", messageInfoId, "", content, "", time);
	}
	
	/**
	 * 任务执行失败的日志记录。（附带描述）
	 * @param taskId
	 * @param senderId
	 * @param senderDesc
	 * @param messageInfoId
	 * @param messageInfoDesc
	 * @param content
	 * @param contentDesc
	 */
	public void loggingFail(String taskId, String senderId, String senderDesc, String messageInfoId, 
			String messageInfoDesc, String content, String contentDesc, long time) {
		if (taskId == null || senderId == null || messageInfoId == null) {
			log.error("Task failed log logging failed. because the taskId or the senderId or the messageInfoId is NULL.");
			return;
		}
		TaskLog taskLog = newTaskLog(taskId, senderId, senderDesc, messageInfoId, messageInfoDesc, content, contentDesc, time);
		taskLog.setStatus(FAIL_STATUS);
		try {
			dao.insert(taskLog);
		} catch (Exception e) {
			log.error("Task failed log logging failed. The cause is:", e);
		}
	}
	
	/**
	 * 私有方法，用来生成TaskLog实例。
	 * @param taskId
	 * @param senderId
	 * @param senderDesc
	 * @param messageInfoId
	 * @param messageInfoDesc
	 * @param content
	 * @param contentDesc
	 * @return
	 */
	private TaskLog newTaskLog(String taskId, String senderId, String senderDesc, String messageInfoId, 
			String messageInfoDesc, String content, String contentDesc, long time) {
		TaskLog taskLog = new TaskLog();
		taskLog.setTaskId(taskId);
		taskLog.setSenderId(senderId);
		taskLog.setSenderDesc(senderDesc);
		taskLog.setMessageInfoId(messageInfoId);
		taskLog.setMessageInfoDesc(messageInfoDesc);
		taskLog.setContent(content);
		taskLog.setContentDesc(contentDesc);
		taskLog.setExecuteTime(new Timestamp(time > 0L ? time : System.currentTimeMillis()));//系统当前时间。
		return taskLog;
	}
}
