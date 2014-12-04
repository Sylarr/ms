package com.sinosoft.ms.dto;

public class ServiceDto {
	
	private String ServiceName;
	private String Version;
	private String MethodName;
	
	public String getServiceName() {
		return ServiceName;
	}
	public void setServiceName(String serviceName) {
		ServiceName = serviceName;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getMethodName() {
		return MethodName;
	}
	public void setMethodName(String methodName) {
		MethodName = methodName;
	}
}
