package com.sinosoft.ms.remote.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web-service.xml（称为远程服务注册表配置文件）的映射类。
 * 
 * @author HanYan
 * @date 2014-11-13
 */
public class RemoteServiceMapping {
	
	private String name;
	private String version;
	private String namespace;
	Map<String, List<String>> methods;
	
	public RemoteServiceMapping(String name, String version) {
		this(name, version, null, new HashMap<String, List<String>>());
	}
	
	public RemoteServiceMapping(String name, String version, String namespace) {
		this(name, version, namespace, new HashMap<String, List<String>>());
	}

	public RemoteServiceMapping(String name, String version, String namespace,
			Map<String, List<String>> methods) {
		super();
		this.name = name;
		this.version = version;
		this.namespace = namespace;
		this.methods = methods;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public Map<String, List<String>> getMethods() {
		return methods;
	}
	public void setMethods(Map<String, List<String>> methods) {
		this.methods = methods;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteServiceMapping other = (RemoteServiceMapping) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	public RemoteServiceMapping addMethod(String methodName, List<String> args) {
		if (methodName != null && methodName.trim().length() > 0) {
			if (methods == null) {
				methods = new HashMap<String, List<String>>();
			}
			methods.put(methodName, args);
		}
		return this;
	}
}
