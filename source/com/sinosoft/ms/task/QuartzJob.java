package com.sinosoft.ms.task;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 用于执行Quartz任务的中间类。
 * 
 * @author HanYan
 * @date 2014-10-29
 */
public class QuartzJob extends QuartzJobBean {
	
	private final static Logger log = Logger.getLogger(QuartzJob.class);

	/**
	 * 任务执行内容。
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)	throws JobExecutionException {
		String taskId = null;
		String kindId = null;
		try {
			taskId = context.getJobDetail().getKey().getName();
			log.info("任务[" + taskId + "]执行开始...");
			kindId = (String)context.getJobDetail().getJobDataMap().get("JobKind");
			TargetJobFactory.getJobClass(kindId).execute(taskId);
			log.info("任务[" + taskId + "]执行成功...");
		} catch (Exception e) {
			log.error("任务[" + taskId + "]执行时异常：", e);
		} finally {
		}
	}
}