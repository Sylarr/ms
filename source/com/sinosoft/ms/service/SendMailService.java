package com.sinosoft.ms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sinosoft.ms.beans.MailInfo;
import com.sinosoft.ms.beans.MailSender;
import com.sinosoft.ms.cache.SystemCache;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.impl.TaskMailDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.net.Connection;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 发送邮件的服务。
 * 2014-11-13正式由SendMailUtil改为服务。
 * 之前的版本为：
 * 2014-08-25:邮件发送工具类。
 * 2014-10-16:改版，整合MailInfo与MailSender两个Bean。调整类名与package，并加入高级功能：认证、记录发送失败的邮件。
 * 2014-11-03:加入返回结果，记录邮件是否发送成功。
 * 
 * @author HanYan
 * @date 2014-11-13
 */
public class SendMailService {

	private final static Logger log = Logger.getLogger(SendMailService.class);
	private final static String HEADER_MESSAGE_ID = "Message-ID";
	private final static String DEFAULT_ENCODING = SystemCache.getCache("mail.encoding");
	
	private Session session;
	private Connection connection;
	private DaoSupport mailSenderDao;
	private TaskLogService taskLogService;
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public void setMailSenderDao(DaoSupport mailSenderDao) {
		this.mailSenderDao = mailSenderDao;
	}
	public void setTaskLogService(TaskLogService taskLogService) {
		this.taskLogService = taskLogService;
	}
	
	/**
	 * 构造时，初始化session
	 */
	public SendMailService() {
		Properties pro = new Properties();
		pro.put("mail.smtp.auth", "true");
		pro.put("mail.transport.protocol", "smtp");
		pro.put("mail.debug", SystemCache.getCache("mail.debug"));
		this.session = Session.getInstance(pro);
	}
	
	/**
	 * 校验发送器是否能可以正常发邮件。
	 * 
	 * @param mailSender
	 * @return
	 */
	public boolean validate(MailSender mailSender) {
		Transport transport = null;
		boolean success = false;
		try {
			transport = session.getTransport();
			// 登录邮件服务器
			transport.connect(mailSender.getHost(), 25, mailSender.getEmail(), mailSender.getPassword());
			success = true;
		} catch (NoSuchProviderException e) {
			log.info("邮件发送器认证失败：获取Transport失败!", e);
		} catch (MessagingException e) {
			log.info("邮件发送器认证失败：认证失败!", e);
		} finally {
			try {
				transport.close();
			} catch (Exception e) {
				log.warn("transport closed warning...", e);
			}
		}
		return success;
	}
	
	/**
	 * 发送邮件的核心方法。
	 */
	public void send(TaskMailDto associationDto) {
		TaskMailDto dto = (TaskMailDto)associationDto;
		if (dto.getSender() == null || dto.getMails() == null || dto.getMails().size() == 0) {
			throw new BusinessException("发送邮件失败：发送器或邮件INFO为空!");
		}
		// 如果只有SenderId，则从数据库中查。这段逻辑写的不太好，意在远程服务调用时，只传发送器ID即可，以后有时间再考虑下更合理的设计。
		if (StringUtil.isEmpty(dto.getSender().getEmail()) && !StringUtil.isEmpty(dto.getSender().getSenderId())) {
			dto.setSender((MailSender)mailSenderDao.read(dto.getSender().getSenderId()));
			if (dto.getSender() == null) {
				throw new BusinessException("不存在的发送器信息!");
			}
		}
		
		MailInfo[] mailsArray = dto.getMails().toArray(new MailInfo[dto.getMails().size()]);
		Object[][] result = send(dto.getSender(), mailsArray);
		
		//以下为记录日志的逻辑。
		//这里不需要对result进行校验，如果进入了发送的代码，则一定有返回结果，如果没有返回结果，则一定是之前就异常了。
		for (int i = 0; i < result.length; i++) {
			if (result[i][1].equals(true)) {//发送成功的邮件。
				taskLogService.loggingSuccess(dto.getTask().getTaskId(), dto.getSender().getSenderId(), dto.getSender().getEmail(), 
						mailsArray[i].getMailId(), mailsArray[i].getRecipientTo(), 
						mailsArray[i].getContent(), "内容为URL", Long.parseLong(result[i][0].toString()));
			} else {//发送失败的邮件。
				taskLogService.loggingFail(dto.getTask().getTaskId(), dto.getSender().getSenderId(), dto.getSender().getEmail(), 
						mailsArray[i].getMailId(), mailsArray[i].getRecipientTo(), 
						mailsArray[i].getContent(), "内容为URL", Long.parseLong(result[i][0].toString()));
			}
		}
	}
	
	/**
	 * 根据发送器MailSender与邮件内容MailInfo发送一封邮件。
	 * @param mailSender
	 * @param mailInfos
	 */
	public Object[][] send(MailSender mailSender, MailInfo mailInfo) {
		return send(mailSender, new MailInfo[]{mailInfo});
	}

