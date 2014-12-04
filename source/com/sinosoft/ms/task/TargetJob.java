package com.sinosoft.ms.task;

/**
 * 执行的具体的目标任务的公共接口，每一个消息推送方式一个实例。
 * 
 * @author HanYan
 * @date 2014-10-29
 */
public interface TargetJob {
	
	/**
	 * 任务执行方法。
	 * @param taskId
	 */
	public void execute(String taskId);
}
