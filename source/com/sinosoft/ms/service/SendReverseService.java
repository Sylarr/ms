package com.sinosoft.ms.service;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.Reverse;
import com.sinosoft.ms.dto.impl.TaskReverseDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.net.Connection;

/**
 * 发送短信的服务。
 * 
 * @author HanYan
 * @date 2014-11-14
 */
public class SendReverseService {

	private final static Logger log = Logger.getLogger(SendReverseService.class);
	
	private Connection connection;
	private TaskLogService taskLogService;
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public void setTaskLogService(TaskLogService taskLogService) {
		this.taskLogService = taskLogService;
	}
	
	/**
	 * 发送短信的核心的方法。
	 */
	public void send(TaskReverseDto dto) {
		try {
			send(dto.getTask().getTaskId(), dto.getReverse());
			taskLogService.loggingSuccess(dto.getTask().getTaskId(), dto.getReverse().getReverseId(), dto.getReverse().getReverseId(), 
					dto.getReverse().getUrl(), 0L);
		} catch (Exception e) {
			log.error("ID为[" + dto.getReverse() + "]的消息反向请求失败!", e);
			taskLogService.loggingFail(dto.getTask().getTaskId(), dto.getReverse().getReverseId(), dto.getReverse().getReverseId(), 
					dto.getReverse().getUrl(), 0L);
		}
	}
	
	/**
	 * 发送方法。
	 * @param mailSender
	 * @param mailInfo
	 */
	private void send(String taskId, Reverse reverse) {
		if (reverse == null) {
			throw new BusinessException("消息反向请求信息为空!");
		}
		try {
			String result = connection.connect(reverse.getUrl(), taskId);
			log.info("ID为[" + reverse.getReverseId() + "]的消息反向请求成功!返回信息为:" + result);
		} catch (Exception e) {
			log.info("ID为[" + reverse.getReverseId() + "]的消息反向请求失败!");
			throw new BusinessException("ID为[" + reverse.getReverseId() + "]的消息反向请求失败!", e);
		}
	}
	
	/**
	 * 测试。
	 * @param args
	 */
	public static void main(String[] args) {
		SendReverseService util = new SendReverseService();
		util.setConnection(new com.sinosoft.ms.net.QueryConnection());
		Reverse r = new Reverse();
		r.setReverseId("~~~~");
		r.setUrl("");
		util.send("test", r);
	}
}