	/**
	 * 根据发送器MailSender与邮件内容MailInfo数组发送多封邮件。
	 * @param sender
	 * @param mailInfos
	 */
	public Object[][] send(MailSender sender, MailInfo[] mailInfos) {
		if (sender == null || mailInfos == null || mailInfos.length == 0) {
			throw new BusinessException("发送邮件失败：发送器或邮件INFO为空!");
		}
		
		List<MimeMessage> mimeMessages = new ArrayList<MimeMessage>();
		MimeMessageHelper helper = null;// 使用工具组装MimeMessage
		MimeMessage temp = null;
		
		for (int i = 0; i < mailInfos.length; i++) {
			temp = new MimeMessage(session);
			helper = new MimeMessageHelper(temp, DEFAULT_ENCODING);
			try {
				temp.setHeader("MailId", mailInfos[i].getMailId());
				
				if (StringUtil.isEmpty(mailInfos[i].getFrom())) {
					helper.setFrom(sender.getEmail().replaceAll("corp", "com.cn"));//安信邮箱特殊处理。
				} else {
					helper.setFrom(mailInfos[i].getFrom());
				}
				helper.setSubject(mailInfos[i].getSubject());
				helper.setTo(mailInfos[i].getRecipientTo().split(";"));//收件人
				if (!StringUtil.isEmpty(mailInfos[i].getRecipientCc())) {//抄送人
					helper.setCc(mailInfos[i].getRecipientCc().split(";"));
				}
				helper.setText("1".equals(mailInfos[i].getHtml()) ? 
						connection.connect(mailInfos[i].getContent(), null) : mailInfos[i].getContent(), 
						"1".equals(mailInfos[i].getHtml()));
			} catch (MessagingException e) {
				throw new BusinessException("发送邮件失败：ID为[" + mailInfos[i].getMailId() + "]的邮件生成失败!", e);
			}
			mimeMessages.add(helper.getMimeMessage());
		}
		
		return doSend(mimeMessages.toArray(new MimeMessage[mimeMessages.size()]), sender.getHost(), sender.getEmail(), sender.getPassword());
	}
	
	/**
	 * 根据Mail的API发送一封邮件。
	 * @param mimeMessages
	 * @param host
	 * @param userName
	 * @param pwd
	 */
	protected Object[][] doSend(MimeMessage mimeMessage, String host, String userName, String pwd) {
		return doSend(new MimeMessage[]{mimeMessage}, host, userName, pwd);
	}
	
	/**
	 * 根据Mail的API发送多封邮件。
	 * @param mimeMessages
	 * @param host
	 * @param userName
	 * @param pwd
	 * @return 发送邮件的结果，顺序为传入的MimeMessage[]的顺序，记录发送毫秒及是否成功。
	 */
	protected Object[][] doSend(MimeMessage[] mimeMessages, String host, String userName, String pwd) {
//		Map<Object, Exception> failedMessages = new LinkedHashMap<Object, Exception>();//记录发送失败的 邮件。
		Transport transport = null;
		Object[][] result = new Object[mimeMessages.length][2];// 横向的数组第一位表示系统时间，第二位表示是否发送成功。
		try {
			transport = session.getTransport();
			// 登录邮件服务器
			transport.connect(host, 25, userName, pwd);
		} catch (NoSuchProviderException e) {
			throw new BusinessException("发送邮件失败：获取Transport失败!", e);
		} catch (MessagingException e) {
			throw new BusinessException("发送邮件失败：认证失败!", e);
		}
		String mailId = "";
		try {
			for (int i = 0; i < mimeMessages.length; i++) {
				MimeMessage mimeMessage = mimeMessages[i];
				try {
					if (mimeMessage.getSentDate() == null) {
						mimeMessage.setSentDate(new Date());
					}
					if (mimeMessage.getHeader("MailId") != null) {
						mailId = mimeMessage.getHeader("MailId")[0];
					} else {
						mailId = mimeMessage.getSubject();
					}
					String messageId = mimeMessage.getMessageID();
					mimeMessage.saveChanges();
					if (messageId != null) {
						// 显式指定 message id...
						mimeMessage.setHeader(HEADER_MESSAGE_ID, messageId);
					}
					// 发送邮件。
					transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
					log.info("邮件ID[" + mailId + "]发送成功...");
					result[i][0] = System.currentTimeMillis();
					result[i][1] = true;
				} catch (Exception ex) {
					log.info("邮件ID[" + mailId + "]发送失败...");
					log.error("邮件ID[" + mailId + "]发送失败...", ex);
					result[i][0] = System.currentTimeMillis();
					result[i][1] = false;
				}
			}
		} finally {
			try {
				transport.close();
			} catch (Exception e) {
				log.warn("transport closed warning...", e);
			}
		}
		return result;
	}
}
