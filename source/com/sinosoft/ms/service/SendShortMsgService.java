package com.sinosoft.ms.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sinosoft.ms.beans.ShortMsgInfo;
import com.sinosoft.ms.beans.ShortMsgSender;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.impl.TaskShortMsgDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.net.Connection;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 发送短信的服务。
 * 2014-11-13正式由SendShortMsgUtil改为服务。
 * 之前的版本为：
 * 2014-11-07:短信发送工具类。
 * 
 * @author HanYan
 * @date 2014-11-13
 */
public class SendShortMsgService {

	private final static Logger log = Logger.getLogger(SendShortMsgService.class);
	private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Connection connection;
	private DaoSupport shortMsgSenderDao;
	private TaskLogService taskLogService;
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public void setShortMsgSenderDao(DaoSupport shortMsgSenderDao) {
		this.shortMsgSenderDao = shortMsgSenderDao;
	}
	public void setTaskLogService(TaskLogService taskLogService) {
		this.taskLogService = taskLogService;
	}
	
	/**
	 * 发送短信的核心的方法。
	 */
	public void send(TaskShortMsgDto associationDto) {
		TaskShortMsgDto dto = (TaskShortMsgDto)associationDto;
		
		for (ShortMsgInfo msg : dto.getMsgs()) {
			try {
				send(dto.getSender(), msg);
				taskLogService.loggingSuccess(dto.getTask().getTaskId(), dto.getSender().getSenderId(), "短信账户名：" + dto.getSender().getAccount(), 
						msg.getMsgId(), msg.getPhoneNumber(), msg.getContent(), "", 0L);
			} catch (Exception e) {
				log.error("ID为[" + msg.getMsgId() + "]的短信发送失败!", e);
				taskLogService.loggingFail(dto.getTask().getTaskId(), dto.getSender().getSenderId(), "短信账户名：" + dto.getSender().getAccount(), 
						msg.getMsgId(), msg.getPhoneNumber(), msg.getContent(), e.getMessage(), 0L);
			}
		}
	}
	
	/**
	 * 发送方法。
	 * @param shortMsgSender
	 * @param shortMsgInfo
	 */
	private void send(ShortMsgSender shortMsgSender, ShortMsgInfo shortMsgInfo) {
		if (shortMsgSender == null || shortMsgInfo == null) {
			throw new BusinessException("发送器或短信信息为空!");
		}
		// 如果只有SenderId，则从数据库中查。这段逻辑写的不太好，意在远程服务调用时，只传发送器ID即可，以后有时间再考虑下更合理的设计。
		if (StringUtil.isEmpty(shortMsgSender.getAccount()) && !StringUtil.isEmpty(shortMsgSender.getSenderId())) {
			shortMsgSender = (ShortMsgSender)shortMsgSenderDao.read(shortMsgSender.getSenderId());
			if (shortMsgSender == null) {
				throw new BusinessException("不存在的发送器信息!");
			}
		}
		try {
			String result = connection.connect(shortMsgSender.getUrl(), generateContent(shortMsgSender, shortMsgInfo));
			log.info("ID为[" + shortMsgInfo.getMsgId() + "]的短信发送成功!返回信息为:" + result);
		} catch (Exception e) {
			log.info("ID为[" + shortMsgInfo.getMsgId() + "]的短信发送失败!");
			throw new BusinessException("ID为[" + shortMsgInfo.getMsgId() + "]的短信发送失败!", e);
		}
	}
	
	/**
	 * 组织发送给短信平台的报文。
	 * 
	 * @param sender
	 * @param msg
	 * @return
	 */
	private String generateContent(ShortMsgSender sender, ShortMsgInfo msg) {
		StringBuffer conBuffer = new StringBuffer(512);
		conBuffer.append("account=").append(sender.getAccount());
		conBuffer.append("&password=").append(sender.getPassword());
		conBuffer.append("&smsType=").append(sender.getSmsType() == null ? "" : sender.getSmsType());
		conBuffer.append("&message=");
		conBuffer.append("<com.ctc.ema.server.jwsserver.sms.MtMessage>");
		conBuffer.append("<content>").append(msg.getContent()).append("</content>");
		String[] phoneNumbers = msg.getPhoneNumber().split(";");
		for (String phoneNumber : phoneNumbers) {
			conBuffer.append("<phoneNumber>").append(phoneNumber).append("</phoneNumber>");
		}
		conBuffer.append("<sendTime>").append(format.format(new Date(System.currentTimeMillis()))).append("</sendTime>");
		conBuffer.append("<smsId>4acadda1-5806-4492-9a82-b7ab3f1c8ec0</smsId>");
		conBuffer.append("<subCode>").append(sender.getSubCode() == null ? "" : sender.getSubCode()).append("</subCode>");
		conBuffer.append("</com.ctc.ema.server.jwsserver.sms.MtMessage>");
		
		return conBuffer.toString();
	}
	
	/**
	 * 测试。
	 * @param args
	 */
	public static void main(String[] args) {
		SendShortMsgService util = new SendShortMsgService();
		util.setConnection(new com.sinosoft.ms.net.SmsConnection());
		ShortMsgSender shortMsgSender = new ShortMsgSender();
		ShortMsgInfo shortMsgInfo = new ShortMsgInfo();
		util.send(shortMsgSender, shortMsgInfo);
	}
}
