package com.sinosoft.ms.remote.action;

import com.sinosoft.ms.dto.ServiceDto;
import com.sinosoft.ms.remote.service.RemoteServiceInvocation;

public class RemoteServiceAction {
	
	/** 远程服务的调用类 */
	private RemoteServiceInvocation invocation;
	
	public void setInvocation(RemoteServiceInvocation invocation) {
		this.invocation = invocation;
	}

	public String execute() {
		
		invocation.inject(serviceDto, args);
		invocation.execute();
		
		return null;
	}
	
	/** -------------以下为Action参数------------- */
	
	private ServiceDto serviceDto;
	private Object[] args;

	public ServiceDto getServiceDto() {
		return serviceDto;
	}
	public void setServiceDto(ServiceDto serviceDto) {
		this.serviceDto = serviceDto;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
}
