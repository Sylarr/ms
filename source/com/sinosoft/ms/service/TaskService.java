package com.sinosoft.ms.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.sinosoft.ms.beans.Task;
import com.sinosoft.ms.dao.DaoSupport;
import com.sinosoft.ms.dto.TaskAssociationDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.task.QuartzJob;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 定时任务业务逻辑。
 * 这个Service比较特殊：自身有一个初始化的方法，在web服务器启动的时候会完成一些逻辑。
 * 
 * @author HanYan
 * @date 2014-10-09
 */
public class TaskService {
	
	private final static Logger log = Logger.getLogger(TaskService.class);
	
	private Scheduler scheduler;
	private DaoSupport dao;
	
	/**
	 * 由spring通过工厂注入，此实例为单例。
	 * @param scheduler
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	public void setDao(DaoSupport dao) {
		this.dao = dao;
	}
	
	/**
	 * 初始化数据库中所有开启状态的定时任务。
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		log.info("Task初始化开始...");
		List<Task> tasks = (List<Task>)dao.query("Status='1' ORDER BY TaskId");
		for (Task task : tasks) {
			addJob(task.getTaskId(), task.getTaskId(), task.getCronExpression(), task.getKindId());
		}
		log.info("Task初始化结束...");
	}

	/**
	 * 新增任务。
	 * @param jobName
	 * @param jobGroup
	 * @param cron
	 */
	public void addJob(String jobName, String jobGroup, String cron, String kindId) {
		try {
			if (StringUtil.isEmpty(jobName) || StringUtil.isEmpty(jobGroup) || StringUtil.isEmpty(cron)) {
				throw new BusinessException("定时任务创建失败，参数为空");
			}
			JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class).withIdentity(jobName, jobGroup).build();
			jobDetail.getJobDataMap().put("JobKind", kindId);
			//表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
			//按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
			log.info("新增定时任务，name:" + jobName + ",group:" + jobGroup + ",cron:" + cron);
		} catch (SchedulerException e) {
			throw new BusinessException("定时任务创建失败!", e);
		}
	}
	
	/**
	 * 删除任务。
	 * @param jobName
	 * @param jobGroup
	 */
	public void deleteJob(String jobName, String jobGroup) {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			if (jobKey != null) {
				scheduler.deleteJob(jobKey);
				log.info("删除定时任务，name:" + jobName + ",group:" + jobGroup);
			}
		} catch (SchedulerException e) {
			throw new BusinessException("定时任务删除失败!", e);
		}
	}
	
	/**
	 * 停止任务。
	 * @param jobName
	 * @param jobGroup
	 */
	public void stopJob(String jobName, String jobGroup) {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		try {
			if (triggerKey != null) {
				scheduler.unscheduleJob(triggerKey);
				log.info("停止定时任务，name:" + jobName + ",group:" + jobGroup);
			}
		} catch (SchedulerException e) {
			throw new BusinessException("定时任务删除失败!", e);
		}
	}
	
	/**
	 * 更新或重启任务。
	 * @param jobName
	 * @param jobGroup
	 * @param cron
	 */
	public void updateJob(String jobName, String jobGroup, String cron, String kindId) {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		try {
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (trigger != null) {
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				scheduler.rescheduleJob(triggerKey, trigger);
				log.info("更新或重启定时任务，name:" + jobName + ",group:" + jobGroup + ",cron:" + cron);
			} else {// 更新时没找到任务则新增。
				addJob(jobName, jobGroup, cron, kindId);
			}
		} catch (SchedulerException e) {
			throw new BusinessException("定时任务更新失败!", e);
		}
	}
	
	/**
	 * 同步数据库的添加一个任务。
	 * @param task
	 */
	public void addTask(Task task, TaskAssociationService service, TaskAssociationDto dto) {
		try {
			dao.startTransaction();
			Timestamp createTime = new Timestamp(System.currentTimeMillis());
			task.setCreateTime(createTime);
			dao.insert_cache(task);//保存Task
			dto.setTask(task);
			service.save(dto);//保存关联表。
			addJob(task.getTaskId(), task.getTaskId(), task.getCronExpression(), task.getKindId());
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("新增任务失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 同步数据库的更新一个任务。
	 * @param task
	 */
	public void updateTask(Task task) {
		Task old;
		try {
			dao.startTransaction();
			old = (Task)dao.read_cache(task.getTaskId());
			task.setStatus("1");//更新后默认自动开启任务。
			task.setKindId(old.getKindId());
			task.setCreateTime(old.getCreateTime());
			task.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			dao.update_cache(task);
			updateJob(task.getTaskId(), task.getTaskId(), task.getCronExpression(), task.getKindId());
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("更新任务失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 同步数据库的关闭一个任务。
	 * @param task
	 */
	public void stopTask(Task task) {
		Task old = (Task)dao.read(task.getTaskId());
		if (old == null) {
			throw new BusinessException("要关闭的任务不存在!");
		}
		if ("1".equals(old.getStatus())) {
			old.setStatus("0");
			try {
				dao.startTransaction();
				dao.update_cache(old);
				stopJob(old.getTaskId(), old.getTaskId());
				dao.commitTransaction();
			} catch (BusinessException ex) {
				dao.rollbackTransaction();
				throw ex;
			} catch (Exception e) {
				dao.rollbackTransaction();
				throw new BusinessException("关闭任务失败!", e);
			} finally {
				dao.closeConnection();
			}
		}
	}
	
	/**
	 * 同步数据库的开启一个任务。
	 * @param task
	 */
	public void startTask(Task task) {
		Task old = (Task)dao.read(task.getTaskId());
		if (old == null) {
			throw new BusinessException("要开启的任务不存在!");
		}
		if ("0".equals(old.getStatus())) {
			old.setStatus("1");
			try {
				dao.startTransaction();
				dao.update_cache(old);
				updateJob(old.getTaskId(), old.getTaskId(), old.getCronExpression(), old.getKindId());
				dao.commitTransaction();
			} catch (BusinessException ex) {
				dao.rollbackTransaction();
				throw ex;
			} catch (Exception e) {
				dao.rollbackTransaction();
				throw new BusinessException("开启任务失败!", e);
			} finally {
				dao.closeConnection();
			}
		}
	}
	
	/**
	 * 同步数据库的删除一个任务。
	 * @param task
	 */
	public void deleteTask(Task task, TaskAssociationService service) {
		try {
			dao.startTransaction();
			service.delete(task);
			dao.delete_cache(task);
			deleteJob(task.getTaskId(), task.getTaskId());
			dao.commitTransaction();
		} catch (BusinessException ex) {
			dao.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			dao.rollbackTransaction();
			throw new BusinessException("删除任务失败!", e);
		} finally {
			dao.closeConnection();
		}
	}
	
	/**
	 * 读取一条任务信息。
	 * @param task
	 * @return
	 */
	public Task read(Task task) {
		return (Task)dao.read(task.getTaskId());
	}
	
	/**
	 * 查询数据库中的所有任务。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> queryAll() {
		return (List<Task>) dao.query("1=1 ORDER BY TaskId");
	}
}
